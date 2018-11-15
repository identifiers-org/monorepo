package org.identifiers.cloud.hq.ws.registry.api.data.models.exporters.ontology;

import org.identifiers.cloud.hq.ws.registry.api.data.models.exporters.ExportedDocument;
import org.identifiers.cloud.hq.ws.registry.api.data.models.exporters.RegistryExporter;
import org.identifiers.cloud.hq.ws.registry.api.data.models.exporters.RegistryExporterException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

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

    private void addContext() {
        documentBuilder.buildContext("dc", "http://purl.org/dc/terms/");
        documentBuilder.buildContext("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
        documentBuilder.buildContext("foaf", "http://xmlns.com/foaf/0.1/");
        documentBuilder.buildContext("owl", "http://www.w3.org/2002/07/owl#");
        documentBuilder.buildContext("skos", "http://www.w3.org/2004/02/skos/core#");
    }

    private void addFixedTrail() {
        // YES, this is as dirty as it looks, it will be cleaned later on
        Map<String, Object> entry = new HashMap<>();
        entry.put("@id", "http://rdf.identifiers.org/ontology/");
        entry.put("@type", "owl:Ontology");
        Map<String, Object> subentry = new HashMap<>();
        subentry.put("@id", "http://creativecommons.org/publicdomain/zero/1.0/");
        entry.put("dc:license", subentry);
        entry.put("owl:versionInfo", "Created on 2018-03-22");
        entry.put("rdfs:comment", "Ontology for describing databases and entries in the Identifiers.org repository.");
        entry.put("rdfs:label", "Identifiers.org ontology");
        documentBuilder.build((Serializable) entry);
        // Another
        entry = new HashMap<>();
        entry.put("@id", "http://rdf.identifiers.org/ontology/DataCollection");
        entry.put("@type", "owl:Class");
        entry.put("rdfs:comment", "An instance of a database described with an Identifiers.org URI.");
        entry.put("rdfs:label", "DataCollection");
        subentry = new HashMap<>();
        subentry.put("@id", "http://semanticscience.org/resource/SIO_000089");
        entry.put("skos:narrower", subentry);
        // Another
    }

    public RegistryOntologyJsonLdExporter(ExportOntologyDocumentBuilder documentBuilder) {
        this.documentBuilder = documentBuilder;
    }

    @Override
    public ExportedDocument export() throws RegistryExporterException {
        // Set the contexts
        addContext();
        // Add the fixed items of the graph
        addFixedTrail();
        // TODO - Add the namespaces
        return documentBuilder.getDocument();
    }
}
