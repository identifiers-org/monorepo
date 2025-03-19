package org.identifiers.cloud.hq.validatorregistry.curation.verifiers;

import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.commons.messages.models.Resource;
import org.identifiers.cloud.commons.messages.responses.ServiceResponse;
import org.identifiers.cloud.commons.messages.responses.linkchecker.ServiceResponseResourceAvailabilityPayload;
import org.identifiers.cloud.commons.messages.models.CurationWarningNotification;
import org.identifiers.cloud.hq.validatorregistry.helpers.StatusHelper;
import org.identifiers.cloud.hq.validatorregistry.helpers.TargetEntityHelper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@ConditionalOnProperty(prefix="org.identifiers.cloud.verifiers.availability", name="enabled")
public class AvailabilityVerifier extends RegistryEntityVerifier<Resource> {
    public static final String NOTIFICATION_TYPE = "low-availability-resource";

    private final String availabilityInformationEndpoint;
    private final Integer minAvailability;
    private final RestTemplate restTemplate;
    private Map<Long, Float> resourceAvailabilities;

    public AvailabilityVerifier(@Qualifier("restTemplateExternal")
                                RestTemplate restTemplateExternal,
                                StatusHelper statusHelper,
                                @Value("${org.identifiers.cloud.verifiers.availability.endpoint}")
                                String availabilityInformationEndpoint,
                                @Value("${org.identifiers.cloud.verifiers.availability.min}")
                                Integer minAvailability) {
        super(statusHelper);
        this.restTemplate = restTemplateExternal;
        this.availabilityInformationEndpoint = availabilityInformationEndpoint;
        this.minAvailability = minAvailability;
    }

    @Override
    public void preValidateTask() {
        var uri = UriComponentsBuilder.fromHttpUrl(availabilityInformationEndpoint).build(minAvailability);
        var typeRef = new ParameterizedTypeReference<ServiceResponse<ServiceResponseResourceAvailabilityPayload>>() {};
        try {
            var availabilityResponse = restTemplate.exchange(uri, HttpMethod.GET, null, typeRef);

            if (availabilityResponse.getStatusCode().is2xxSuccessful() && availabilityResponse.getBody() != null) {
                resourceAvailabilities = availabilityResponse.getBody().getPayload().stream()
                        .filter(i -> i.availability() <= minAvailability)
                        .collect(
                            Collectors.toMap(
                                ServiceResponseResourceAvailabilityPayload.Item::resourceId,
                                ServiceResponseResourceAvailabilityPayload.Item::availability
                            )
                        );
            } else {
                log.info(
                        "Invalid status code when retrieving availability information: {}",
                        availabilityResponse.getStatusCode()
                );
                if (log.isDebugEnabled() && availabilityResponse.getBody() != null) {
                    log.debug("Error message: {}", availabilityResponse.getBody().getErrorMessage());
                }
                resourceAvailabilities = null;
            }
        } catch (RestClientException e) {
            log.info("Exception when retrieving availability information: {}", e.getMessage());
            log.debug("Stack trace", e);
            resourceAvailabilities = null;
        }
    }

    @Override
    public Optional<CurationWarningNotification> doValidate(Resource entity) {
        if (CollectionUtils.isEmpty(resourceAvailabilities)) {
            return Optional.empty();
        }

        if (resourceAvailabilities.containsKey(entity.getId())) {
            var avail = resourceAvailabilities.get(entity.getId());
            String globalId = String.format("%s:%s", NOTIFICATION_TYPE, entity.getId());
            String sampleUrl = entity.getUrlPattern().replace("{$id}", entity.getSampleId());
            var notification = CurationWarningNotification.builder()
                    .targetType(TargetEntityHelper.getEntityTypeOf(entity))
                    .targetId(entity.getId())
                    .globalId(globalId)
                    .type(NOTIFICATION_TYPE)
                    .details(Map.of("availability", String.valueOf(avail),
                                    "home-url", entity.getResourceHomeUrl(),
                                    "sample-url", sampleUrl))
                    .build();
            return Optional.of(notification);
        } else {
            return Optional.empty();
        }
    }
}
