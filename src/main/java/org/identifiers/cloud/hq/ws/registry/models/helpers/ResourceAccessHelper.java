package org.identifiers.cloud.hq.ws.registry.models.helpers;

import javax.validation.constraints.NotNull;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models.helpers
 * Timestamp: 2019-03-14 15:51
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This helper is part of the prefix registration API service, that has been integrated into the HQ registry.
 */
public class ResourceAccessHelper {
    public static final String RESOURCE_ACCESS_RULE_PLACEHOLDER_ID = "{$id}";

    public static String getResourceUrlFor(@NotNull String accessRule, @NotNull String id) {
        return accessRule.replace(RESOURCE_ACCESS_RULE_PLACEHOLDER_ID, id);
    }
}