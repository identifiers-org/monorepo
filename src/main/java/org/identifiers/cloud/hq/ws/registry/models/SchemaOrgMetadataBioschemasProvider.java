package org.identifiers.cloud.hq.ws.registry.models;

import org.identifiers.cloud.hq.ws.registry.models.schemaorg.PublicationEvent;
import org.identifiers.cloud.hq.ws.registry.models.schemaorg.SchemaOrgNode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models
 * Timestamp: 2019-08-19 10:43
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Component
public class SchemaOrgMetadataBioschemasProvider implements SchemaOrgMetadataProvider {
    // Helpers
    private List<PublicationEvent> getPlatformPublicationEvents() {
        List<PublicationEvent> publicationEvents = new ArrayList<>();
        PublicationEvent publicationEvent = new PublicationEvent();
        publicationEvent.setName("Uniform Resolution of Compact Identifiers for Biomedical Data");
        publicationEvent.setUrl("http://biorxiv.org/content/early/2017/01/18/101279");
        publicationEvents.add(publicationEvent);
        publicationEvent = new PublicationEvent();
        publicationEvent.setName("Identifiers.org and MIRIAM Registry: community resources to provide persistent identification.");
        publicationEvent.setUrl("https://identifiers.org/pubmed:22140103");
        publicationEvents.add(publicationEvent);
    }

    @Override
    public SchemaOrgNode getForPlatform() throws SchemaOrgMetadataProviderException {
        // Somewhat of a builder
        return null;
    }

    @Override
    public SchemaOrgNode getForNamespace(long namespaceId) throws SchemaOrgMetadataProviderException {
        return null;
    }
}
