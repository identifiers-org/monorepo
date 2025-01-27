package org.identifiers.cloud.hq.ws.registry.models.schemaorg;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models.schemaorg
 * Timestamp: 2019-08-17 07:49
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Data
@AllArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true, value = {"SCHEMAORG_CONTEXT", "rootNode"})
public abstract class SchemaOrgNode implements Serializable {
    // Schema.org context for the root node
    public static final String SCHEMAORG_CONTEXT = "http://schema.org";

    private boolean rootNode = false;

    // Include this one if, and only if, it is not null
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("@context")
    private String context;
    @JsonProperty("@type")
    private String type;

    // Helpers
    public void makeItRootNode() {
        rootNode = true;
        context = SCHEMAORG_CONTEXT;
    }

    // Delegates
    public String delegateGetNodeType() {
        return null;
    }

    // Constructor
    public SchemaOrgNode() {
        context = null;
        type = delegateGetNodeType();
    }
}
