package org.identifiers.cloud.hq.ws.registry.api.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.models
 * Timestamp: 2018-10-10 14:00
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Document
public class Institution {
    @Id private BigInteger id;
    private String name;
    private String description;
}
