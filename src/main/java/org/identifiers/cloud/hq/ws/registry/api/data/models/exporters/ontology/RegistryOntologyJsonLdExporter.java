package org.identifiers.cloud.hq.ws.registry.api.data.models.exporters.ontology;

import org.identifiers.cloud.hq.ws.registry.api.data.models.exporters.ExportedDocument;
import org.identifiers.cloud.hq.ws.registry.api.data.models.exporters.RegistryExporter;
import org.identifiers.cloud.hq.ws.registry.api.data.models.exporters.RegistryExporterException;
import org.identifiers.cloud.hq.ws.registry.data.models.Namespace;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        // Entry for Ontology
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
        // Entry for Data Collection
        entry = new HashMap<>();
        entry.put("@id", "http://rdf.identifiers.org/ontology/DataCollection");
        entry.put("@type", "owl:Class");
        entry.put("rdfs:comment", "An instance of a database described with an Identifiers.org URI.");
        entry.put("rdfs:label", "DataCollection");
        subentry = new HashMap<>();
        subentry.put("@id", "http://semanticscience.org/resource/SIO_000089");
        entry.put("skos:narrower", subentry);
        documentBuilder.build((Serializable) entry);
        // Entry for Data Resource
        entry = new HashMap<>();
        entry.put("@id", "http://rdf.identifiers.org/ontology/DataResource");
        entry.put("@type", "owl:Class");
        entry.put("rdfs:comment", "An instance of a database described with an Identifiers.org URI.");
        entry.put("rdfs:label", "DataResource");
        subentry = new HashMap<>();
        subentry.put("@id", "http://semanticscience.org/resource/SIO_000756");
        entry.put("skos:narrower", subentry);
        documentBuilder.build((Serializable) entry);
        // Entry for Database
        entry = new HashMap<>();
        entry.put("@id", "http://rdf.identifiers.org/ontology/database");
        entry.put("@type", "owl:ObjectProperty");
        subentry = new HashMap<>();
        subentry.put("@id", "http://semanticscience.org/resource/SIO_000068");
        entry.put("owl:subPropertyOf", subentry);
        entry.put("rdfs:comment", "A predicate for describing that a DataResource belongs to a DataCollection.");
        subentry = new HashMap<>();
        subentry.put("@id", "http://rdf.identifiers.org/ontology/DataCollection");
        entry.put("rdfs:domain", subentry);
        entry.put("rdfs:label", "is entry of");
        subentry = new HashMap<>();
        subentry.put("@id", "http://rdf.identifiers.org/ontology/DataResource");
        entry.put("rdfs:range", subentry);
        documentBuilder.build((Serializable) entry);
        // Entry for Link
        entry = new HashMap<>();
        entry.put("@id", "http://rdf.identifiers.org/ontology/link");
        entry.put("@type", "owl:ObjectProperty");
        entry.put("rdfs:label", "has relation");
        entry.put("rdfs:comment", "A predicate for describing that a DataResource has a relation to another DataResource.");
        subentry = new HashMap<>();
        subentry.put("@id", "http://rdf.identifiers.org/ontology/DataResource");
        entry.put("rdfs:domain", subentry);
        subentry = new HashMap<>();
        subentry.put("@id", "http://rdf.identifiers.org/ontology/DataResource");
        entry.put("rdfs:range", subentry);
        subentry = new HashMap<>();
        subentry.put("@id", "http://semanticscience.org/resource/SIO_000001");
        entry.put("skos:narrower", subentry);
        documentBuilder.build((Serializable) entry);
        // Entry for link_to
        entry = new HashMap<>();
        entry.put("@id", "http://rdf.identifiers.org/ontology/link_to");
        entry.put("@type", "owl:ObjectProperty");
        entry.put("rdfs:label", "has a link to");
        entry.put("rdfs:comment", "A predicate for describing that a DataResource has a cross reference to another DataResource.");
        subentry = new HashMap<>();
        subentry.put("@id", "http://rdf.identifiers.org/ontology/DataResource");
        entry.put("rdfs:domain", subentry);
        subentry = new HashMap<>();
        subentry.put("@id", "http://rdf.identifiers.org/ontology/DataResource");
        entry.put("rdfs:range", subentry);
        subentry = new HashMap<>();
        subentry.put("@id", "http://semanticscience.org/resource/SIO_000628");
        entry.put("skos:narrower", subentry);
        subentry = new HashMap<>();
        subentry.put("@id", "http://rdf.identifiers.org/ontology/link");
        entry.put("owl:subPropertyOf", subentry);
        documentBuilder.build((Serializable) entry);
        // Entry for link_for
        entry = new HashMap<>();
        entry.put("@id", "http://rdf.identifiers.org/ontology/link_from");
        entry.put("@type", "owl:ObjectProperty");
        entry.put("rdfs:label", "has a link from");
        entry.put("rdfs:comment", "A predicate for describing that a DataResource has a cross reference from another DataResource.");
        subentry = new HashMap<>();
        subentry.put("@id", "http://rdf.identifiers.org/ontology/DataResource");
        entry.put("rdfs:domain", subentry);
        subentry = new HashMap<>();
        subentry.put("@id", "http://rdf.identifiers.org/ontology/DataResource");
        entry.put("rdfs:range", subentry);
        subentry = new HashMap<>();
        subentry.put("@id", "http://semanticscience.org/resource/SIO_000212");
        entry.put("skos:narrower", subentry);
        subentry = new HashMap<>();
        subentry.put("@id", "http://rdf.identifiers.org/ontology/link");
        entry.put("owl:subPropertyOf", subentry);
        documentBuilder.build((Serializable) entry);

    }

    private void addNamespaces(List<Namespace> namespaces) {
        // DISCLAIMER NOTE - My eyes are BLEEDING by looking at this disaster, but let's get this done for the
        // biohackathon and we can polish it later
        namespaces.stream().forEach(namespace -> {
            Map<String, Object> entry = new HashMap<>();
            entry.put("@id", String.format("http://identifiers.org/%s", namespace.getPrefix()));
            entry.put("@type", "http://rdf.identifiers.org/ontology/DataCollection");
            entry.put("rdfs:comment", namespace.getDescription());
            entry.put("rdfs:label", namespace.getName());
            entry.put("foaf:homepage", namespace.getResources().stream().map(resource -> {
                Map<String, String> foafEntry = new HashMap<>();
                foafEntry.put("@id", resource.getResourceUrl().trim());
                return foafEntry;
            }).collect(Collectors.toList()));
            documentBuilder.build((Serializable) entry);
        });
    }

    public RegistryOntologyJsonLdExporter(ExportOntologyDocumentBuilder documentBuilder) {
        this.documentBuilder = documentBuilder;
    }

    @Override
    public ExportedDocument export(List<Namespace> namespaces) throws RegistryExporterException {
        // Set the contexts
        addContext();
        // Add the fixed items of the graph
        addFixedTrail();
        // Add the namespaces
        addNamespaces(namespaces);
        return documentBuilder.getDocument();
    }

}
