package org.identifiers.cloud.hq.ws.registry.api.models;

import org.identifiers.cloud.hq.ws.registry.api.data.models.exporters.RegistryExporterException;
import org.identifiers.cloud.hq.ws.registry.api.data.models.exporters.RegistryExporterFactory;
import org.identifiers.cloud.hq.ws.registry.api.responses.ServiceResponseSemanticExportRequest;
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

    public ServiceResponseSemanticExportRequest getRegistryOntology() {
        // Default response
        ServiceResponseSemanticExportRequest response = new ServiceResponseSemanticExportRequest();
        response.setHttpStatus(HttpStatus.OK);
        // No default payload this time
        try {
            response.setPayload(RegistryExporterFactory.getForJsonLdOntology().export(namespaceRepository.findAll()));
        } catch (RegistryExporterException e) {
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setErrorMessage(e.getMessage());
        }
        return response;
    }
}
