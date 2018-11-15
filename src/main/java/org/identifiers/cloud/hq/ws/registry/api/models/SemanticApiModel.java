package org.identifiers.cloud.hq.ws.registry.api.models;

import org.identifiers.cloud.hq.ws.registry.api.data.models.exporters.ExportedDocument;
import org.identifiers.cloud.hq.ws.registry.api.data.models.exporters.RegistryExporterFactory;
import org.springframework.stereotype.Component;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.models
 * Timestamp: 2018-11-15 11:54
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Component
public class SemanticApiModel {

    public ExportedDocument getRegistryOntology() {
        return RegistryExporterFactory.getForJsonLdOntology().export();
    }
}
