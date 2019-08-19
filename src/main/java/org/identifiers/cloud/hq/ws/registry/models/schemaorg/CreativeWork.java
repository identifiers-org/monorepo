package org.identifiers.cloud.hq.ws.registry.models.schemaorg;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models.schemaorg
 * Timestamp: 2019-08-19 09:46
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CreativeWork extends SchemaOrgNode implements Serializable {
    // Node Attributes
    private String name;
    private String url;

    // Set the node type
    @Override
    public String delegateGetNodeType() {
        return "CreativeWork";
    }
}
