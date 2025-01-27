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

    /**
     * Get Schema.org metadata describing the platform
     * @return root node of the Schema.org metadata tree
     * @throws SchemaOrgMetadataProviderException
     */
    SchemaOrgNode getForPlatform() throws SchemaOrgMetadataProviderException;

    /**
     * Get Schema.org metadata for the platform, including information about a particular namespace
     * @param namespaceId namespace ID (internal)
     * @return root node of the Schema.org metadata tree
     * @throws SchemaOrgMetadataProviderException
     */
    SchemaOrgNode getForNamespace(long namespaceId) throws SchemaOrgMetadataProviderException;

    /**
     * Get Schema.org metadata for the platform, including information about a particular namespace
     * @param namespacePrefix namespace prefix
     * @return root node of the Schema.org metadata tree
     * @throws SchemaOrgMetadataProviderException
     */
    SchemaOrgNode getForNamespacePrefix(String namespacePrefix) throws SchemaOrgMetadataProviderException;
}
