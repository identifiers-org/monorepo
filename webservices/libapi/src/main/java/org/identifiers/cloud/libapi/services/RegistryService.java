package org.identifiers.cloud.libapi.services;

import org.identifiers.cloud.commons.messages.requests.ServiceRequest;
import org.identifiers.cloud.commons.messages.requests.registry.ServiceRequestRegisterPrefixPayload;
import org.identifiers.cloud.commons.messages.responses.ServiceResponse;
import org.identifiers.cloud.commons.messages.responses.registry.ServiceResponseRegisterPrefixPayload;
import org.identifiers.cloud.libapi.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: libapi
 * Package: org.identifiers.cloud.libapi.services
 * Timestamp: 2018-03-08 11:31
 * ---
 *
 * This class implements a client to the Registry service API.
 */
// TODO Rewriting of this, for the new registry service in HQ
public class RegistryService {
    // API subpath
    public static final String REGISTRY_API_PATH_PREFIX_REGISTRATION = "prefixRegistrationApi";
    private static final String apiVersion = "1.0";
    private static final Logger logger = LoggerFactory.getLogger(RegistryService.class);
    // Re-try pattern, externalize this later if needed
    private final RetryTemplate retryTemplate = Configuration.retryTemplate();
    // Default protocol scheme is HTTPS
    private String protocolScheme = "https";
    // Default host will be localhost
    private String host = "localhost";
    // Default port is 80
    private int port = 8180;

    private RegistryService() { }

    RegistryService(String host, String port) {
        this.host = host;
        this.port = Integer.parseInt(port);
    }

    public RegistryService setProtocolSchemeToHttp() {
        protocolScheme = "http";
        return this;
    }

    public RegistryService setProtocolSchemeToHttps() {
        protocolScheme = "https";
        return this;
    }

    private String getServiceApiEndpointBaseline() {
        return String.format("%s://%s:%d/%s", protocolScheme, host, port, REGISTRY_API_PATH_PREFIX_REGISTRATION);
    }

    /**
     * This is a generic helper method that prepares an Entity Request, used by the different kinds of POST requests
     * this service client implements.
     * @param requestBody body for the POST request
     * @param serviceApiEndpoint service enpoint for the request.
     * @param <T> type of the request body
     * @return the entity request object, or null if there was a problem creating it, e.g. the provided service endpoint
     * was invalid
     */
    private <T> RequestEntity<T> prepareEntityRequest(T requestBody, String serviceApiEndpoint) {
        RequestEntity<T> entityRequest = null;
        try {
            entityRequest = RequestEntity.post(new URI(serviceApiEndpoint)).body(requestBody);
        } catch (URISyntaxException e) {
            logger.error("INVALID URI '{}'", serviceApiEndpoint);
        }
        return entityRequest;
    }

    /**
     * Helper method to instantiate rest templates used for submitting requests to the registry service.
     * It will also set the right error handler in the rest template instance.
     * @return a new instance of the RestTemplate
     */
    private RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(Configuration.responseErrorHandler());
        return restTemplate;
    }

    /**
     * Helper method that submits a given prefix registration requests to the registry service and expects a response
     * accordingly.
     * @param request prefix registration request to be submitted.
     * @return a ResponseEntity specialized with the expected prefix registration request response type.
     */
    private ResponseEntity<ServiceResponse<ServiceResponseRegisterPrefixPayload>> doRegisterPrefixRequest(RequestEntity<ServiceRequest<ServiceRequestRegisterPrefixPayload>> request) {
        var typeRef = new ParameterizedTypeReference<ServiceResponse<ServiceResponseRegisterPrefixPayload>>() {};
        return getRestTemplate().exchange(request, typeRef);
    }

    /**
     * Helper method that submits a given validation requests to the registry service and expects a response accordingly.
     * @param request validation request to be submitted.
     * @return a ResponseEntity specialized with the expected validation request response type.
     */
    private ResponseEntity<ServiceResponse<ServiceResponseRegisterPrefixPayload>> doValidateRequest(RequestEntity<ServiceRequest<ServiceRequestRegisterPrefixPayload>> request) {
        var typeRef = new ParameterizedTypeReference<ServiceResponse<ServiceResponseRegisterPrefixPayload>>() {};
        return getRestTemplate().exchange(request, typeRef);
    }

    /**
     * This helper method will submit a validation request to the given service endpoint, using the given payload.
     * Expecting the validation request response from the registry service.
     * Full validation is no longer offer by the HQ Registry API Service
     * @param serviceApiEndpoint service endpoint where to submit the request.
     * @param payload data content for this request.
     * @return validation request response from the registry service, or a guaranteed default response reflecting what
     * could have happened via its HTTP Status code and error message fields.
     */
    private ServiceResponse<ServiceResponseRegisterPrefixPayload> requestValidation(String serviceApiEndpoint, ServiceRequestRegisterPrefixPayload payload) {
        // TODO The new registration API integrated in the HQ Registry API Service no longer offers this.
        var response = new ServiceResponse<ServiceResponseRegisterPrefixPayload>();
        logger.info("Requesting validation at '{}'", serviceApiEndpoint);
        RequestEntity<ServiceRequest<ServiceRequestRegisterPrefixPayload>> requestEntity =
                prepareEntityRequest(ServiceRequest.of(payload),
                        serviceApiEndpoint);
        try {
            ResponseEntity<ServiceResponse<ServiceResponseRegisterPrefixPayload>> responseEntity = retryTemplate.execute(retryContext -> {
                if (requestEntity != null) {
                    return doValidateRequest(requestEntity);
                }
                var errorResponse = new ServiceResponse<ServiceResponseRegisterPrefixPayload>();
                errorResponse.setHttpStatus(HttpStatus.BAD_REQUEST)
                        .setErrorMessage(String.format("INVALID URI %s", serviceApiEndpoint));
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            });
            response = responseEntity.getBody();
            if (response != null) {
                response.setHttpStatus(responseEntity.getStatusCode());
            }
           if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                String errorMessage = String.format("VALIDATION ERROR " +
                                "at '%s', " +
                                "HTTP status code '%d', " +
                                "explanation '%s'",
                        serviceApiEndpoint,
                        responseEntity.getStatusCode().value(),
                        responseEntity.getBody().getErrorMessage());
                logger.error(errorMessage);
            }
        } catch (RuntimeException e) {
            String errorMessage = String.format("ERROR while requesting validation at '%s', because of '%s'",
                    serviceApiEndpoint, e.getMessage());
            logger.error(errorMessage);
            return ServiceResponse.ofError(HttpStatus.BAD_REQUEST, errorMessage);
        }
        return response;
    }

    // --- API ---
    /**
     * Registry Service API: request prefix registration for the given prefix registration information.
     * @param registrationPayload prefix registration information.
     * @return prefix registration request response or a guaranteed default response, for this context, where HTTP
     * Status code and error message fields have information on what could have happened.
     */
    public ServiceResponse<ServiceResponseRegisterPrefixPayload> requestPrefixRegistration(ServiceRequestRegisterPrefixPayload registrationPayload) {
        String serviceApiEndpoint = String.format("%s/%s", getServiceApiEndpointBaseline(), "registerPrefix");
        var response = new ServiceResponse<ServiceResponseRegisterPrefixPayload>();
        logger.info("Requesting prefix '{}' registration at '{}'", registrationPayload.getRequestedPrefix(),
                serviceApiEndpoint);
        RequestEntity<ServiceRequest<ServiceRequestRegisterPrefixPayload>> requestEntity =
                prepareEntityRequest(ServiceRequest.of(registrationPayload),
                        serviceApiEndpoint);
        try {
            ResponseEntity<ServiceResponse<ServiceResponseRegisterPrefixPayload>> responseEntity = retryTemplate.execute(retryContext -> {
                if (requestEntity != null) {
                    return doRegisterPrefixRequest(requestEntity);
                }
                var errorResponse = new ServiceResponse<ServiceResponseRegisterPrefixPayload>();
                errorResponse.setHttpStatus(HttpStatus.BAD_REQUEST)
                        .setErrorMessage(String.format("INVALID URI %s", serviceApiEndpoint));
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            });
            response = responseEntity.getBody();
            if (response != null) {
                response.setHttpStatus(responseEntity.getStatusCode());
            }
           if (HttpStatus.valueOf(responseEntity.getStatusCode().value()) != HttpStatus.OK) {
                String errorMessage = String.format("ERROR registering your prefix " +
                                "at '%s', " +
                                "HTTP status code '%d', " +
                                "explanation '%s'",
                        serviceApiEndpoint,
                        responseEntity.getStatusCode().value(),
                        responseEntity.getBody().getErrorMessage());
                logger.error(errorMessage);
            }
        } catch (RuntimeException e) {
            String errorMessage = String.format("ERROR while registering prefix '%s' at '%s', because of '%s'",
                    registrationPayload.getRequestedPrefix(), serviceApiEndpoint, e.getMessage());
            logger.error(errorMessage);
            return ServiceResponse.ofError(HttpStatus.BAD_REQUEST, errorMessage);
        }
        return response;
    }

    /**
     * Registry Service API: validate the namespace name.
     * This will not validate the whole prefix registration request, only namespace name.
     * @param payload a prefix registration payload that contains a value for the field that is being validated.
     * @return validation request response for the prefix registration field that is being validated, or a guaranteed
     * default response, for this context, where HTTP Status code and error message fields have information on what
     * could have happened.
     */
    public ServiceResponse<ServiceResponseRegisterPrefixPayload> validateName(ServiceRequestRegisterPrefixPayload payload) {
        return requestValidation(String.format("%s/%s", getServiceApiEndpointBaseline(), "validateName"), payload);
    }

    /**
     * Registry Service API: validate the namespace description
     * This will not validate the whole prefix registration request, only the namespace description.
     * @param payload a prefix registration payload that contains a value for the field that is being validated.
     * @return validation request response for the prefix registration field that is being validated, or a guaranteed
     * default response, for this context, where HTTP Status code and error message fields have information on what
     * could have happened.
     */
    public ServiceResponse<ServiceResponseRegisterPrefixPayload> validateDescription(ServiceRequestRegisterPrefixPayload payload) {
        return requestValidation(String.format("%s/%s", getServiceApiEndpointBaseline(), "validateDescription"), payload);
    }

    /**
     * Registry Service API: validate the given provider home URL
     * This will not validate the whole prefix registration request, only the given provider home URL.
     * @param payload a prefix registration payload that contains a value for the field that is being validated.
     * @return validation request response for the prefix registration field that is being validated, or a guaranteed
     * default response, for this context, where HTTP Status code and error message fields have information on what
     * could have happened.
     */
    public ServiceResponse<ServiceResponseRegisterPrefixPayload> validateProviderHomeUrl(ServiceRequestRegisterPrefixPayload payload) {
        return requestValidation(String.format("%s/%s", getServiceApiEndpointBaseline(), "validateProviderHomeUrl"), payload);
    }

    /**
     * Registry Service API: validate the given provider name.
     * This will not validate the whole prefix registration request, only the given provider name.
     * @param payload a prefix registration payload that contains a value for the field that is being validated.
     * @return validation request response for the prefix registration field that is being validated, or a guaranteed
     * default response, for this context, where HTTP Status code and error message fields have information on what
     * could have happened.
     */
    public ServiceResponse<ServiceResponseRegisterPrefixPayload> validateProviderName(ServiceRequestRegisterPrefixPayload payload) {
        return requestValidation(String.format("%s/%s", getServiceApiEndpointBaseline(), "validateProviderName"), payload);
    }

    /**
     * Registry Service API: validate the given provider description.
     * This will not validate the whole prefix registration request, only the given provider description.
     * @param payload a prefix registration payload that contains a value for the field that is being validated.
     * @return validation request response for the prefix registration field that is being validated, or a guaranteed
     * default response, for this context, where HTTP Status code and error message fields have information on what
     * could have happened.
     */
    public ServiceResponse<ServiceResponseRegisterPrefixPayload> validateProviderDescription(ServiceRequestRegisterPrefixPayload payload) {
        return requestValidation(String.format("%s/%s", getServiceApiEndpointBaseline(), "validateProviderDescription"), payload);
    }

    /**
     * Registry Service API: validate the given provider location.
     * This will not validate the whole prefix registration request, only the given provider location.
     * @param payload a prefix registration payload that contains a value for the field that is being validated.
     * @return validation request response for the prefix registration field that is being validated, or a guaranteed
     * default response, for this context, where HTTP Status code and error message fields have information on what
     * could have happened.
     */
    public ServiceResponse<ServiceResponseRegisterPrefixPayload> validateProviderLocation(ServiceRequestRegisterPrefixPayload payload) {
        return requestValidation(String.format("%s/%s", getServiceApiEndpointBaseline(), "validateProviderLocation"), payload);
    }

    /**
     * Registry Service API: validate the given provider code.
     * This will not validate the whole prefix registration request, only the given provider code.
     * @param payload a prefix registration payload that contains a value for the field that is being validated.
     * @return validation request response for the prefix registration field that is being validated, or a guaranteed
     * default response, for this context, where HTTP Status code and error message fields have information on what
     * could have happened.
     */
    public ServiceResponse<ServiceResponseRegisterPrefixPayload> validateProviderCode(ServiceRequestRegisterPrefixPayload payload) {
        return requestValidation(String.format("%s/%s", getServiceApiEndpointBaseline(), "validateProviderCode"), payload);
    }

    /**
     * Registry Service API: validate the given provider URL pattern.
     * This will not validate the whole prefix registration request, only the given provider URL pattern.
     * @param payload a prefix registration payload that contains a value for the field that is being validated.
     * @return validation request response for the prefix registration field that is being validated, or a guaranteed
     * default response, for this context, where HTTP Status code and error message fields have information on what
     * could have happened.
     */
    public ServiceResponse<ServiceResponseRegisterPrefixPayload> validateProviderUrlPattern(ServiceRequestRegisterPrefixPayload payload) {
        return requestValidation(String.format("%s/%s", getServiceApiEndpointBaseline(), "validateProviderUrlPattern"), payload);
    }

    /**
     * Registry Service API: validate the provided institution name
     * This will not validate the whole prefix registration request, only the given institution name.
     * @param payload a prefix registration payload that contains a value for the field that is being validated.
     * @return validation request response for the prefix registration field that is being validated, or a guaranteed
     * default response, for this context, where HTTP Status code and error message fields have information on what
     * could have happened.
     */
    public ServiceResponse<ServiceResponseRegisterPrefixPayload> validateInstitutionName(ServiceRequestRegisterPrefixPayload payload) {
        return requestValidation(String.format("%s/%s", getServiceApiEndpointBaseline(), "validateInstitutionName"), payload);
    }

    /**
     * Registry Service API: validate institution home URL.
     * This will not validate the whole prefix registration request, only the given institution home URL.
     * @param payload a prefix registration payload that contains a value for the field that is being validated.
     * @return validation request response for the prefix registration field that is being validated, or a guaranteed
     * default response, for this context, where HTTP Status code and error message fields have information on what
     * could have happened.
     */
    public ServiceResponse<ServiceResponseRegisterPrefixPayload> validateInstitutionHomeUrl(ServiceRequestRegisterPrefixPayload payload) {
        return requestValidation(String.format("%s/%s", getServiceApiEndpointBaseline(), "validateInstitutionHomeUrl"), payload);
    }

    /**
     * Registry Service API: validate institution description.
     * This will not validate the whole prefix registration request, only the given institution description.
     * @param payload a prefix registration payload that contains a value for the field that is being validated.
     * @return validation request response for the prefix registration field that is being validated, or a guaranteed
     * default response, for this context, where HTTP Status code and error message fields have information on what
     * could have happened.
     */
    public ServiceResponse<ServiceResponseRegisterPrefixPayload> validateInstitutionDescription(ServiceRequestRegisterPrefixPayload payload) {
        return requestValidation(String.format("%s/%s", getServiceApiEndpointBaseline(), "validateInstitutionDescription"), payload);
    }

    /**
     * Registry Service API: validate institution location.
     * This will not validate the whole prefix registration request, only the given institution location.
     * @param payload a prefix registration payload that contains a value for the field that is being validated.
     * @return validation request response for the prefix registration field that is being validated, or a guaranteed
     * default response, for this context, where HTTP Status code and error message fields have information on what
     * could have happened.
     */
    public ServiceResponse<ServiceResponseRegisterPrefixPayload> validateInstitutionLocation(ServiceRequestRegisterPrefixPayload payload) {
        return requestValidation(String.format("%s/%s", getServiceApiEndpointBaseline(), "validateInstitutionLocation"), payload);
    }

    /**
     * Registry Service API: validate the requested prefix.
     * This will not validate the whole prefix registration request, only the requested prefix.
     * @param payload a prefix registration payload that contains a value for the field that is being validated.
     * @return validation request response for the prefix registration field that is being validated, or a guaranteed
     * default response, for this context, where HTTP Status code and error message fields have information on what
     * could have happened.
     */
    public ServiceResponse<ServiceResponseRegisterPrefixPayload> validateRequestedPrefix(ServiceRequestRegisterPrefixPayload payload) {
        return requestValidation(String.format("%s/%s", getServiceApiEndpointBaseline(), "validateRequestedPrefix"), payload);
    }

    /**
     * Registry Service API: validate the given sample ID
     * This will not validate the whole prefix registration request, only the 'example identifier' field of the request.
     * @param payload a prefix registration payload that contains a value for the field that is being validated.
     * @return validation request response for the prefix registration field that is being validated, or a guaranteed
     * default response, for this context, where HTTP Status code and error message fields have information on what
     * could have happened.
     */
    public ServiceResponse<ServiceResponseRegisterPrefixPayload> validateSampleId(ServiceRequestRegisterPrefixPayload payload) {
        return requestValidation(String.format("%s/%s", getServiceApiEndpointBaseline(), "validateSampleId"), payload);
    }

    /**
     * Registry Service API: validate the regular expression that represents LUIs in the requested namespace
     * This will not validate the whole prefix registration request, only the given regular expression that represents
     * LUIs in the requested namespace.
     * @param payload a prefix registration payload that contains a value for the field that is being validated.
     * @return validation request response for the prefix registration field that is being validated, or a guaranteed
     * default response, for this context, where HTTP Status code and error message fields have information on what
     * could have happened.
     */
    public ServiceResponse<ServiceResponseRegisterPrefixPayload> validateIdRegexPattern(ServiceRequestRegisterPrefixPayload payload) {
        return requestValidation(String.format("%s/%s", getServiceApiEndpointBaseline(), "validateIdRegexPattern"), payload);
    }

    /**
     * Registry Service API: validate 'references' field for a prefix registration request.
     * This will not validate the whole prefix registration request, only the 'references' field of the request.
     * @param payload a prefix registration payload that contains a value for the field that is being validated.
     * @return validation request response for the prefix registration field that is being validated, or a guaranteed
     * default response, for this context, where HTTP Status code and error message fields have information on what
     * could have happened.
     */
    public ServiceResponse<ServiceResponseRegisterPrefixPayload> validateReferences(ServiceRequestRegisterPrefixPayload payload) {
        return requestValidation(String.format("%s/%s", getServiceApiEndpointBaseline(), "validateReferences"), payload);
    }

    /**
     * Registry Service API: validate 'additional information' field for a prefix registration request.
     * This will not validate the whole prefix registration request, only the 'additional information' field of the request.
     * @param payload a prefix registration payload that contains a value for the field that is being validated.
     * @return validation request response for the prefix registration field that is being validated, or a guaranteed
     * default response, for this context, where HTTP Status code and error message fields have information on what
     * could have happened.
     */
    public ServiceResponse<ServiceResponseRegisterPrefixPayload> validateAdditionalInformation(ServiceRequestRegisterPrefixPayload payload) {
        return requestValidation(String.format("%s/%s", getServiceApiEndpointBaseline(), "validateAdditionalInformation"), payload);
    }

    /**
     * Registry Service API: validate 'requester' field for a prefix registration request.
     * This will not validate the whole prefix registration request, only the 'requester' field of the request.
     * @param payload a prefix registration payload that contains a value for the field that is being validated.
     * @return validation request response for the prefix registration field that is being validated, or a guaranteed
     * default response, for this context, where HTTP Status code and error message fields have information on what
     * could have happened.
     */
    public ServiceResponse<ServiceResponseRegisterPrefixPayload> validateRequester(ServiceRequestRegisterPrefixPayload payload) {
        return requestValidation(String.format("%s/%s", getServiceApiEndpointBaseline(), "validateRequester"), payload);
    }

    /**
     * Registry Service API: validate the requester name.
     * This will not validate the whole prefix registration request, only the requester name.
     * @param payload a prefix registration payload that contains a value for the field that is being validated.
     * @return validation request response for the prefix registration field that is being validated, or a guaranteed
     * default response, for this context, where HTTP Status code and error message fields have information on what
     * could have happened.
     */
    public ServiceResponse<ServiceResponseRegisterPrefixPayload> validateRequesterName(ServiceRequestRegisterPrefixPayload payload) {
        return requestValidation(String.format("%s/%s", getServiceApiEndpointBaseline(), "validateRequesterName"), payload);
    }

    /**
     * Registry Service API: validate the requester e-mail.
     * This will not validate the whole prefix registration request, only the requester e-mail address.
     * @param payload a prefix registration payload that contains a value for the field that is being validated.
     * @return validation request response for the prefix registration field that is being validated, or a guaranteed
     * default response, for this context, where HTTP Status code and error message fields have information on what
     * could have happened.
     */
    public ServiceResponse<ServiceResponseRegisterPrefixPayload> validateRequesterEmail(ServiceRequestRegisterPrefixPayload payload) {
        return requestValidation(String.format("%s/%s", getServiceApiEndpointBaseline(), "validateRequesterEmail"), payload);
    }
}
