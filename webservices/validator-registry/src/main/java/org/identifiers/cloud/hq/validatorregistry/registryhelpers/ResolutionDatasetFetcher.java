package org.identifiers.cloud.hq.validatorregistry.registryhelpers;

import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.commons.messages.models.Namespace;
import org.identifiers.cloud.commons.messages.responses.ServiceResponse;
import org.identifiers.cloud.commons.messages.responses.registry.ResolverDatasetPayload;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;

@Slf4j
@Component
public class ResolutionDatasetFetcher {

    @Value("${org.identifiers.cloud.registry.dataset-endpoint}")
    private URI datasetEndpoint;

    private final ParameterizedTypeReference<ServiceResponse<ResolverDatasetPayload>>
            datasetTypeRef = new ParameterizedTypeReference<>() {};

    private final RestTemplate restTemplateExternal;

    public ResolutionDatasetFetcher(@Qualifier("restTemplateExternal")
                                    RestTemplate restTemplateExternal) {
        this.restTemplateExternal = restTemplateExternal;
    }

    public Collection<Namespace> fetch() {
        try {
            var httpResponse = restTemplateExternal.exchange(
                    datasetEndpoint,
                    HttpMethod.GET,
                    null,
                    datasetTypeRef);

            var httpStatus = httpResponse.getStatusCode();
            var httpBody = httpResponse.getBody();

            if (httpStatus.is2xxSuccessful() && httpBody != null) {
                var namespaces = httpBody.getPayload().getNamespaces();
                log.info("Dataset collected SUCCESSFULLY w/ {} namespaces", namespaces.size());
                return namespaces;
            } else {
                log.error("Dataset collection FAILED w/ CODE {}", httpStatus);
                log.debug("HTTP BODY: {}", httpBody);
                return Collections.emptyList();
            }
        } catch (RestClientException e) {
            log.error("Dataset collection FAILED w/ EXCEPTION {}", e.getMessage());
            log.debug("Exception stacktrace", e);
            return Collections.emptyList();
        }
    }
}
