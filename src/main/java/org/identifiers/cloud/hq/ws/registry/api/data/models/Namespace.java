package org.identifiers.cloud.hq.ws.registry.api.data.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
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
// TODO Refactor with lombok
public class Namespace implements Serializable {
    private long id;
    private String prefix;
    private String mirId;
    private String name;
    private String pattern;
    private String description;
    private Timestamp created;
    private Timestamp modified;
    private boolean deprecated = false;
    private Timestamp deprecationDate;
    private List<Resource> resources = new ArrayList<>();
}
