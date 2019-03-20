package org.identifiers.cloud.hq.ws.registry.models.helpers;

import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefixPayload;
import org.identifiers.cloud.hq.ws.registry.data.models.PrefixRegistrationRequest;

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
public class ApiDataModelHelper {

    public static PrefixRegistrationRequest getPrefixRegistrationRequest(ServiceRequestRegisterPrefixPayload sourceModel) {
        String references = "";
        if (sourceModel.getReferences() != null) {
            references = "".join(",", sourceModel.getReferences());
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
                .setRequestedPrefix(sourceModel.getRequestedPrefix())
                .setProviderUrlPattern(sourceModel.getProviderUrlPattern())
                .setSampleId(sourceModel.getSampleId())
                .setIdRegexPattern(sourceModel.getIdRegexPattern())
                .setSupportingReferences(references)
                .setAdditionalInformation(additionalInformation)
                .setRequesterName(sourceModel.getRequester().getName())
                .setRequesterEmail(sourceModel.getRequester().getEmail());
    }
}
