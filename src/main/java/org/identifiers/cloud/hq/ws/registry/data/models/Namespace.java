package org.identifiers.cloud.hq.ws.registry.data.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.sql.Timestamp;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.data.models
 * Timestamp: 2018-10-11 11:57
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This entity models a Prefix (or namespace) in the registry
 */
@Document
public class Namespace {
    @Id private BigInteger id;

    @NotNull(message = "The prefix itself must be provided, otherwise the entry makes no sense")
    @Indexed(unique = true)
    private String prefix;

    @Indexed(unique = true)
    private String mirId;

    @Indexed
    private String name;

    private String pattern;

    private String description;

    private Timestamp created;

    private Timestamp modified;

    private boolean deprecated = false;

    private Timestamp deprecationDate;
}
