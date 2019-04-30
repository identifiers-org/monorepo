package org.identifiers.cloud.hq.ws.registry.models;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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
    private static final int WS_REQUEST_RETRY_MAX_ATTEMPTS = 12;
    private static final int WS_REQUEST_RETRY_BACK_OFF_PERIOD = 1500; // 1.5 seconds
    // TODO Remove these timeouts now set by the configuration class
    private static final int WS_REQUEST_CONNECT_TIMEOUT = 1000; // 1 second
    private static final int WS_REQUEST_READ_TIMEOUT = 1000; // 1 second

    @Value("${org.identifiers.cloud.hq.ws.registry.backend.service.miridcontroller.host}")
    private String wsMirIdControllerHost;
    @Value("${org.identifiers.cloud.hq.ws.registry.backend.service.miridcontroller.port}")
    private String wsMirIdControllerPort;

    // Helpers
    private String getMirIdServiceBaseUrl() {
        // We should allow HTTP / HTTPS configurability in case we want the cluster internal traffic to be encrypted,
        // otherwise this is fine, as all HQ services are deployed within the same cluster
        return String.format("http://%s:%s/mirIdApi", wsMirIdControllerHost, wsMirIdControllerPort);
    }

    private String getWsMirIdMintingUrl() {
        // TODO
        return String.format("%s/mintId", getMirIdServiceBaseUrl());
    }

    private String getWsMirIdKeepingAliveUrl() {
        // TODO
    }
    // END - Helpers

    @Retryable(maxAttempts = WS_REQUEST_RETRY_MAX_ATTEMPTS,
            backoff = @Backoff(delay = WS_REQUEST_RETRY_BACK_OFF_PERIOD))
    @Override
    public String mintId() throws MirIdServiceException {
        log.info("Requesting MIR ID MINTING");
        int status = 0;
        String mirId = null;
        // TODO Refactor this to use the configuration provided REST Template
        HttpURLConnection connection = null;
        try {

            // TODO Old request method to be removed
            URL requestUrl = new URL(String.format("%s/mintId", getMirIdServiceBaseUrl()));
            connection = (HttpURLConnection) requestUrl.openConnection();
            connection.setInstanceFollowRedirects(false);
            connection.setConnectTimeout(WS_REQUEST_CONNECT_TIMEOUT);
            connection.setReadTimeout(WS_REQUEST_READ_TIMEOUT);
            connection.setRequestMethod("GET");
            status = connection.getResponseCode();
            if (status == 200) {
                // Whaaaaat!?!? Thank you Baeldung! https://www.baeldung.com/java-http-request
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                mirId = content.toString();
            }
        } catch (RuntimeException | IOException e) {
            throw new MirIdServiceException(e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        if (status != 200) {
            throw new MirIdServiceException(String.format("MIR ID minting FAILED, status code '%d'", status));
        }
        log.info(String.format("MIR ID MINTING, newly minted ID '%s'", mirId));
        return mirId;
    }

    @Retryable(maxAttempts = WS_REQUEST_RETRY_MAX_ATTEMPTS,
            backoff = @Backoff(delay = WS_REQUEST_RETRY_BACK_OFF_PERIOD))
    @Override
    public void keepAlive(String mirId) throws MirIdServiceException {
        log.info(String.format("Requesting '%s' MIR ID to be kept alive", mirId));
        int status = 0;
        // TODO Refactor this to use the configuration provided REST Template
        HttpURLConnection connection = null;
        try {
            // TODO Old request method to be removed
            URL requestUrl = new URL(String.format("%s/keepAlive/%s", getMirIdServiceBaseUrl(), mirId));
            connection = (HttpURLConnection) requestUrl.openConnection();
            connection.setInstanceFollowRedirects(false);
            connection.setConnectTimeout(WS_REQUEST_CONNECT_TIMEOUT);
            connection.setReadTimeout(WS_REQUEST_READ_TIMEOUT);
            connection.setRequestMethod("GET");
            status = connection.getResponseCode();
            // I'm not interested on the content back from the MIR ID controller, just the HTTP Status
        } catch (RuntimeException | IOException e) {
            throw new MirIdServiceException(String.format("MIR ID '%s' keepAlive FAILED, status code '%d'", mirId, status));
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        if (status >= 500) {
            // We've got an error on the other side
            throw new MirIdServiceException(String.format("MIR ID '%s' keepAlive FAILED, status code '%d', " +
                    "something went WRONG on the other side!", mirId, status));
        } else if (status >= 400) {
            // We've got an error on our side
            log.error(String.format("MIR ID '%s' keepAlive FAILED, status code '%d', " +
                    "WE did something WRONG", mirId, status));
        } else if (status >= 300) {
            // This is a unicorn at this current iteration of the platform development
            throw new MirIdServiceException(String.format("MIR ID '%s' keepAlive FAILED, status code '%d', " +
                    "CONGRATULATIONS! YOU FOUND THE UNICORN! Something is deeply wrong because this iteration of the " +
                    "platform development has no redirections for the MIR ID Controller API Service", mirId, status));
        } else {
            // If we get here, it is within the HTTP 2xx status space
            log.info(String.format("SUCCESS, Request for '%s' MIR ID to be kept alive", mirId));
        }
    }
}
