package org.identifiers.cloud.hq.ws.registry.api.data.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.data.models
 * Timestamp: 2018-10-17 12:02
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This class models how the microservice exposes information about namespaces in the registry through its Resolution API.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
public class Namespace implements Serializable {
    // identifiers.org internal ID for this namespace
    private long id;
    // This is the prefix itself, i.e. what is actually used in the compact identifier.
    private String prefix;
    // MIR ID associated with this namespace
    private String mirId;
    // Name for this namespace, this part does not have anything to do with the prefix in the compact identifier
    private String name;
    // This is the regular expression that describes the IDs within the ID space scoped by this namespace
    private String pattern;
    // Some literal description of the namespace
    private String description;
    // Creation and modification dates for this namespace
    private Date created;
    private Date modified;
    // The resolver does not need deprecated namespaces
    private List<Resource> resources = new ArrayList<>();
    // A sample ID at namespace level
    private String sampleId;
}
