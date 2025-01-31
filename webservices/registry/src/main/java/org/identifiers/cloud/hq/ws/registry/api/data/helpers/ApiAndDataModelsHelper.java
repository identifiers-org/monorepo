package org.identifiers.cloud.hq.ws.registry.api.data.helpers;

import org.identifiers.cloud.commons.messages.models.Institution;
import org.identifiers.cloud.commons.messages.models.Location;
import org.identifiers.cloud.commons.messages.models.Namespace;
import org.identifiers.cloud.commons.messages.models.Resource;
import org.identifiers.cloud.commons.messages.requests.registry.ServiceRequestRegisterPrefixPayload;
import org.identifiers.cloud.commons.messages.requests.registry.ServiceRequestRegisterResourcePayload;
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
            references = String.join(",", sourceModel.getSupportingReferences());
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

    public static Location getMessagePojoFromEntity(org.identifiers.cloud.hq.ws.registry.data.models.Location entity) {
        return new Location()
                .setCountryCode(entity.getCountryCode())
                .setCountryName(entity.getCountryName());
    }

    public static Institution getMessagePojoFromEntity(org.identifiers.cloud.hq.ws.registry.data.models.Institution entity) {
        return new Institution()
                .setId(entity.getId())
                .setName(entity.getName())
                .setDescription(entity.getDescription())
                .setHomeUrl(entity.getHomeUrl())
                .setRorId(entity.getRorId())
                .setLocation(getMessagePojoFromEntity(entity.getLocation()));
    }

    public static Resource getMessagePojoFromEntity(org.identifiers.cloud.hq.ws.registry.data.models.Resource entity) {
        return new Resource()
                .setId(entity.getId())
                .setMirId(entity.getMirId())
                .setUrlPattern(entity.getUrlPattern())
                .setName(entity.getName())
                .setDescription(entity.getDescription())
                .setOfficial(entity.isOfficial())
                .setProviderCode(entity.getProviderCode())
                .setSampleId(entity.getSampleId())
                .setResourceHomeUrl(entity.getResourceHomeUrl())
                .setInstitution(getMessagePojoFromEntity(entity.getInstitution()))
                .setLocation(getMessagePojoFromEntity(entity.getLocation()))
                .setDeprecated(entity.isDeprecated())
                .setDeprecationDate(entity.getDeprecationDate())
                .setRenderDeprecatedLanding(entity.isRenderDeprecatedLanding())
                .setProtectedUrls(entity.isProtectedUrls())
                .setRenderProtectedLanding(entity.isRenderProtectedLanding())
                .setAuthHelpDescription(entity.getAuthHelpDescription())
                .setAuthHelpUrl(entity.getAuthHelpUrl());
    }

    public static Namespace getMessagePojoFromEntity(org.identifiers.cloud.hq.ws.registry.data.models.Namespace entity) {
        return new Namespace()
                .setId(entity.getId())
                .setMirId(entity.getMirId())
                .setPrefix(entity.getPrefix())
                .setName(entity.getName())
                .setPattern(entity.getPattern())
                .setDescription(entity.getDescription())
                .setCreated(entity.getCreated())
                .setModified(entity.getModified())
                .setSampleId(entity.getSampleId())
                .setNamespaceEmbeddedInLui(entity.isNamespaceEmbeddedInLui())
                .setDeprecated(entity.isDeprecated())
                .setDeprecationDate(entity.getDeprecationDate())
                .setRenderDeprecatedLanding(entity.isRenderDeprecatedLanding());
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
