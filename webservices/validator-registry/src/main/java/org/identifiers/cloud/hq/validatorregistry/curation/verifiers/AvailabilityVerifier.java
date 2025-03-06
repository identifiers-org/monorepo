package org.identifiers.cloud.hq.validatorregistry.curation.verifiers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.commons.messages.models.Resource;
import org.identifiers.cloud.commons.messages.responses.ServiceResponse;
import org.identifiers.cloud.commons.messages.responses.linkchecker.ServiceResponseResourceAvailabilityPayload;
import org.identifiers.cloud.commons.messages.models.CurationWarningNotification;
import org.identifiers.cloud.hq.validatorregistry.helpers.TargetEntityHelper;
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
@RequiredArgsConstructor
@ConditionalOnProperty(prefix="org.identifiers.cloud.verifiers.availability", name="enabled")
public class AvailabilityVerifier implements RegistryEntityVerifier<Resource> {
    public static final String NOTIFICATION_TYPE = "low-availability-resource";

    @Value("${org.identifiers.cloud.verifiers.availability.endpoint}")
    private String availabilityInformationEndpoint;
    @Value("${org.identifiers.cloud.verifiers.availability.min}")
    private Integer minAvailability;

    private final RestTemplate restTemplate;

    private Map<Long, Float> resourceAvailabilities;

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
    public Optional<CurationWarningNotification> validate(Resource entity) {
        if (CollectionUtils.isEmpty(resourceAvailabilities)) {
            return Optional.empty();
        }

        if (resourceAvailabilities.containsKey(entity.getId())) {
            var avail = resourceAvailabilities.get(entity.getId());
            String globalId = String.format("%s:%s", NOTIFICATION_TYPE, entity.getId());
            var notification = CurationWarningNotification.builder()
                    .targetType(TargetEntityHelper.getEntityTypeOf(entity))
                    .targetId(entity.getId())
                    .globalId(globalId)
                    .type(NOTIFICATION_TYPE)
                    .details(Map.of("availability", String.valueOf(avail)))
                    .build();
            return Optional.of(notification);
        } else {
            return Optional.empty();
        }
    }
}
