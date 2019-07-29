package org.identifiers.cloud.hq.ws.registry.models.validators;

import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterResourcePayload;
import org.identifiers.cloud.hq.ws.registry.data.repositories.NamespaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models.validators
 * Timestamp: 2019-07-29 19:07
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Slf4j
@Component
@Qualifier("ResourceRegistrationRequestValidatorNamespacePrefix")
public class ResourceRegistrationRequestValidatorNamespacePrefix implements ResourceRegistrationRequestValidator {
    // Repositories
    @Autowired
    private NamespaceRepository namespaceRepository;

    @Override
    public boolean validate(ServiceRequestRegisterResourcePayload request) throws ResourceRegistrationRequestValidatorException {
        return false;
    }
}
