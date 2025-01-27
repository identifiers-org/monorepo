package org.identifiers.cloud.ws.resolver.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.identifiers.cloud.ws.resolver.models.ResolvedResource;

import java.util.ArrayList;
import java.util.List;

/**
 * Project: resolver
 * Package: org.identifiers.cloud.ws.resolver.services
 * Timestamp: 2019-06-26 15:09
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 * <p>
 * This model represents the result from a resolution service
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
public class ResolutionServiceResult {
    private boolean resolved = false;
    private String errorMessage = "--- NO ERROR SET ---";
    private List<ResolvedResource> resolvedResources = new ArrayList<>();
}
