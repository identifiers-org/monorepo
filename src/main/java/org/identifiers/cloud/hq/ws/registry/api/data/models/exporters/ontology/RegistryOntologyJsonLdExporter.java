package org.identifiers.cloud.hq.ws.registry.api.data.models.exporters.ontology;

import org.identifiers.cloud.hq.ws.registry.api.data.models.exporters.ExportedDocument;
import org.identifiers.cloud.hq.ws.registry.api.data.models.exporters.RegistryExporter;
import org.identifiers.cloud.hq.ws.registry.api.data.models.exporters.RegistryExporterException;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.data.models.exporters.ontology
 * Timestamp: 2018-11-15 13:03
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * I'll probably refactor this in the future to make it more pattern generic
 */
public class RegistryOntologyJsonLdExporter implements RegistryExporter {
    private ExportOntologyDocumentBuilder documentBuilder = null;

    public RegistryOntologyJsonLdExporter(ExportOntologyDocumentBuilder documentBuilder) {
        this.documentBuilder = documentBuilder;
    }

    @Override
    public ExportedDocument export() throws RegistryExporterException {
        // Set the contexts
        documentBuilder.buildContext("dc", "http://purl.org/dc/terms/");
        documentBuilder.buildContext("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
        documentBuilder.buildContext("foaf", "http://xmlns.com/foaf/0.1/");
        documentBuilder.buildContext("owl", "http://www.w3.org/2002/07/owl#");
        documentBuilder.buildContext("skos", "http://www.w3.org/2004/02/skos/core#");
        // TODO - Add the fixed items of the graph
        // TODO - Add the namespaces
        return null;
    }
}
