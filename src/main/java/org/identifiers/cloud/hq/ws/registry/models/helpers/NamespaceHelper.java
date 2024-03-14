package org.identifiers.cloud.hq.ws.registry.models.helpers;

import org.identifiers.cloud.hq.ws.registry.data.models.Namespace;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Project: idorg-hq-registry
 * Package: org.identifiers.cloud.hq.ws.registry.models.helpers
 * Timestamp: 2020-11-10 13:15
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * Helper for common tasks around namespaces
 */
@Component
public class NamespaceHelper {
    @Value("${org.identifiers.cloud.hq.ws.registry.fairapi.interoperability.baseurl}")
    private String interoperabilityBaseUrl;
    /**
     * Validate a LUI against a given Namespace.
     * @param namespace that will define the context for validation
     * @param lui LUI to validate
     * @return true if the LUI is valid, false otherwise
     */
    public static boolean validateLui(Namespace namespace, String lui) {
        Pattern pattern = Pattern.compile(namespace.getPattern());
        Matcher matcher = pattern.matcher(lui);
        return matcher.matches();
    }

    /**
     * Given a namespace and a LUI, get the corresponding compact identifier
     * @param namespace a namespace
     * @param lui a LUI within the context of the namespace
     * @return the correponding compact identifier, or null if any of them is not valid
     */
    public static String getCompactIdentifier(Namespace namespace, String lui) {
        if (!validateLui(namespace, lui)) {
            return null;
        }
        if (namespace.isNamespaceEmbeddedInLui()) {
            return lui;
        }
        return String.format("%s:%s", namespace.getPrefix(), lui);
    }

    /**
     * Get the Identifiers.org URL given a namespace and a LUI
     * @param namespace namespace for ID context
     * @param lui LUI as defined by the context
     * @return The Identifiers.org URL, a.k.a. Interoperability URL, null if the LUI is not valid
     */
    public String getInteroperabilityUrl(Namespace namespace, String lui) {
        String compactIdentifier = getCompactIdentifier(namespace, lui);
        if (compactIdentifier != null) {
            return String.format("%s/%s", interoperabilityBaseUrl, compactIdentifier);
        }
        return null;
    }
}
