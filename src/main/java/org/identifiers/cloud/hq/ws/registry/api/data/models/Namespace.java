package org.identifiers.cloud.hq.ws.registry.api.data.models;

import org.identifiers.cloud.hq.ws.registry.data.models.Resource;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.data.models
 * Timestamp: 2018-10-17 12:02
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
public class Namespace implements Serializable {
    private BigInteger id;
    private String prefix;
    private String mirId;
    private String name;
    private String pattern;
    private String description;
    private Timestamp created;
    private Timestamp modified;
    private boolean deprecated = false;
    private Timestamp deprecationDate;
    private List<Resource> resources;
}
