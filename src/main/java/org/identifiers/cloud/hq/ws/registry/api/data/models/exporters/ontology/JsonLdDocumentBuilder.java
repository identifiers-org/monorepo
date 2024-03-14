package org.identifiers.cloud.hq.ws.registry.api.data.models.exporters.ontology;

import org.identifiers.cloud.hq.ws.registry.api.data.models.exporters.ExportDocumentBuilderException;
import org.identifiers.cloud.hq.ws.registry.api.data.models.exporters.ExportedDocument;
import org.identifiers.cloud.hq.ws.registry.data.models.Namespace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.data.models.exporters.ontology
 * Timestamp: 2018-11-15 12:20
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
public class JsonLdDocumentBuilder implements ExportOntologyDocumentBuilder {
    private static final Logger logger = LoggerFactory.getLogger(JsonLdDocumentBuilder.class);

    private final JsonLdDocument document = new JsonLdDocument();

    @Override
    public void buildContext(String namespace, String reference) throws ExportOntologyDocumentBuilderException {
        document.addContext(namespace, reference);
    }

    @Override
    public void build(Serializable entry) throws ExportOntologyDocumentBuilderException {
        document.addEntry(entry);
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
