package org.identifiers.org.cloud.ws.metadata.models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: metadata
 * Package: org.identifiers.org.cloud.ws.metadata.models
 * Timestamp: 2018-02-08 12:02
 * ---
 */
@Component
@Scope("prototype")
public class IdResolverThroughResolverWebService implements IdResolver {
    private static Logger logger = LoggerFactory.getLogger(IdResolverThroughResolverWebService.class);

    // Re-try pattern
    public static final int WS_REQUEST_RETRY_MAX_ATTEMPTS = 12;
    public static final int WS_REQUEST_RETRY_BACK_OFF_PERIOD = 1500;    // 1.5 seconds

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

    // TODO - Error handler for the request

    @Value("${WS_METADATA_CONFIG_RESOLVER_HOST}")
    private String wsResolverHost;
    @Value("${WS_METADATA_CONFIG_RESOLVER_PORT}")
    private int wsResolverPort;

    @Override
    public List<ResolverApiResponseResource> resolve(String compactIdParameter) throws IdResolverException {
        // TODO - Again, here I'm using the Resolver Web Service, for this prototype iteration is fine, but we need to
        // TODO - keep this in mind for future iterations of the software lifecycle
        return null;
    }
}
