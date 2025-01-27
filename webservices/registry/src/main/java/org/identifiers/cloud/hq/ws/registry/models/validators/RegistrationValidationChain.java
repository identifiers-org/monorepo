package org.identifiers.cloud.hq.ws.registry.models.validators;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefixPayload;
import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterResourcePayload;
import org.springframework.lang.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Ordered list of validators to check whether a registration request is valid
 */
@Slf4j
public class RegistrationValidationChain {
    final String attributeLabel;
    final Function<ServiceRequestRegisterPrefixPayload, String> prefixValueGetter;
    final Function<ServiceRequestRegisterResourcePayload, String> resourceValueGetter;
    final Predicate<ServiceRequestRegisterPrefixPayload> prefixCondition;
    final Predicate<ServiceRequestRegisterResourcePayload> resourceCondition;
    final List<RegistrationValidationChainItem> validatorChain;


    /**
     * Constructor of validation chain. Getter should be null if validator chain should not be used for datatype.
     * @param prefixValueGetter getter of value from namespace registration request
     * @param prefixCondition condition to indicate that chain must validate namespace registration request
     * @param resourceValueGetter getter of value from resource registration request
     * @param resourceCondition condition to indicate that chain must validate resource registration request
     * @param validatorChain validators to use to verify request
     */
    public RegistrationValidationChain(String attributeLabel,
                                       @Nullable Function<ServiceRequestRegisterPrefixPayload, String> prefixValueGetter,
                                       @Nullable Predicate<ServiceRequestRegisterPrefixPayload> prefixCondition,
                                       @Nullable Function<ServiceRequestRegisterResourcePayload, String> resourceValueGetter,
                                       @Nullable Predicate<ServiceRequestRegisterResourcePayload> resourceCondition,
                                       RegistrationValidationChainItem... validatorChain) {
        this.attributeLabel = attributeLabel;
        this.prefixValueGetter = prefixValueGetter;
        this.resourceValueGetter = resourceValueGetter;
        this.prefixCondition = prefixCondition;
        this.resourceCondition = resourceCondition;
        this.validatorChain = Arrays.asList(validatorChain);
    }
    /**
     * Constructor of validation chain without condition, all requests are validated.
     * Getter should be null if validator chain should not be used for datatype.
     * @param prefixValueGetter getter of value from namespace registration request
     * @param resourceValueGetter getter of value from resource registration request
     * @param validatorChain validators to use to verify request
     */
    public RegistrationValidationChain(String attributeLabel,
                                       @Nullable Function<ServiceRequestRegisterPrefixPayload, String> prefixValueGetter,
                                       @Nullable Function<ServiceRequestRegisterResourcePayload, String> resourceValueGetter,
                                       RegistrationValidationChainItem... validatorChain) {
        this(attributeLabel, prefixValueGetter, null, resourceValueGetter, null, validatorChain);
    }


    /**
     * @return chain that accepts all requests
     */
    public static RegistrationValidationChain acceptAllChain() {
        return new RegistrationValidationChain(null, null, null);
    }

    /**
     * Validates an attribute identified by valueName on a given request
     * @param request to be validated
     * @return Error message of first validator that find request invalid for requested attribute, empty if valid
     */
    public Optional<String> validate(ServiceRequestRegisterPrefixPayload request) {
        if (prefixCondition != null && !prefixCondition.test(request)) {
            log.debug("Skipping chain validation of resource request due to condition");
            return Optional.empty();
        }

        if (validatorChain.isEmpty()) {
            // Empty chain should accept all requests
            return Optional.empty();
        }

        if (prefixValueGetter == null) {
            // Null getter means that the validator does not apply to prefixes
            return Optional.empty();
        }

        for (var validator : validatorChain) {
            var error = validator.validate(request, prefixValueGetter, attributeLabel);
            if (error.isPresent()) {
                log.debug(error.get());
                return error;
            }
        }
        return Optional.empty();
    }

    /**
     * Validates an attribute identified by valueName on a given request
     * @param request to be validated
     * @return Error message of first validator that find request invalid for requested attribute, empty if valid
     */
    public Optional<String> validate(ServiceRequestRegisterResourcePayload request) {
        if (resourceCondition != null && !resourceCondition.test(request)) {
            log.debug("Skipping chain validation of resource request due to condition");
            return Optional.empty();
        }

        if (validatorChain.isEmpty()) {
            // Empty chain should accept all requests
            return Optional.empty();
        }

        if (resourceValueGetter == null) {
            // Null getter means that the validator does not apply to resources
            return Optional.empty();
        }


        for (var validator : validatorChain) {
            var error = validator.validate(request, resourceValueGetter, attributeLabel);
            if (error.isPresent()) {
                log.debug(error.get());
                return error;
            }
        }
        return Optional.empty();
    }




}
