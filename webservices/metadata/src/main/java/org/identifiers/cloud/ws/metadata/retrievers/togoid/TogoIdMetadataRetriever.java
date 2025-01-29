package org.identifiers.cloud.ws.metadata.retrievers.togoid;

import org.eclipse.rdf4j.query.resultio.QueryResultIO;
import org.identifiers.cloud.commons.compactidparsing.ParsedCompactIdentifier;
import org.identifiers.cloud.ws.metadata.retrievers.SparqlBasedMetadataRetriever;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.eclipse.rdf4j.query.resultio.TupleQueryResultFormat.JSON;

@Component
public class TogoIdMetadataRetriever extends SparqlBasedMetadataRetriever {
    final Resource relatedTogoIdQueryFile;
    final Set<String> namespaceBlacklist;
    public TogoIdMetadataRetriever(@Value("${org.identifiers.cloud.ws.metadata.retrievers.togoid.sparqlendpoint}")
                                   String sparqlEndpoint,
                                   @Value("${org.identifiers.cloud.ws.metadata.retrievers.togoid.namespaceblacklist}")
                                   String[] namespaceBlacklist,
                                   ResourceLoader resourceLoader) {
        super(sparqlEndpoint);
        this.namespaceBlacklist = Set.of(namespaceBlacklist);
        this.relatedTogoIdQueryFile = resourceLoader.getResource("classpath:togoIdRelatedIdentifiers.sparql");
    }

    @Override
    public String getId() {
        return "togoid";
    }

    @Override
    public boolean isEnabled(ParsedCompactIdentifier compactIdentifier) {
        return !namespaceBlacklist.contains(compactIdentifier.getNamespace());
    }

    /**
     * Gets metadata by querying the togo ID sparql endpoint for
     *   related URIs and the label of their relationship.
     *   Answer is a JSON string in the SPARQL JSON response format.
     * @param compactIdentifier to get metadata on
     * @return SPARQL result as JSON string.
     */
    @Override
    public String getRawMetaData(ParsedCompactIdentifier compactIdentifier) {
        var byteArrayOutputStream = new ByteArrayOutputStream();
        var resultHandler = QueryResultIO.createTupleWriter(JSON, byteArrayOutputStream);
        this.runTupleQuery(relatedTogoIdQueryFile, resultHandler,
                "curie", compactIdentifier.getRawRequest());
        return byteArrayOutputStream.toString(StandardCharsets.UTF_8);
    }

    /**
     * Gets metadata by querying the togoid sparql endpoint for
     *   related URIs and the label of their relationship.
     * Then creates a map where entries are [ URI -> relationship label ] paris
     * @param compactIdentifier to get metadata on
     * @return Map relatedUri -> RelationLabel
     */
    @Override
    public MultiValueMap<String, String> getParsedMetaData(ParsedCompactIdentifier compactIdentifier) {
        var result = getBaseMap();
        var queryResult = this.runTupleQuery(relatedTogoIdQueryFile,
                "curie", compactIdentifier.getRawRequest());
        if (queryResult == null) return result;

        var requiredBindings = List.of("label", "related");
        queryResult.forEach(bs -> {
            if (bs.getBindingNames().containsAll(requiredBindings)) {
                String label = bs.getValue("label").stringValue();
                String relatedUri = bs.getValue("related").stringValue();

                result.add(label, relatedUri);
            }
        });

        return result;
    }

    @Override
    public List<MediaType> getRawMediaType() {
        return MediaType.parseMediaTypes(JSON.getMIMETypes());
    }
}
