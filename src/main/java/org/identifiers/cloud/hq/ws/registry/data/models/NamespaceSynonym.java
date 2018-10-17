package org.identifiers.cloud.hq.ws.registry.data.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.data.models
 * Timestamp: 2018-10-11 12:08
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * Data model for namespace synonyms
 */
@Document
public class NamespaceSynonym {
    @Id private String synonym;
    private BigInteger namespaceFk;
    @Transient private Namespace namespace;

    
}
