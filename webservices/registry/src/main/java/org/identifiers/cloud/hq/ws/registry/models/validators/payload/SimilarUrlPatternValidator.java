package org.identifiers.cloud.hq.ws.registry.models.validators.payload;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefixPayload;
import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterResourcePayload;
import org.identifiers.cloud.hq.ws.registry.data.models.Resource;
import org.identifiers.cloud.hq.ws.registry.data.services.ResourceService;
import org.identifiers.cloud.hq.ws.registry.models.validators.RegistrationPayloadValidator;

import java.util.Optional;


@RequiredArgsConstructor
public class SimilarUrlPatternValidator extends RegistrationPayloadValidator {
    final ResourceService resourceService;

    @Override
    public Optional<String> validate(ServiceRequestRegisterResourcePayload request, String valueLabel) {
        String urlPattern = request.getProviderUrlPattern();
        String sampleId = request.getSampleId();
        String pCode = request.getProviderCode();
        return validate(urlPattern, pCode, sampleId);
    }

    @Override
    public Optional<String> validate(ServiceRequestRegisterPrefixPayload request, String valueLabel) {
        String urlPattern = request.getProviderUrlPattern();
        String sampleId = request.getSampleId();
        String pCode = request.getProviderCode();
        return validate(urlPattern, pCode, sampleId);
    }

    public Optional<String> validate(String urlPattern, String pCode, String sampleId) {
        Resource existingResourceWithSimilarPattern =
                resourceService.findSimilarByUrlPattern(urlPattern);
        if (existingResourceWithSimilarPattern == null) {
            return Optional.empty();
        }

        String existingPrefix = existingResourceWithSimilarPattern.getNamespace().getPrefix();
        String message = "URL Pattern is similar to existing registry entry.";
        if(StringUtils.isNotBlank(sampleId)) {
            var idorgUri = "http://identifiers.org";
            if (StringUtils.isNotBlank(pCode) && !StringUtils.equals("CURATOR_REVIEW", pCode)) {
                idorgUri += "/" + existingResourceWithSimilarPattern.getProviderCode();
            }
            idorgUri += "/" + existingPrefix + ":" + sampleId;
            message += " The URL " + idorgUri + " should be what you want.";
        } else {
            message += " You should be able to use the existing '" + existingPrefix + "' namespace.";
        }
        message += " Please contact us if you need any assistance.";
        return Optional.of(message);
    }
}
