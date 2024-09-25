package org.identifiers.cloud.hq.ws.registry.models.validators.payload;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefixPayload;
import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterResourcePayload;
import org.identifiers.cloud.hq.ws.registry.data.models.Namespace;
import org.identifiers.cloud.hq.ws.registry.data.services.NamespaceService;
import org.identifiers.cloud.hq.ws.registry.models.validators.RegistrationPayloadValidator;

import java.util.Optional;
import java.util.regex.PatternSyntaxException;

import static org.apache.commons.lang3.StringUtils.isAnyBlank;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Slf4j
@RequiredArgsConstructor
public class SampleIdMatchesPatternValidator extends RegistrationPayloadValidator {
    final NamespaceService namespaceRepository;

    @Override
    public Optional<String> validate(ServiceRequestRegisterResourcePayload request, String valueLabel) {
        String namespacePrefix = request.getNamespacePrefix();
        if (isBlank(namespacePrefix)) {
            log.debug("Accepting because prefix is blank");
            return Optional.empty();
        }
        Namespace namespace = namespaceRepository.getNamespaceByPrefix(namespacePrefix);
        if (namespace == null) {
            return Optional.of("Prefix " + namespacePrefix + " does not exist");
        }

        String idPattern = namespace.getPattern();
        String sampleId = request.getSampleId();
        return validate(idPattern, sampleId);
    }

    @Override
    public Optional<String> validate(ServiceRequestRegisterPrefixPayload request, String valueLabel) {
        return validate(request.getIdRegexPattern(), request.getSampleId());
    }

    Optional<String> validate(String idPattern, String sampleId) {
        if (isAnyBlank(idPattern, sampleId)) {
            log.debug("Accepting because one of required values is blank");
            return Optional.empty();
        }

        try {
            if (sampleId.matches(idPattern)) {
                return Optional.empty();
            } else {
                return Optional.of("Sample ID does not match ID pattern");
            }
        } catch (PatternSyntaxException e) {
            return Optional.empty();
        }
    }
}
