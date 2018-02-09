package org.identifiers.org.cloud.ws.metadata.models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
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
    class RestTemplateErrorHandler implements ResponseErrorHandler {
        ClientHttpResponse clientHttpResponse;

        @Override
        public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
            // We're going to say that it has no error so we can't handle this properly
            return false;
        }

        @Override
        public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
            logger.error("The following error came back from the Resolver, HTTP Status #{}, error content '{}'",
                    clientHttpResponse.getRawStatusCode(),
                    clientHttpResponse.getStatusText());
        }
    }

    @Value("${WS_METADATA_CONFIG_RESOLVER_HOST}")
    private String wsResolverHost;
    @Value("${WS_METADATA_CONFIG_RESOLVER_PORT}")
    private int wsResolverPort;

    @Override
    public List<ResolverApiResponseResource> resolve(String compactIdParameter) throws IdResolverException {
        // TODO - Again, here I'm using the Resolver Web Service, for this prototype iteration is fine, but we need to
        // TODO - keep this in mind for future iterations of the software lifecycle
        String queryUrl = String.format("http://%s:%d/%s", wsResolverHost, wsResolverPort, compactIdParameter);
        logger.debug("Querying resolver with '{}'", queryUrl);
        ResponseEntity<ResolverApiResponse> response = null;
        try {
            response = retryTemplate.execute(retryContext -> {
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.setErrorHandler(new RestTemplateErrorHandler());
                return restTemplate.getForEntity(queryUrl, ResolverApiResponse.class);
            });
        } catch (RuntimeException e) {
            String errorMessage = String.format("Resolution of Compact ID '%s' was NOT POSSIBLE " +
                    "through the Resolution Service at '%s', " +
                    "due to the following error '%s'",
                    compactIdParameter,
                    queryUrl,
                    e.getMessage())
            logger.error(errorMessage);
            throw new IdResolverException(errorMessage);
        }
        if (response.getStatusCode() != HttpStatus.OK) {
            // TODO - I may need to deal with those cases where whatever is the content coming back, cannot be
            // TODO - deserialized to an instance of ResolverApiResponse
            // We report back the error
            throw new IdResolverException(String.format("ERROR while trying to resolve Compact ID '%s' " +
                    "- 'HTTP Status %d, %s'",
                    compactIdParameter,
                    response.getStatusCodeValue(),
                    response.getBody().getErrorMessage()));
        }
        // Otherwise, everything went ok
        return response.getBody().getResolvedResources();
    }
}
