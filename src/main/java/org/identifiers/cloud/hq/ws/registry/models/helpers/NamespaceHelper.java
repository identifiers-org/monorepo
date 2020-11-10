package org.identifiers.cloud.hq.ws.registry.models.helpers;

import org.identifiers.cloud.hq.ws.registry.data.models.Namespace;

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
public class NamespaceHelper {

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
}
