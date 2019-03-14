package org.identifiers.cloud.hq.ws.registry.models.validators;

import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefixPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models.validators
 * Timestamp: 2019-03-14 15:42
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
// We don't need qualifier here?
@Component
@Scope("prototype")
@Qualifier("prefixRegistrationRequestValidatorPreferredPrefix")
public class PrefixRegistrationRequestValidatorPreferredPrefix implements PrefixRegistrationRequestValidator {
    private static Logger logger = LoggerFactory.getLogger(PrefixRegistrationRequestValidatorPreferredPrefix.class);

    class RestTemplateErrorHandler implements ResponseErrorHandler {
        ClientHttpResponse clientHttpResponse;

        @Override
        public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
            // By default we'll tell the rest template there is no error here
            return false;
        }

        @Override
        public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
            // Thus, we don't need to do anything to handle a non-existing error
        }
    }

    // TODO - Let's see how this plays with Docker, later. It should go through a discovery service, but I'll find out
    // TODO - later how to lay all the pieces together for testing, development and production
    @Value("${org.identifiers.cloud.ws.register.resolver.host}")
    private String resolverHost;
    @Value("${org.identifiers.cloud.ws.register.resolver.port}")
    private int resolverPort;

    @Override
    public boolean validate(ServiceRequestRegisterPrefixPayload request) throws PrefixRegistrationRequestValidatorException {
        // TODO - This method is going to call a Resolver WS, and it will do it straight away for this iteration of the
        // TODO - software, but in the future, we need to provide Resolver Web Service clients for several languages,
        // TODO - e.g. Java and Python, so people don't have to write their own code every time
        // TODO - What happens if the prefix has been requested for registration but it's in "pending" state?
        // TODO
        if (request.getPreferredPrefix() == null) {
            throw new PrefixRegistrationRequestValidatorException("MISSING Preferred Prefix");
        }
        // TODO - This hack is only valid because the resolver does not validate the PID against the registered regular expression for the given prefix
        String fakeCompactId = String.format("%s:093846", request.getPreferredPrefix());
        String queryUrl = String.format("http://%s:%d/%s", resolverHost, resolverPort, fakeCompactId);
        logger.info("Prefix Validation, hack URL '{}'", queryUrl);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new RestTemplateErrorHandler());
        ResponseEntity<String> response = restTemplate.getForEntity(queryUrl, String.class);
        if (response.getStatusCode() != HttpStatus.NOT_FOUND) {
            String errorMessage = String.format("Preferred Prefix COULD NOT BE VALIDATED, internal status %s, IT MAY ALREADY BEEN REGISTERED", response.getStatusCode());
            logger.error(errorMessage);
            throw new PrefixRegistrationRequestValidatorException(errorMessage);
        }
        return true;
    }
}
