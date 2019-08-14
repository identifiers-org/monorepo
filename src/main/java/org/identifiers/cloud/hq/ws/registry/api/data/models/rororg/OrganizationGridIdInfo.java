package org.identifiers.cloud.hq.ws.registry.api.data.models.rororg;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.data.models.rororg
 * Timestamp: 2019-08-14 17:29
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * ROR data model for GRID link from an organization
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrganizationGridIdInfo {
    private String preferred;
    private String all;
}
