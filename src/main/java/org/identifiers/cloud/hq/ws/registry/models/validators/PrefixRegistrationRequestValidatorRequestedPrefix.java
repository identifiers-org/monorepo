package org.identifiers.cloud.hq.ws.registry.models.validators;

import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefixPayload;
import org.identifiers.cloud.hq.ws.registry.data.models.Namespace;
import org.identifiers.cloud.hq.ws.registry.data.repositories.NamespaceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
@Qualifier("PrefixRegistrationRequestValidatorRequestedPrefix")
public class PrefixRegistrationRequestValidatorRequestedPrefix implements PrefixRegistrationRequestValidator {
    // TODO - Refactor this according to issue #16, at https://github.com/identifiers-org/cloud-hq-ws-registry/issues/16
    private static Logger logger = LoggerFactory.getLogger(PrefixRegistrationRequestValidatorRequestedPrefix.class);

    @Autowired
    private NamespaceRepository namespaceRepository;

    @Override
    public boolean validate(ServiceRequestRegisterPrefixPayload request) throws PrefixRegistrationRequestValidatorException {
        // TODO - This method is going to call a Resolver WS, and it will do it straight away for this iteration of the
        // TODO - software, but in the future, we need to provide Resolver Web Service clients for several languages,
        // TODO - e.g. Java and Python, so people don't have to write their own code every time
        // TODO - What happens if the prefix has been requested for registration but it's in "pending" state?
        // TODO
        if (request.getRequestedPrefix() == null) {
            logger.error("Invalid request for validating Requested Prefix, WITHOUT specifying a prefix");
            throw new PrefixRegistrationRequestValidatorException("MISSING Preferred Prefix");
        }
        try {
            // I planned on reusing the error message, but I may use different messages for logging and the client
            String errorMessage = "--- no error message has been set ---";
            Namespace foundNamespace = namespaceRepository.findByPrefix(request.getRequestedPrefix());
            if (foundNamespace != null) {
                if (foundNamespace.isDeprecated()) {
                    errorMessage = String.format("Prefix '%s' is DEPRECATED, for REACTIVATION, please, use a different " +
                            "API", request.getRequestedPrefix());
                    logger.error(String.format("Prefix Validation FAILED on prefix %s, because it ALREADY EXISTS and it's DEPRECATED", request.getRequestedPrefix()));
                    throw new PrefixRegistrationRequestValidatorException(errorMessage);
                }
                
            }
        }
        // TODO - This hack is only valid because the resolver does not validate the PID against the registered regular expression for the given prefix
        String fakeCompactId = String.format("%s:093846", request.getRequestedPrefix());
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
