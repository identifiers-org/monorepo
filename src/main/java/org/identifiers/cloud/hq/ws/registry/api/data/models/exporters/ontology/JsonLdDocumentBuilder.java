package org.identifiers.cloud.hq.ws.registry.api.data.models.exporters.ontology;

import org.identifiers.cloud.hq.ws.registry.api.data.models.exporters.ExportedDocument;
import org.identifiers.cloud.hq.ws.registry.api.data.models.exporters.ExportDocumentBuilder;
import org.identifiers.cloud.hq.ws.registry.api.data.models.exporters.ExportDocumentBuilderException;
import org.identifiers.cloud.hq.ws.registry.data.models.Namespace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.data.models.exporters.ontology
 * Timestamp: 2018-11-15 12:20
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
public class JsonLdDocumentBuilder implements ExportDocumentBuilder {
    private static final Logger logger = LoggerFactory.getLogger(JsonLdDocumentBuilder.class);

    private JsonLdDocument document = new JsonLdDocument();

    public JsonLdDocumentBuilder() {
        document.addContext("dc", "http://purl.org/dc/terms/");
        document.addContext("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
        document.addContext("foaf", "http://xmlns.com/foaf/0.1/");
        document.addContext("owl", "http://www.w3.org/2002/07/owl#");
        document.addContext("skos", "http://www.w3.org/2004/02/skos/core#");
    }

    @Override
    public void build(Namespace namespace) throws ExportDocumentBuilderException {
        // TODO
    }

    @Override
    public ExportedDocument getDocument() throws ExportDocumentBuilderException {
        return document;
    }
}
