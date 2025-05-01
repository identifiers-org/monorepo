package org.identifiers.cloud.ws.sparql.services;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.common.iteration.EmptyIteration;
import org.eclipse.rdf4j.common.iteration.SingletonIteration;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.identifiers.cloud.commons.messages.models.Namespace;
import org.identifiers.cloud.commons.messages.responses.registry.ResolverDatasetPayload;
import org.identifiers.cloud.ws.sparql.data.ResolverDatasetSubscriber;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RdfsMemberResolver extends VirtualStatementResolver implements ResolverDatasetSubscriber {
    private final Map<String, String> namespaceData = new HashMap<>();
    private final ReadWriteLock rwLock = new ReentrantReadWriteLock(true);
    private final Predicate<String> baseUrlPattern = Pattern
            .compile("^https?://identifiers.org/.*").asMatchPredicate();
    private static final int baseUrlLength = "https://identifiers.org/".length();

    @Override
    public void receive(ResolverDatasetPayload resolverDatasetPayload) {
        var currentNamespaceData = resolverDatasetPayload
                .getNamespaces().stream()
                .collect(Collectors.toMap(Namespace::getPrefix, Namespace::getPattern));
        var lock = rwLock.writeLock();
        lock.lock();
        try {
            namespaceData.clear();
            namespaceData.putAll(currentNamespaceData);
        } finally {
            lock.unlock();
        }
    }




    @Override
    public boolean mayGenerateStatements(Resource subj, IRI pred, Value obj, Resource... contexts) {
        return (pred == null || pred.equals(RDFS.MEMBER)) && // predicate is rdfs:member
                (subj == null ^ obj == null) && // Only one is null
                (isIdOrgUri(subj) || isIdOrgUri(obj));
    }


    @Override
    CloseableIteration<Statement> doGetStatements(Resource subj, Resource... contexts) {
        var namespace = getNamespaceOfResourceIfExists(subj);
        if (namespace.isPresent()) {
            var stmt = valueFactory.createStatement(subj, RDFS.MEMBER, namespace.get());
            return new SingletonIteration<>(stmt);
        } else {
            return new EmptyIteration<>();
        }
    }


    @Override
    CloseableIteration<Statement> doGetStatements(Resource subj, Resource obj, Resource... contexts) {
        return new EmptyIteration<>();
    }




    boolean isIdOrgUri(Value possibleUri) {
        return possibleUri instanceof Resource r && isIdOrgUri(r);
    }

    boolean isIdOrgUri(Resource possibleUri) {
        if (possibleUri == null) return false;
        String uriStr = possibleUri.stringValue();
        return uriStr.length() > baseUrlLength && baseUrlPattern.test(uriStr);
    }

    Optional<Resource> getNamespaceOfResourceIfExists(Resource idOrgURI) {
        var curie = idOrgURI.toString().replaceFirst("^https?://identifiers.org/", "");

        // Entry key = prefix & value = luiPattern
        Optional<Map.Entry<String,String>> longestMatchingPrefix = Optional.empty();

        var lock = rwLock.readLock();
        lock.lock();
        try {
            longestMatchingPrefix = namespaceData.entrySet().stream()
                    .filter(e -> curie.startsWith(e.getKey()))
                    .max(Comparator.comparing(e -> e.getKey().length()));
        } finally {
            lock.unlock();
        }

        if (longestMatchingPrefix.isEmpty()) {
            return Optional.empty();
        } else {
            var matchedPrefix = longestMatchingPrefix.get().getKey();
            var matchedLuiPattern = longestMatchingPrefix.get().getValue();
            if (curie.length() <= matchedPrefix.length()) {
                // Incomplete curie or prefix only
                return Optional.empty();
            }
            var lui = curie.substring(matchedPrefix.length()+1); // +1 to skip separator character
            if (!Pattern.matches(matchedLuiPattern, lui)) {
                return Optional.empty();
            } else {
                var iri = valueFactory.createIRI("http://identifiers.org/" + matchedPrefix);
                return Optional.of(iri);
            }
        }
    }
}
