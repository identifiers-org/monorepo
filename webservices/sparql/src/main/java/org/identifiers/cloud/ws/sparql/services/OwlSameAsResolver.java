package org.identifiers.cloud.ws.sparql.services;

import java.util.*;
import java.util.regex.Pattern;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.common.iteration.CloseableIteratorIteration;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.vocabulary.OWL;
import org.identifiers.cloud.commons.messages.responses.registry.ResolverDatasetPayload;
import org.identifiers.cloud.commons.messages.models.Namespace;
import org.identifiers.cloud.commons.messages.models.Resource;
import org.identifiers.cloud.ws.sparql.data.ResolverDatasetSubscriber;
import org.identifiers.cloud.ws.sparql.data.URIextended;

import org.springframework.stereotype.Service;

import static java.lang.Boolean.FALSE;


@Slf4j
@Service
public final class OwlSameAsResolver extends VirtualStatementResolver implements ResolverDatasetSubscriber {
    // Perhaps reimplement this class with a Radix Tree (also known as patricia trie, radix trie or compact prefix tree)
    // or a normal Trie tree for faster resolution. See https://github.com/npgall/concurrent-trees

    private List<PrefixPatterns> prefixPatterns;

    @Override
    public void receive(ResolverDatasetPayload endpointResponse) {
        prefixPatterns = new ArrayList<>();
        for (Namespace namespace : endpointResponse.getNamespaces()) {
            PrefixPatterns prefixPattern = new PrefixPatterns(namespace.getPattern());

            for (Resource resource : namespace.getResources()) {
                prefixPattern.add(namespace.getPrefix(), resource.getUrlPattern(),
                        resource.isDeprecated(), namespace.isNamespaceEmbeddedInLui());
            }
            if (!namespace.isNamespaceEmbeddedInLui()) {
                prefixPattern.add(true, "https://identifiers.org/" + namespace.getPrefix() + ":{$id}");
                prefixPattern.add(false, "http://identifiers.org/" + namespace.getPrefix() + ":{$id}");
                prefixPattern.add(true, "https://identifiers.org/" + namespace.getPrefix() + "/{$id}");
                prefixPattern.add(true, "http://identifiers.org/" + namespace.getPrefix() + "/{$id}");
            } else {
                prefixPattern.add(false, "http://identifiers.org/{$id}");
                prefixPattern.add(true, "https://identifiers.org/{$id}");
            }
            prefixPatterns.add(prefixPattern);
        }
        log.debug("Parsed {} prefix patterns from resolver dataset", prefixPatterns.size());
    }




    @Override
    public boolean mayGenerateStatements(org.eclipse.rdf4j.model.Resource subj, IRI pred,
                                         Value obj, org.eclipse.rdf4j.model.Resource... contexts) {
        return (pred == null || pred.equals(OWL.SAMEAS)) && // predicate is owl:sameAs
                (subj != null || obj != null) && // One is null and the other is not null
                (subj == null || obj == null);
    }

    @Override
    CloseableIteration<Statement> doGetStatements(org.eclipse.rdf4j.model.Resource subj,
                                                  org.eclipse.rdf4j.model.Resource... contexts) {
        Optional<Boolean> activeflag = getActiveFlag(contexts);
        final String stringValue = subj.stringValue();
        final List<URIextended> sameAsURIs = getSameAsURIs(stringValue, activeflag);
        final Iterator<Statement> iter = sameAsURIs
                .stream()
                .map(urIextended -> {
                    if (urIextended.urlPattern() != null) {
                        IRI uri = valueFactory.createIRI(urIextended.urlPattern());
                        return valueFactory.createStatement(subj, OWL.SAMEAS, uri);
                    } else {
                        return valueFactory.createStatement(subj, OWL.SAMEAS, valueFactory.createBNode());
                    }
                })
                .iterator();

        return new CloseableIteratorIteration<>(iter);
    }

    @Override
    CloseableIteration<Statement> doGetStatements(org.eclipse.rdf4j.model.Resource subj,
                                                  org.eclipse.rdf4j.model.Resource obj,
                                                  org.eclipse.rdf4j.model.Resource... contexts) {
        Optional<Boolean> activeflag = getActiveFlag(contexts);
        var uris = getSameAsURIs(obj.stringValue(), activeflag);

        // Query triple is ?var [predicate] [obj]
        final Iterator<Statement> iter = uris.stream()
                .map(uriExtended -> {
                    IRI uri = valueFactory.createIRI(uriExtended.urlPattern());
                    return valueFactory.createStatement(uri, OWL.SAMEAS, obj);
                })
                .iterator();
        // For each result we had from translation we now turn them into a
        // statement.
        return new CloseableIteratorIteration<>(iter);
    }

    public List<URIextended> getSameAsURIs(String uri, boolean activeFlag) {
        return getSameAsURIs(uri, Optional.of(activeFlag));
    }

    public List<URIextended> getSameAsURIs(String uri, Optional<Boolean> activeFlag) {
        log.debug("getSameAsURIs for {}, active flag: {}", uri, activeFlag.isPresent() && activeFlag.get());
        List<URIextended> resultList = new ArrayList<>();
        for (PrefixPatterns patterns : prefixPatterns) {
            for (BeforeAfterActive beforeAndAfterId : patterns.beforeAndAfterId) {

                if (beforeAndAfterId.matches(uri)) {
                    String id = beforeAndAfterId.id(uri);
                    if (patterns.idPattern == null || patterns.idPattern.matcher(id).matches()) {
                        log.debug("ID pattern {} matches {}", patterns.idPattern, id);
                        addAll(resultList, id, patterns.beforeAndAfterId, uri, activeFlag);
                    }
                }
            }
        }
        return resultList;
    }




    private void addAll(List<URIextended> resultList, String id,
                        List<BeforeAfterActive> beforeAndAfterIds,
                        String uri, Optional<Boolean> activeFlag) {
        for (BeforeAfterActive beforeAndAfterId : beforeAndAfterIds) {
            String newUrl = beforeAndAfterId.beforeId + id + beforeAndAfterId.afterId;
            if (uri.equals(newUrl)) continue;
            if (activeFlag.isEmpty() || Objects.equals(activeFlag.get(), beforeAndAfterId.active)) {
                resultList.add(new URIextended(newUrl, !activeFlag.orElse(FALSE)));
            }
        }
    }




    private static class PrefixPatterns {
        private final Pattern idPattern;
        private final List<BeforeAfterActive> beforeAndAfterId = new ArrayList<>();

        public PrefixPatterns(String idPattern) {
            if (idPattern != null && !idPattern.isBlank()) {
                this.idPattern = Pattern.compile(idPattern);
            } else {
                this.idPattern = null;
            }
        }

        public void add(boolean deprecated, String urlPattern) {
            this.add(null, urlPattern, deprecated, false);
        }

        public void add(String prefix, String urlPattern, boolean deprecated, boolean namespaceEmbeddedInLui) {
            final String idString = "{$id}";
            int startIndexOf = urlPattern.indexOf(idString);
            int endIndexOf = startIndexOf + idString.length();
            if (namespaceEmbeddedInLui) {
                startIndexOf = startIndexOf - prefix.length() - 1; //Slide to before prefix and sep character
            }

            String beforeId = urlPattern.substring(0, startIndexOf);
            String afterId = urlPattern.substring(endIndexOf);
            this.beforeAndAfterId.add(new BeforeAfterActive(beforeId, afterId, !deprecated));
        }
    }

    private record BeforeAfterActive (String beforeId, String afterId, boolean active) {
        boolean matches(String uri1) {
            return uri1.startsWith(beforeId) && uri1.endsWith(afterId);
        }
        private String id(String uri) {
            return uri.substring(beforeId.length(), uri.length() - afterId.length());
        }
    }
}
