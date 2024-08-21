package org.identifiers.cloud.hq.ws.registry.models.validators;

import lombok.AllArgsConstructor;
import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefixPayload;
import org.identifiers.cloud.hq.ws.registry.data.models.Resource;
import org.identifiers.cloud.hq.ws.registry.data.services.ResourceService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import static org.identifiers.cloud.hq.ws.registry.models.helpers.ResourceAccessHelper.PROVIDER_URL_PATTERN_PLACEHOLDER_ID;
import static org.springframework.util.StringUtils.hasText;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models.validators
 * Timestamp: 2019-03-14 15:49
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
// TODO I don't think it needs to be of "prototype" scope
@Component
@AllArgsConstructor
@Scope("prototype")
@Qualifier("PrefixRegistrationRequestValidatorProviderUrlPattern")
public class PrefixRegistrationRequestValidatorProviderUrlPattern implements PrefixRegistrationRequestValidator {
    final ResourceService resourceService;

    @Override
    public boolean validate(ServiceRequestRegisterPrefixPayload request) throws PrefixRegistrationRequestValidatorException {
        if (request.getProviderUrlPattern() == null) {
            // TODO In future iterations, use a different mechanism for reporting back why this is not valid, and leave exceptions for non-recoverable conditions
            throw new PrefixRegistrationRequestValidatorException("MISSING required Provider URL Pattern");
        }
        // Check that PLACEHOLDER_ID is uniquely present
        if (StringUtils.countOccurrencesOf(request.getProviderUrlPattern(), PROVIDER_URL_PATTERN_PLACEHOLDER_ID) != 1) {
            // TODO In future iterations, use a different mechanism for reporting back why this is not valid, and leave exceptions for non-recoverable conditions
            throw new PrefixRegistrationRequestValidatorException(String.format("URL Pattern must include the '%s' placeholder", PROVIDER_URL_PATTERN_PLACEHOLDER_ID));
        }
        // Check if a similar pattern is not already in registry
        Resource resourceWithSimilarPattern = resourceService.findSimilarByUrlPattern(request.getProviderUrlPattern());
        if (resourceWithSimilarPattern != null) {
            String message = "URL Pattern is too similar to existing resource.";
            if(hasText(request.getSampleId())) {
                var idorgUri = "http://identifiers.org";
                String pcode = resourceWithSimilarPattern.getProviderCode();
                if (hasText(pcode) && !"CURATOR_REVIEW".equals(pcode)) {
                    idorgUri += "/" + resourceWithSimilarPattern.getProviderCode();
                }
                idorgUri += "/" + resourceWithSimilarPattern.getNamespace().getPrefix() + ":" + request.getSampleId();
                message += " Check this URI: " + idorgUri + ".";
            }
            message += " If this is wrong, contact our support via the feedback button.";
            throw new PrefixRegistrationRequestValidatorException(message);
        }

        WebPageChecker webPageChecker = WebPageCheckerFactory.getWebPageChecker();
        String urlToCheck = StringUtils.replace(request.getProviderUrlPattern(), PROVIDER_URL_PATTERN_PLACEHOLDER_ID, "placeholderId");
        // Remove the PLACEHOLDER_ID from thr URL for standalone checking
        if (!webPageChecker.checkForValidUrl(urlToCheck)) {
            throw new PrefixRegistrationRequestValidatorException(String.format("Invalid URL '%s'", request.getProviderUrlPattern()));
        }
        return true;
    }
}
