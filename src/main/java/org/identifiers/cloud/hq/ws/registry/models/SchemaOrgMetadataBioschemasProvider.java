package org.identifiers.cloud.hq.ws.registry.models;

import org.identifiers.cloud.hq.ws.registry.data.models.Namespace;
import org.identifiers.cloud.hq.ws.registry.data.repositories.NamespaceRepository;
import org.identifiers.cloud.hq.ws.registry.models.schemaorg.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
    // Repositories
    @Autowired
    private NamespaceRepository namespaceRepository;

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
        return publicationEvents;
    }

    private CreativeWork getPlatformLicense() {
        CreativeWork license = new CreativeWork();
        license.setName("Creative Commons CC4 Attribution");
        license.setUrl("https://creativecommons.org/licenses/by/4.0/");
        return license;
    }

    private Organization getPlatformProvider() {
        // Create the contact point
        ContactPoint contactPoint = new ContactPoint();
        contactPoint.setContactType("customer support");
        contactPoint.setEmail("identifiers-org@ebi.ac.uk");
        contactPoint.setUrl("https://github.com/identifiers-org/identifiers-org.github.io/issues/new");
        // Create the Organization
        Organization organization = new Organization();
        organization.setName("The European Bioinformatics Institute (EMBL-EBI)");
        organization.setUrl("http://www.ebi.ac.uk/");
        // Set the organization contact point
        organization.setContactPoint(contactPoint);
        return organization;
    }

    private DataCatalog startPlatformMetadataTree() {
        DataCatalog dataCatalog = new DataCatalog();
        dataCatalog.makeItRootNode();
        dataCatalog.setName("Identifiers.org");
        dataCatalog.setDescription("The Identifiers.org registry contains registered namespace and provider prefixes with associated access URIs for a large number of high quality data collections. These prefixes are used in web resolution of compact identifiers of the form PREFIX:ACCESSION commonly used to specify bioinformatics and other data resources.");
        dataCatalog.setUrl("https://identifiers.org/");
        dataCatalog.setKeywords("registry,life sciences,compact identifier");
        dataCatalog.setAlternateName(Arrays.asList("Identifiers.org Central Registry"));
        return dataCatalog;
    }

    private Dataset getDatasetFromNamespace(Namespace namespace) {
        // TODO Refactor some things here in the future, if we ever need to extend this
        // Some values
        String idorgNamespaceUrl = String.format("https://registry.identifiers.org/registry/%s#!", namespace.getPrefix());
        String keywords = String.format("registry,life sciences,compact identifier, %s", namespace.getPrefix());
        // Build the Dataset node
        Dataset dataset = new Dataset();
        dataset.setName(namespace.getName());
        dataset.setDescription(namespace.getDescription());
        dataset.setUrl(idorgNamespaceUrl);
        dataset.setKeywords(keywords);
        dataset.setIdentifier(idorgNamespaceUrl);
        return dataset;
    }

    @Override
    public SchemaOrgNode getForPlatform() throws SchemaOrgMetadataProviderException {
        // Somewhat of a builder
        DataCatalog dataCatalog = startPlatformMetadataTree();
        dataCatalog.setProvider(getPlatformProvider());
        dataCatalog.setLicense(getPlatformLicense());
        dataCatalog.setPublication(getPlatformPublicationEvents());
        return dataCatalog;
    }

    @Override
    public SchemaOrgNode getForNamespace(long namespaceId) throws SchemaOrgMetadataProviderException {
        Optional<Namespace> namespace = namespaceRepository.findById(namespaceId);
        if (!namespace.isPresent()) {
            String errorMessage = String.format("Schema.org metadata for namespace with ID '%d' COULD NOT BE PROVIDED, the namespace DOES NOT exist", namespaceId);
            throw new SchemaOrgMetadataProviderException(errorMessage);
        }
        return getForPlatform().setDataset(getDatasetFromNamespace(namespace.get()));
    }
}
