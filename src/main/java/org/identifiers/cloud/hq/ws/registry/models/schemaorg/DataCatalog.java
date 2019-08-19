package org.identifiers.cloud.hq.ws.registry.models.schemaorg;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DataCatalog extends SchemaOrgNode implements Serializable {
    // Node Attributes
    private String name;
    private String description;
    private String url;
    private String keywords;
    private Organization provider;
    private CreativeWork license;
    private List<PublicationEvent> publication = new ArrayList<>();
    private List<String> alternateName = new ArrayList<>();
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Dataset dataset;

    // Set the node type
    @Override
    public String delegateGetNodeType() {
        return "DataCatalog";
    }
}
