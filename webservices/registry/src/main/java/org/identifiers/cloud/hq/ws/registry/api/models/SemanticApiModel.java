package org.identifiers.cloud.hq.ws.registry.api.models;

import org.identifiers.cloud.commons.messages.responses.ServiceResponse;
import org.identifiers.cloud.hq.ws.registry.api.data.exporters.ExportedDocument;
import org.identifiers.cloud.hq.ws.registry.api.data.exporters.RegistryExporterException;
import org.identifiers.cloud.hq.ws.registry.api.data.exporters.RegistryExporterFactory;
import org.identifiers.cloud.hq.ws.registry.data.repositories.NamespaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @Autowired
    private NamespaceRepository namespaceRepository;

    public ServiceResponse<ExportedDocument> getRegistryOntology() {
        try {
            var payload = RegistryExporterFactory.getForJsonLdOntology().export(namespaceRepository.findAll());
            return ServiceResponse.of(payload);
        } catch (RegistryExporterException e) {
            return ServiceResponse.ofError(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
