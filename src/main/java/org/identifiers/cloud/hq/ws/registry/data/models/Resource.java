package org.identifiers.cloud.hq.ws.registry.data.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.data.models
 * Timestamp: 2018-10-11 11:40
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This is a data model for a Resource (Provider) in the registry.
 */
@Document
public class Resource {
    @Id private BigInteger id;

    @Indexed(unique = true)
    private String mirId;

    private String accessUrl;

    private String info;

    @Indexed
    private boolean official;

    // TODO This should be a provider code
    @Indexed
    private String resourcePrefix;

    // TODO This should be Sample ID
    private String localId;

    // TODO This should be Resource Home URL
    private String resourceUrl;

    @DBRef private Institution institution;

    @DBRef private Location location;

    @DBRef private Namespace namespace;
}
