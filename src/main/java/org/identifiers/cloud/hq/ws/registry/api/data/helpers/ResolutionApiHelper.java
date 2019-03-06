package org.identifiers.cloud.hq.ws.registry.api.data.helpers;

import org.identifiers.cloud.hq.ws.registry.api.data.models.Namespace;
import org.identifiers.cloud.hq.ws.registry.api.data.models.Resource;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.data.helpers
 * Timestamp: 2018-10-17 12:29
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This is a data helper in the context of the Resolution API data models
 */
// TODO this is affected by the refactoring
public class ResolutionApiHelper {
    // NOTE - I don't totally like to have two models with the same name, as it makes the coding more prone to
    // making mistakes, but, at the same time, this is the meaning of it, and that's why we have packages,
    // right?
    public static List<Namespace> getResolutionDatasetFrom(List<org.identifiers.cloud.hq.ws.registry.data.models.Namespace> namespaces) {
        return namespaces.parallelStream().map(namespace -> {
            Namespace resultNamespace = new Namespace()
                    .setId(namespace.getId())
                    .setMirId(namespace.getMirId())
                    .setName(namespace.getName())
                    .setPattern(namespace.getPattern())
                    .setDescription(namespace.getDescription())
                    .setPrefix(namespace.getPrefix());
            for (org.identifiers.cloud.hq.ws.registry.data.models.Resource resource :
                    namespace.getResources()) {
                resultNamespace.getResources().add(
                        new Resource()
                                .setId(resource.getId())
                                .setMirId(resource.getMirId())
                                .setAccessUrl(resource.getAccessUrl())
                                .setInfo(resource.getInfo())
                                .setInstitution((resource.getInstitution() != null ? resource.getInstitution().getName() : null))
                                .setLocation((resource.getLocation() != null ? resource.getLocation().getCountryCode() : null))
                                .setOfficial(resource.isOfficial())
                                .setResourcePrefix(resource.getResourcePrefix())
                                .setLocalId(resource.getLocalId())
                                .setResourceUrl(resource.getResourceUrl())
                );
            }
            return resultNamespace;
        }).collect(Collectors.toList());
    }
}
