package org.identifiers.cloud.hq.ws.registry.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.identifiers.cloud.hq.ws.registry.data.models.Resource;

import java.util.ArrayList;
import java.util.List;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models
 * Timestamp: 2019-08-22 11:23
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class InstitutionLifecycleMangementOperationReport extends ActionReport {
    // Whether the institution is in the registry
    private boolean institutionFound = false;
    // Resources that are linked to the institution, if empty, it means the institution is not being used, otherwise, it is in used
    private List<Resource> resources = new ArrayList<>();
}
