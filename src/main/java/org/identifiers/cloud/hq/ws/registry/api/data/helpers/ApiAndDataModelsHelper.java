package org.identifiers.cloud.hq.ws.registry.api.data.helpers;

import org.identifiers.cloud.hq.ws.registry.api.data.models.Institution;
import org.identifiers.cloud.hq.ws.registry.api.data.models.Location;
import org.identifiers.cloud.hq.ws.registry.api.data.models.Namespace;
import org.identifiers.cloud.hq.ws.registry.api.data.models.Resource;
import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefixPayload;
import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterResourcePayload;
import org.identifiers.cloud.hq.ws.registry.data.models.PrefixRegistrationRequest;
import org.identifiers.cloud.hq.ws.registry.data.models.ResourceRegistrationRequest;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models.helpers
 * Timestamp: 2019-03-20 13:49
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This helper provides methods for transformations between api models and data models.
 */
public class ApiAndDataModelsHelper {
    public static PrefixRegistrationRequest getPrefixRegistrationRequest(ServiceRequestRegisterPrefixPayload sourceModel) {
        String references = "";
        if (sourceModel.getSupportingReferences() != null) {
            references = "".join(",", sourceModel.getSupportingReferences());
        }
        String additionalInformation = "--- No additional information provided ---";
        if (sourceModel.getAdditionalInformation() != null) {
            additionalInformation = sourceModel.getAdditionalInformation();
        }
        return new PrefixRegistrationRequest()
                .setName(sourceModel.getName())
                .setDescription(sourceModel.getDescription())
                .setProviderHomeUrl(sourceModel.getProviderHomeUrl())
                .setProviderName(sourceModel.getProviderName())
                .setProviderDescription(sourceModel.getProviderDescription())
                .setProviderLocation(sourceModel.getProviderLocation())
                .setProviderCode(sourceModel.getProviderCode())
                .setInstitutionName(sourceModel.getInstitutionName())
                .setInstitutionDescription(sourceModel.getInstitutionDescription())
                .setInstitutionLocation(sourceModel.getInstitutionLocation())
                .setInstitutionHomeUrl(sourceModel.getInstitutionHomeUrl())
                .setInstitutionRorId(sourceModel.getInstitutionRorId())
                .setRequestedPrefix(sourceModel.getRequestedPrefix())
                .setProviderUrlPattern(sourceModel.getProviderUrlPattern())
                .setSampleId(sourceModel.getSampleId())
                .setIdRegexPattern(sourceModel.getIdRegexPattern())
                .setSupportingReferences(references)
                .setAdditionalInformation(additionalInformation)
                .setNamespaceEmbeddedInLui(sourceModel.isNamespaceEmbeddedInLui())
                .setRequesterName(sourceModel.getRequester().getName())
                .setRequesterEmail(sourceModel.getRequester().getEmail())
                .setProtectedUrls(sourceModel.isProtectedUrls())
                .setRenderProtectedLanding(sourceModel.isRenderProtectedLanding())
                .setAuthHelpDescription(sourceModel.getAuthHelpDescription())
                .setAuthHelpUrl(sourceModel.getAuthHelpUrl());
    }

    // NOTE - I don't totally like to have two models with the same name, as it makes the coding more prone to
    // making mistakes, but, at the same time, this is the meaning of it, and that's why we have packages,
    // right?

    public static Location getLocationFrom(org.identifiers.cloud.hq.ws.registry.data.models.Location location) {
        return new Location()
                .setCountryCode(location.getCountryCode())
                .setCountryName(location.getCountryName());
    }

    public static Institution getInstitutionFrom(org.identifiers.cloud.hq.ws.registry.data.models.Institution institution) {
        return new Institution()
                .setId(institution.getId())
                .setName(institution.getName())
                .setDescription(institution.getDescription())
                .setHomeUrl(institution.getHomeUrl())
                .setRorId(institution.getRorId())
                .setLocation(getLocationFrom(institution.getLocation()));
    }

    public static Resource getResourceFrom(org.identifiers.cloud.hq.ws.registry.data.models.Resource resource) {
        return new Resource()
                .setId(resource.getId())
                .setMirId(resource.getMirId())
                .setUrlPattern(resource.getUrlPattern())
                .setName(resource.getName())
                .setDescription(resource.getDescription())
                .setOfficial(resource.isOfficial())
                .setProviderCode(resource.getProviderCode())
                .setSampleId(resource.getSampleId())
                .setResourceHomeUrl(resource.getResourceHomeUrl())
                .setInstitution(getInstitutionFrom(resource.getInstitution()))
                .setLocation(getLocationFrom(resource.getLocation()))
                .setDeprecated(resource.isDeprecated())
                .setDeprecationDate(resource.getDeprecationDate())
                .setProtectedUrls(resource.isProtectedUrls())
                .setRenderProtectedLanding(resource.isRenderProtectedLanding())
                .setAuthHelpDescription(resource.getAuthHelpDescription())
                .setAuthHelpUrl(resource.getAuthHelpUrl());
    }

    public static Namespace getNamespaceFrom(org.identifiers.cloud.hq.ws.registry.data.models.Namespace namespace) {
        return new Namespace()
                .setId(namespace.getId())
                .setMirId(namespace.getMirId())
                .setPrefix(namespace.getPrefix())
                .setName(namespace.getName())
                .setPattern(namespace.getPattern())
                .setDescription(namespace.getDescription())
                .setCreated(namespace.getCreated())
                .setModified(namespace.getModified())
                .setSampleId(namespace.getSampleId())
                .setNamespaceEmbeddedInLui(namespace.isNamespaceEmbeddedInLui())
                .setDeprecated(namespace.isDeprecated())
                .setDeprecationDate(namespace.getDeprecationDate());
    }

    // Get a Resource Registration Request from the request payload
    public static ResourceRegistrationRequest getResourceRegistrationRequestFrom(ServiceRequestRegisterResourcePayload payload) {
        return new ResourceRegistrationRequest()
                .setAdditionalInformation(payload.getAdditionalInformation())
                .setInstitutionDescription(payload.getInstitutionDescription())
                .setInstitutionHomeUrl(payload.getInstitutionHomeUrl())
                .setInstitutionRorId(payload.getInstitutionRorId())
                .setInstitutionLocation(payload.getInstitutionLocation())
                .setInstitutionName(payload.getInstitutionName())
                .setNamespacePrefix(payload.getNamespacePrefix())
                .setProviderCode(payload.getProviderCode())
                .setProviderDescription(payload.getProviderDescription())
                .setProviderHomeUrl(payload.getProviderHomeUrl())
                .setProviderLocation(payload.getProviderLocation())
                .setSampleId(payload.getSampleId())
                .setProviderName(payload.getProviderName())
                .setProviderUrlPattern(payload.getProviderUrlPattern())
                .setRequesterEmail(payload.getRequester().getEmail())
                .setRequesterName(payload.getRequester().getName())
                .setProtectedUrls(payload.isProtectedUrls())
                .setRenderProtectedLanding(payload.isRenderProtectedLanding())
                .setAuthHelpDescription(payload.getAuthHelpDescription())
                .setAuthHelpUrl(payload.getAuthHelpUrl());
    }

    // Get a Prefix Registration Request Payload from a Resource Registration Request Payload
    public static ServiceRequestRegisterPrefixPayload getFrom(ServiceRequestRegisterResourcePayload payload) {
        return new ServiceRequestRegisterPrefixPayload()
                .setAdditionalInformation(payload.getAdditionalInformation())
                .setRequester(payload.getRequester())
                .setSampleId(payload.getSampleId())
                .setInstitutionDescription(payload.getInstitutionDescription())
                .setInstitutionHomeUrl(payload.getInstitutionHomeUrl())
                .setInstitutionRorId(payload.getInstitutionRorId())
                .setInstitutionLocation(payload.getInstitutionLocation())
                .setInstitutionName(payload.getInstitutionName())
                .setProviderCode(payload.getProviderCode())
                .setProviderDescription(payload.getProviderDescription())
                .setProviderHomeUrl(payload.getProviderHomeUrl())
                .setProviderLocation(payload.getProviderLocation())
                .setProviderName(payload.getProviderName())
                .setProviderUrlPattern(payload.getProviderUrlPattern())
                .setRequestedPrefix(payload.getNamespacePrefix())
                .setProtectedUrls(payload.isProtectedUrls())
                .setRenderProtectedLanding(payload.isRenderProtectedLanding())
                .setAuthHelpDescription(payload.getAuthHelpDescription())
                .setAuthHelpUrl(payload.getAuthHelpUrl());
    }
}
