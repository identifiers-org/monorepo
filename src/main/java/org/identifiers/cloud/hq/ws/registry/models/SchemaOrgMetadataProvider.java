package org.identifiers.cloud.hq.ws.registry.models;

import org.identifiers.cloud.hq.ws.registry.models.schemaorg.SchemaOrgNode;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models
 * Timestamp: 2019-08-19 10:23
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This contract is met by any provider of Schema.org metadata from the registry data
 */
public interface SchemaOrgMetadataProvider {
    // TODO

    SchemaOrgNode getForPlatform() throws SchemaOrgMetadataProviderException;
}
