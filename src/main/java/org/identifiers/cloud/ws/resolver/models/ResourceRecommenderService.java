package org.identifiers.cloud.ws.resolver.models;

import org.identifiers.cloud.ws.resolver.data.models.ResourceEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: resolver
 * Package: org.identifiers.cloud.ws.resolver.models
 * Timestamp: 2018-02-27 21:34
 * ---
 */
@Component
@Scope("prototype")
public class ResourceRecommenderService implements ResourceRecommenderStrategy {
    public static final int WS_REQUEST_RETRY_MAX_ATTEMPTS = 12;
    public static final int WS_REQUEST_RETRY_BACK_OFF_PERIOD = 1500; // 1.5 seconds

    private static final Logger logger = LoggerFactory.getLogger(ResourceRecommenderService.class);

    // Re-try pattern, externalize this later if needed
    private static final RetryTemplate retryTemplate;
    static {
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(WS_REQUEST_RETRY_MAX_ATTEMPTS);

        FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
        backOffPolicy.setBackOffPeriod(WS_REQUEST_RETRY_BACK_OFF_PERIOD);

        retryTemplate = new RetryTemplate();
        retryTemplate.setRetryPolicy(retryPolicy);
        retryTemplate.setBackOffPolicy(backOffPolicy);
    }

    @Value("${org.identifiers.cloud.ws.resolver.service.recommender.host}")
    private String resourceRecommenderServiceHost;
    @Value("${org.identifiers.cloud.ws.resolver.service.recommender.port}")
    private String resourceRecommenderServicePort;

    // Error handler for the request
    class RestTemplateErrorHandler implements ResponseErrorHandler {
        ClientHttpResponse clientHttpResponse;

        @Override
        public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
            // We're going to say that it has no error so we can't handle this properly
            return false;
        }

        @Override
        public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
            logger.error("The following error came back from the Recommender Service, HTTP Status #{}, error content '{}'",
                    clientHttpResponse.getRawStatusCode(),
                    clientHttpResponse.getStatusText());
        }
    }

    @Override
    public List<RecommendedResource> getRecommendations(List<ResourceEntry> resources) throws ResourceRecommenderStrategyException{
        String recommenderEndpoint = String.format("http://%s:%s", resourceRecommenderServiceHost, resourceRecommenderServicePort);
        List<RecommendedResource> recommendations = new ArrayList<>();
        logger.info("Looking for resource recommendations at '{}'", recommenderEndpoint);
        if (!resources.isEmpty()) {
            List<ResolvedResource> resolvedResources = resources.parallelStream().map(resourceEntry -> new ResolvedResource()
                    .setId(resourceEntry.getId())
                    .setAccessURL(resourceEntry.getAccessURL())
                    .setOfficial(resourceEntry.isOfficial())).collect(Collectors.toList());
            try {
                ResourceRecommenderResponse response = retryTemplate.execute(retryContext -> {
                    RestTemplate restTemplate = new RestTemplate();
                    return restTemplate.postForObject(recommenderEndpoint,
                            new ResourceRecommenderRequest().setResolvedResources(resolvedResources),
                            ResourceRecommenderResponse.class);
                });
                if (response.getHttpStatus() == HttpStatus.OK) {
                    logger.debug("Got recommendations!");
                    recommendations = response.getPayload();
                } else {
                    String errorMessage = String.format("ERROR retrieving resource recommendations " +
                                    "from '%s', " +
                                    "error code'%s', " +
                                    "explanation '%s'",
                            recommenderEndpoint,
                            response.getHttpStatus().toString(),
                            response.getErrorMessage());
                    logger.error(errorMessage);
                    //throw new ResourceRecommenderStrategyException(errorMessage);
                }
            } catch (RuntimeException e) {
                logger.error("ERROR retrieving resource recommendations from '{}' because of '{}'",
                        recommenderEndpoint,
                        e.getMessage());
            }
        }
        return recommendations;
    }
}
