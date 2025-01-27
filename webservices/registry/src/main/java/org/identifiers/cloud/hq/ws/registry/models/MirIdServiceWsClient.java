package org.identifiers.cloud.hq.ws.registry.models;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models
 * Timestamp: 2019-03-26 14:22
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This implementation of MIR ID Service delegates the operations on identifiers.org MIR ID Controller API.
 */
@Component
@Slf4j
public class MirIdServiceWsClient implements MirIdService {
    private static final String propPrefix = "org.identifiers.cloud.hq.ws.registry.backend.service.miridcontroller";
    private final RestTemplate restTemplate;

    private final Duration wsRequestConnectTimeout;
    private final Duration wsRequestReadTimeout;
    private final String wsMirIdControllerHost;
    private final String wsMirIdControllerPort;

    public MirIdServiceWsClient(@Qualifier("miridRestTemplate") RestTemplate restTemplate,
                                @Value("${org.identifiers.cloud.hq.ws.registry.backend.service.miridcontroller.request.connect_timeout}")
                                Duration wsRequestConnectTimeout,
                                @Value("${org.identifiers.cloud.hq.ws.registry.backend.service.miridcontroller.request.read_timeout}")
                                Duration wsRequestReadTimeout,
                                @Value("${org.identifiers.cloud.hq.ws.registry.backend.service.miridcontroller.host}")
                                String wsMirIdControllerHost,
                                @Value("${org.identifiers.cloud.hq.ws.registry.backend.service.miridcontroller.port}")
                                String wsMirIdControllerPort) {
        this.restTemplate = restTemplate;
        this.wsRequestConnectTimeout = wsRequestConnectTimeout;
        this.wsRequestReadTimeout = wsRequestReadTimeout;
        this.wsMirIdControllerHost = wsMirIdControllerHost;
        this.wsMirIdControllerPort = wsMirIdControllerPort;
    }

    @Retryable(maxAttemptsExpression = "${org.identifiers.cloud.hq.ws.registry.backend.service.miridcontroller.request.retry_attempts}",
            backoff = @Backoff(delayExpression = "${org.identifiers.cloud.hq.ws.registry.backend.service.miridcontroller.request.retry_backoff}"))
    @Override
    public String mintId() throws MirIdServiceException {
        log.info("Requesting MIR ID MINTING");
        String mirId = null;
        try {
            ResponseEntity<String> response = doGetRequest(getWsMirIdMintingUrl());
            if (response.getStatusCode() != HttpStatus.OK) {
                throw new MirIdServiceException(String.format("MIR ID minting FAILED, status code '%s'", response.getStatusCode()));
            }
            if (!response.hasBody()) {
                throw new MirIdServiceException(String.format("MIR ID minting FAILED, NO BODY IN THE RESPONSE, response -> '%s'", response));
            }
            if (StringUtils.isNotBlank(response.getBody())) {
                mirId = response.getBody();
            } else {
                throw new MirIdServiceException("BLANK RESPONSE from MIR ID service while minting!");
            }
        } catch (RestClientException | NullPointerException e) {
            throw new MirIdServiceException(e.getMessage(), e);
        }
        log.info(String.format("MIR ID MINTING, newly minted ID '%s'", mirId));
        return mirId;
    }

    @Retryable(maxAttemptsExpression = "${org.identifiers.cloud.hq.ws.registry.backend.service.miridcontroller.request.retry_attempts}",
            backoff = @Backoff(delayExpression = "${org.identifiers.cloud.hq.ws.registry.backend.service.miridcontroller.request.retry_backoff}"))
    @Override
    public void keepAlive(String mirId) throws MirIdServiceException {
        log.info(String.format("Requesting '%s' MIR ID to be kept alive", mirId));
        int status = 500;
        HttpURLConnection connection = null;
        try {
            URL requestUrl = new URL(String.format("%s/keepAlive/%s", getMirIdServiceBaseUrl(), mirId));
            connection = (HttpURLConnection) requestUrl.openConnection();
            connection.setInstanceFollowRedirects(false);
            connection.setConnectTimeout((int) wsRequestConnectTimeout.toMillis());
            connection.setReadTimeout((int) wsRequestReadTimeout.toMillis());
            connection.setRequestMethod("GET");
            status = connection.getResponseCode();
            // I'm not interested on the content back from the MIR ID controller, just the HTTP Status
        } catch (RuntimeException | IOException e) {
            throw new MirIdServiceException(String.format("MIR ID '%s' keepAlive FAILED, status code '%d'", mirId, status), e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        HttpStatusCode statusCode = HttpStatusCode.valueOf(status);
        if (statusCode.is5xxServerError()) {
            // We've got an error on the other side
            throw new MirIdServiceException(String.format("MIR ID '%s' keepAlive FAILED, status code '%d', " +
                    "something went WRONG on the other side!", mirId, status));
        } else if (statusCode.is4xxClientError()) {
            // We've got an error on our side
            log.error(String.format("MIR ID '%s' keepAlive FAILED, status code '%d', " +
                    "WE did something WRONG", mirId, status));
        } else if (statusCode.is3xxRedirection()) {
            // This is a unicorn at this current iteration of the platform development
            throw new MirIdServiceException(String.format("MIR ID '%s' keepAlive FAILED, status code '%d', " +
                    "CONGRATULATIONS! YOU FOUND THE UNICORN! Something is deeply wrong because this iteration of the " +
                    "platform development has no redirections for the MIR ID Controller API Service", mirId, status));
        } else {
            // If we get here, it is within the HTTP 2xx status space
            log.info(String.format("SUCCESS, Request for '%s' MIR ID to be kept alive", mirId));
        }
    }

    // Helpers
    String getMirIdServiceBaseUrl() {
        // We should allow HTTP / HTTPS configurability in case we want the cluster internal traffic to be encrypted,
        // otherwise this is fine, as all HQ services are deployed within the same cluster
        return String.format("http://%s:%s/mirIdApi", wsMirIdControllerHost, wsMirIdControllerPort);
    }

    String getWsMirIdMintingUrl() {
        return String.format("%s/mintId", getMirIdServiceBaseUrl());
    }

    private ResponseEntity<String> doGetRequest(String url) {
        return restTemplate.getForEntity(url, String.class);
    }
    // END - Helpers
}
