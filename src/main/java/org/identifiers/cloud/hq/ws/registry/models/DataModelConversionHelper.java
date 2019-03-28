package org.identifiers.cloud.hq.ws.registry.models;

import org.identifiers.cloud.hq.ws.registry.data.models.*;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models
 * Timestamp: 2019-03-28 13:46
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This helper implements methods for converting between different data models
 */
public class DataModelConversionHelper {
    // TODO
    public static Resource getFrom(PrefixRegistrationRequest prefixRegistrationRequest) {
        Resource resource = new Resource();
        // TODO
        // Create and fill in the institution information
        resource.setInstitution(new Institution()
                .setName(prefixRegistrationRequest.getInstitutionName())
                .setDescription(prefixRegistrationRequest.getInstitutionDescription())
                .setHomeUrl(prefixRegistrationRequest.getInstitutionHomeUrl())
                .setLocation(new Location().setCountryCode(prefixRegistrationRequest.getInstitutionLocation()))
        );
        // Create and fill in the namespace information
        resource.setNamespace(new Namespace()
                .setPrefix(prefixRegistrationRequest.getRequestedPrefix())
                .setName(prefixRegistrationRequest.getName())
                .setPattern(prefixRegistrationRequest.getIdRegexPattern())
                .setDescription(prefixRegistrationRequest.getDescription())
                .setSampleId(prefixRegistrationRequest.getSampleId())
                .setContactPerson(
                        new Person()
                                .setFullName(prefixRegistrationRequest.getRequesterName())
                                .setEmail(prefixRegistrationRequest.getRequesterEmail()))
        );
        // TODO Create and fill in the resource (provider) information
        return resource;
    }
}
