package org.identifiers.cloud.hq.ws.registry.models.validators;

import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterResourcePayload;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models.validators
 * Timestamp: 2019-03-14 15:49
 *
 * @author Manuel Bernal Llinares <mbdebianS@gmail.com>
 * ---
 */
// TODO I don't think it needs to be of "prototype" scope
@Component
@Scope("prototype")
@Qualifier("ResourceRegistrationRequestValidatorAuthHelpUrl")
public class ResourceRegistrationRequestValidatorAuthHelpUrl implements ResourceRegistrationRequestValidator {
    @Override
    public boolean validate(ServiceRequestRegisterResourcePayload request) throws ResourceRegistrationRequestValidatorException {
        if (request.isProtectedUrls()) {
            if (request.getAuthHelpUrl() == null || request.getAuthHelpUrl().trim().length() == 0) {
                throw new ResourceRegistrationRequestValidatorException("MISSING required Provider URL Pattern");
            }
            if (!request.getAuthHelpUrl().toLowerCase().startsWith("http")) {
                throw new ResourceRegistrationRequestValidatorException("INVALID authentication info URL, it must be a HTTP(S) URL");
            }
            WebPageChecker webPageChecker = WebPageCheckerFactory.getWebPageChecker();
            if (!webPageChecker.checkForValidUrl(request.getAuthHelpUrl())) {
                throw new ResourceRegistrationRequestValidatorException(String.format("Invalid URL '%s'", request.getAuthHelpUrl()));
            }
        }
        return true;
    }
}
