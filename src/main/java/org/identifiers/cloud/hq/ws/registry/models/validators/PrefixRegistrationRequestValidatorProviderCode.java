package org.identifiers.cloud.hq.ws.registry.models.validators;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefixPayload;
import org.identifiers.cloud.hq.ws.registry.data.models.Namespace;
import org.identifiers.cloud.hq.ws.registry.data.models.Resource;
import org.identifiers.cloud.hq.ws.registry.data.services.NamespaceService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models.validators
 * Timestamp: 2019-03-18 11:57
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
// TODO I don't think it needs to be of "prototype" scope
@Component
@Scope("prototype")
@RequiredArgsConstructor
@Qualifier("PrefixRegistrationRequestValidatorProviderCode")
public class PrefixRegistrationRequestValidatorProviderCode implements PrefixRegistrationRequestValidator {
    final NamespaceService namespaceService;

    @Override
    public boolean validate(ServiceRequestRegisterPrefixPayload request) throws PrefixRegistrationRequestValidatorException {
        // Provider code must be provided
        String providerCode = request.getProviderCode();
        if (StringUtils.isBlank(providerCode)) {
            // TODO In future iterations, use a different mechanism for reporting back why this is not valid, and leave exceptions for non-recoverable conditions
            throw new PrefixRegistrationRequestValidatorException("Provider Code is REQUIRED, but it's missing");
        }

        // Provider codes must be different from existing prefixes, otherwise resolution for the provider code may not work.
        boolean namespaceWithProviderCodePrefixExists = namespaceService.checkIfNamespaceExistsByPrefix(providerCode);
        if (namespaceWithProviderCodePrefixExists) {
            var msg = String.format("Provider codes must be DIFFERENT from existing prefixes! Namespace %s exists.", providerCode);
            throw new PrefixRegistrationRequestValidatorException(msg);
        }

        // Provider codes must be unique within the namespace to identify specific resource to resolve to
            if (StringUtils.isNotBlank(request.getRequestedPrefix())) {
            Namespace requestedNamespace = namespaceService.getNamespaceByPrefix(request.getRequestedPrefix());
            if (requestedNamespace != null) { // null for prefix requests
                var existingProviderCodes = requestedNamespace.getResources()
                        .stream()
                        .map(Resource::getProviderCode)
                        .filter(StringUtils::isNotBlank)
                        .toList();
                if (existingProviderCodes.stream().anyMatch(providerCode::equalsIgnoreCase)) {
                    throw new PrefixRegistrationRequestValidatorException("Provider codes must be UNIQUE within a namespace! Please pick another code.");
                }
            }
        }
        return true;
    }
}
