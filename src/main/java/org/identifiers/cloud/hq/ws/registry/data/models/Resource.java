package org.identifiers.cloud.hq.ws.registry.data.models;

import org.springframework.data.annotation.Id;
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
    
}
