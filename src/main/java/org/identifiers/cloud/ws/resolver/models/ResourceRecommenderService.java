package org.identifiers.cloud.ws.resolver.models;

import org.identifiers.cloud.ws.resolver.data.models.ResourceEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

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

    private String resourceRecommenderServiceHost;
    private String resourceRecommenderServicePort;

    @Override
    public List<RecommendedResource> getRecommendations(List<ResourceEntry> resources) throws ResourceRecommenderStrategyException{
        String recommenderEndpoint = String.format("http://%s:%d", resourceRecommenderServiceHost, resourceRecommenderServicePort);
        // TODO
        return null;
    }
}
