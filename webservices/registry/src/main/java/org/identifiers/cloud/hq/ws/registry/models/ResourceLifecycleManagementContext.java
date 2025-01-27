package org.identifiers.cloud.hq.ws.registry.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models
 * Timestamp: 2019-08-01 05:39
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ResourceLifecycleManagementContext {
    // Base class for the information needed by any final resource lifecycle management service strategy, when it comes
    // to execute its operations

    // This is the URL pattern that will be set upon re-activation of a given resource
    String resourceReactivationUrlPattern;
    // NOTE, the minimum amount of information needed for reactivating a resource is its URL pattern, in case other
    // details like 'institution', 'person' or 'location' have changed, this should be updated via the resource editing
    // API.
}
