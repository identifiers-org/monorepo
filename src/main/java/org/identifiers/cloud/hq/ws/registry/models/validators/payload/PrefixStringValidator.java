package org.identifiers.cloud.hq.ws.registry.models.validators.payload;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefixPayload;
import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterResourcePayload;
import org.identifiers.cloud.hq.ws.registry.data.models.Namespace;
import org.identifiers.cloud.hq.ws.registry.data.services.NamespaceService;
import org.identifiers.cloud.hq.ws.registry.models.validators.RegistrationPayloadValidator;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class PrefixStringValidator extends RegistrationPayloadValidator {
    final Predicate<String> validPrefixPredicate = Pattern.compile("^[a-z0-9_.]+$")
                                                          .asPredicate();
    final NamespaceService namespaceService;


    @Override
    public Optional<String> validate(ServiceRequestRegisterResourcePayload request, String valueLabel) {
        String prefix = request.getNamespacePrefix();
        Namespace foundNamespace = namespaceService.getNamespaceByPrefix(prefix);
        if (foundNamespace == null) {
            return Optional.of(String.format("Prefix '%s' IS NOT REGISTERED", prefix));
        } else if (foundNamespace.isDeprecated()) {
            return Optional.of(String.format("Prefix '%s' is DEPRECATED, new resources are not allowed", prefix));
        }
        return Optional.empty();
    }


    @Override
    public Optional<String> validate(ServiceRequestRegisterPrefixPayload request, String valueLabel) {
        String prefix = request.getRequestedPrefix();
        if (!validPrefixPredicate.test(prefix)) {
            return Optional.of("Requested prefix can only contain lowercase characters, numbers, underscores and dots");
        }

        Namespace foundNamespace = namespaceService.getNamespaceByPrefix(prefix);
        if (foundNamespace != null) {
            return Optional.of(String.format("Prefix '%s' already exists", prefix));
        }
        return Optional.empty();
    }
}
