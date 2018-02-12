package org.identifiers.org.cloud.ws.metadata.models;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: metadata
 * Package: org.identifiers.org.cloud.ws.metadata.models
 * Timestamp: 2018-02-12 11:21
 * ---
 */
@Component
@Scope("prototype")
public class IdResourceSelectorSimpleOfficial implements IdResourceSelector {
    @Override
    public ResolverApiResponseResource selectResource(List<ResolverApiResponseResource> resources) throws IdResourceSelectorException {
        List<ResolverApiResponseResource> selected = resources.parallelStream().filter(resolverApiResponseResource -> resolverApiResponseResource.isOfficial()).collect(Collectors.toList());
        if (selected.isEmpty()) {
            throw new IdResourceSelectorException("NO ID RESOURCE could be selected for mining metadata from (select official resource selector)");
        }
        if (selected.size() > 1) {
            throw new IdResourceSelectorException("THERE IS MORE THAN ONE official resource for the given list of ID resources");
        }
        return selected.get(0);
    }
}
