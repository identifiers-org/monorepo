package org.identifiers.cloud.hq.ws.registry.models.validators.payload;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.identifiers.cloud.commons.messages.requests.registry.ServiceRequestRegisterPrefixPayload;
import org.identifiers.cloud.commons.messages.requests.registry.ServiceRequestRegisterResourcePayload;
import org.identifiers.cloud.hq.ws.registry.models.validators.RegistrationPayloadValidator;
import org.identifiers.cloud.hq.ws.registry.models.validators.singlevalue.UrlValueValidator;

import java.util.Optional;

import static org.identifiers.cloud.hq.ws.registry.models.helpers.ResourceAccessHelper.getResourceUrlFor;

@Slf4j
@RequiredArgsConstructor
public class SampleUrlRequestValidator extends RegistrationPayloadValidator {
    final UrlValueValidator urlStringValidator;

    @Override
    public Optional<String> validate(ServiceRequestRegisterResourcePayload resourceRequest, String valueLabel) {
        String urlPattern = resourceRequest.getProviderUrlPattern();
        String sampleId = resourceRequest.getSampleId();
        if (StringUtils.isAnyBlank(urlPattern, sampleId)) {
            log.debug("Accepting because one of required values is blank");
            return Optional.empty();
        }

        String url = getResourceUrlFor(urlPattern, sampleId);
        return this.urlStringValidator.validate(url, valueLabel, resourceRequest.isProtectedUrls());
    }

    @Override
    public Optional<String> validate(ServiceRequestRegisterPrefixPayload prefixRequest, String valueLabel) {
        String urlPattern = prefixRequest.getProviderUrlPattern();
        String sampleId = prefixRequest.getSampleId();
        if (StringUtils.isAnyBlank(urlPattern, sampleId)) {
            log.debug("Accepting because one of required values is blank");
            return Optional.empty();
        }

        String url = getResourceUrlFor(urlPattern, sampleId);
        return this.urlStringValidator.validate(url, valueLabel, prefixRequest.isProtectedUrls());
    }
}
