package org.identifiers.cloud.ws.resolver.data.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: resolver
 * Package: org.identifiers.org.cloud.ws.resolver.models
 * Timestamp: 2018-01-17 15:20
 * ---
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@RedisHash("ResolverNamespaces")
public class Namespace implements Serializable {
    // identifiers.org internal ID for this namespace
    @Id
    private long id;
    // This is the prefix itself, i.e. what is actually used in the compact identifier.
    @Indexed
    private String prefix;
    // MIR ID associated with this namespace
    @Indexed
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
    // WARNING - SPECIAL CASE
    // This flag tells the resolver whether LUIs in this namespace have the namespace embedded in the LUI, as they will
    // have to be treated differently depending on the different resolver domain operations
    private boolean namespaceEmbeddedInLui = false;
}
