package org.identifiers.cloud.hq.validatorregistry.helpers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RorApiHelper {
    @Value("${org.identifiers.cloud.ror.id-fetch-url-template}")
    private String idFetchUrlTemplate;
    @Value("${org.identifiers.cloud.ror.query-url-template}")
    private String queryUrlTemplate;


    private final RestTemplate restTemplateExternal;


    public Optional<RorInformation> fetchRorInfoFor(String rorId) {
        try {
            var httpResponse = restTemplateExternal.getForEntity(idFetchUrlTemplate, RorInformation.class, rorId);

            return httpResponse.getStatusCode().is2xxSuccessful() ?
                    Optional.ofNullable(httpResponse.getBody()) : Optional.empty();
        } catch (RestClientException e) {
            log.error("Exception when retrieving ror id {}: {}", rorId, e.getMessage());
            log.debug("Exception stacktrace", e);
            return Optional.empty();
        }
    }

    public List<RorInformation> query(String query) {
        try {
            var httpResponse = restTemplateExternal.getForEntity(queryUrlTemplate, RorQueryResponse.class, query);
            if (httpResponse.getStatusCode().is2xxSuccessful() && httpResponse.getBody() != null) {
                return httpResponse.getBody().items();
            } else {
                return List.of();
            }
        } catch (RestClientException e) {
            log.error("Exception when querying ror: {}", e.getMessage());
            log.debug("Exception stacktrace", e);
            return List.of();
        }
    }



    public record RorInformation(
            String id,
            String name,
            List<String> links,
            RorCountry country
    ) {}

    public record RorCountry(
            @JsonProperty("country_name") String name,
            @JsonProperty("country_code") String code
    ) {}

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    private record RorQueryResponse(
            int numberOfResults,
            int timeTaken,
            List<RorInformation> items,
            Object meta
    ) {}
}
