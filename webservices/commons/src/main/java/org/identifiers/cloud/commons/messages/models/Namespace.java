package org.identifiers.cloud.commons.messages.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


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
    private Date           modified;
    // The resolver does not need deprecated namespaces
    private List<Resource> resources = new ArrayList<>();
    // A sample ID at namespace level
    private String         sampleId;
    // Whether this namespace has LUIs with embedded namespace prefix
    private boolean namespaceEmbeddedInLui;
    // This field flags whether the namespace has been deprecated or not
    private boolean deprecated;
    // Information on when this namespace was deprecated
    private Date deprecationDate;
    private Date deprecationOfflineDate; // Approximation of when date was made unavailable
    private boolean renderDeprecatedLanding;
    private String deprecationStatement;
    private String infoOnPostmortemAccess;
    private Namespace successor; // Namespace that should be used in case of deprecation
}
