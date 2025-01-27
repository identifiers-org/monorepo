package org.identifiers.cloud.hq.ws.registry.models.validators.payload;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefixPayload;
import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterResourcePayload;
import org.identifiers.cloud.hq.ws.registry.data.models.Namespace;
import org.identifiers.cloud.hq.ws.registry.data.models.Resource;
import org.identifiers.cloud.hq.ws.registry.data.services.NamespaceService;
import org.identifiers.cloud.hq.ws.registry.models.validators.RegistrationPayloadValidator;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class ProviderCodeValueValidator extends RegistrationPayloadValidator {
    final NamespaceService namespaceService;

    @Override
    public Optional<String> validate(ServiceRequestRegisterPrefixPayload request, String valueLabel) {
        return checkAgainstExistingPrefixes(request.getProviderCode());
    }

    @Override
    public Optional<String> validate(ServiceRequestRegisterResourcePayload request, String valueLabel) {
        final String requestedProviderCode = request.getProviderCode();
        Optional<String> error = checkAgainstExistingPrefixes(requestedProviderCode);
        if (error.isPresent()) return error;

        // Provider codes must be unique within the namespace to identify specific resource to resolve to
        String prefix = request.getNamespacePrefix();
        if (StringUtils.isNotBlank(prefix)) {
            Namespace requestedNamespace = namespaceService.getNamespaceByPrefix(prefix);
            if (requestedNamespace != null) {
                var existingProviderCodes = requestedNamespace.getResources()
                                                              .stream()
                                                              .map(Resource::getProviderCode)
                                                              .filter(StringUtils::isNotBlank)
                                                              .toList();
                if (existingProviderCodes.stream().anyMatch(requestedProviderCode::equalsIgnoreCase)) {
                   return Optional.of("Provider codes must be UNIQUE within a namespace! Please pick another code.");
                }
            } else {
                return Optional.of("Namespace with prefix '" + prefix + "' does not exist");
            }
        }
        return Optional.empty();
    }

    /**
     * Provider codes must not be equal to any namespace
     * @return Error string if a namespace exists with prefix equal to this provider code
     */
    private Optional<String> checkAgainstExistingPrefixes(String providerCode) {
        boolean namespaceWithProviderCodePrefixExists = namespaceService.checkIfNamespaceExistsByPrefix(providerCode);
        if (namespaceWithProviderCodePrefixExists) {
            var msg = String.format("Provider codes must be DIFFERENT from existing prefixes! Namespace '%s' exists.", providerCode);
            return Optional.of(msg);
        }
        return Optional.empty();
    }
}
