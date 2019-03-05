package org.identifiers.cloud.hq.ws.registry.api.data.models.exporters;

import org.identifiers.cloud.hq.ws.registry.api.data.models.exporters.ontology.JsonLdDocumentBuilder;
import org.identifiers.cloud.hq.ws.registry.api.data.models.exporters.ontology.RegistryOntologyJsonLdExporter;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.data.models.exporters
 * Timestamp: 2018-11-15 12:21
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * Abstract factory for registry exporters
 */
// TODO - Affected by relational refactoring ????
public class RegistryExporterFactory {
    // TODO
    public static RegistryExporter getForJsonLdOntology() {
        return new RegistryOntologyJsonLdExporter(new JsonLdDocumentBuilder());
    }
}
