package org.identifiers.cloud.hq.validatorregistry.helpers;

import org.identifiers.cloud.commons.messages.models.Institution;
import org.identifiers.cloud.commons.messages.models.Namespace;
import org.identifiers.cloud.commons.messages.models.Resource;

public class TargetEntityHelper {
    public static String getEntityTypeOf(Object entity) {
        return entity.getClass().getSimpleName().toLowerCase();
    }

    public static long getEntityIdOf(Object entity) {
        if (entity instanceof Institution institution) {
            return institution.getId();
        } else if (entity instanceof Resource resource) {
            return resource.getId();
        } else if (entity instanceof Namespace namespace) {
            return namespace.getId();
        } else {
            throw new IllegalArgumentException("Unsupported entity type: " + entity.getClass().getName());
        }
    }
}
