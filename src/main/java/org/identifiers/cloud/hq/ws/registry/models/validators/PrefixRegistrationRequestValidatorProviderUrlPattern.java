package org.identifiers.cloud.hq.ws.registry.models.validators;

import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefixPayload;
import org.identifiers.cloud.hq.ws.registry.models.helpers.ResourceAccessHelper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models.validators
 * Timestamp: 2019-03-14 15:49
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Component
@Scope("prototype")
@Qualifier("PrefixRegistrationRequestValidatorProviderUrlPattern")
public class PrefixRegistrationRequestValidatorProviderUrlPattern implements PrefixRegistrationRequestValidator {
    @Override
    public boolean validate(ServiceRequestRegisterPrefixPayload request) throws PrefixRegistrationRequestValidatorException {
        if (request.getProviderUrlPattern() == null) {
            // TODO In future iterations, use a different mechanism for reporting back why this is not valid, and leave exceptions for non-recoverable conditions
            throw new PrefixRegistrationRequestValidatorException("MISSING required Provider URL Pattern");
        }
        // Check that PLACEHOLDER_ID is uniquely present
        if (StringUtils.countOccurrencesOf(request.getProviderUrlPattern(), ResourceAccessHelper.PROVIDER_URL_PATTERN_PLACEHOLDER_ID) != 1) {
            // TODO In future iterations, use a different mechanism for reporting back why this is not valid, and leave exceptions for non-recoverable conditions
            throw new PrefixRegistrationRequestValidatorException(String.format("URL Pattern must include the '%s' placeholder", ResourceAccessHelper.PROVIDER_URL_PATTERN_PLACEHOLDER_ID));
        }
        WebPageChecker webPageChecker = WebPageCheckerFactory.getWebPageChecker();
        String urlToCheck = StringUtils.replace(request.getProviderUrlPattern(), ResourceAccessHelper.PROVIDER_URL_PATTERN_PLACEHOLDER_ID, "placeholderId");
        // Remove the PLACEHOLDER_ID from thr URL for standalone checking
        if (!webPageChecker.checkForValidUrl(urlToCheck)) {
            throw new PrefixRegistrationRequestValidatorException(String.format("Invalid URL '%s'", request.getProviderUrlPattern()));
        }
        return true;
    }
}
