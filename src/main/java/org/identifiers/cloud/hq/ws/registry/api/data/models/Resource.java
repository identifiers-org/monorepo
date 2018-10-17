package org.identifiers.cloud.hq.ws.registry.api.data.models;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.data.models
 * Timestamp: 2018-10-17 12:04
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
public class Resource implements Serializable {
    private BigInteger id;
    private String mirId;
    private String accessUrl;
    private String info;
    private boolean official;
    // TODO This should be a provider code
    private String resourcePrefix;
    // TODO This should be Sample ID
    private String localId;
    // TODO This should be Resource Home URL
    private String resourceUrl;
    private String institution;
    private String location;
}
