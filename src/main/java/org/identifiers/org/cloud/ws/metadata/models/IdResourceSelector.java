package org.identifiers.org.cloud.ws.metadata.models;

import java.util.List;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: metadata
 * Package: org.identifiers.org.cloud.ws.metadata.models
 * Timestamp: 2018-02-12 11:18
 * ---
 */
public interface IdResourceSelector {
    ResolverApiResponseResource selectResource(List<ResolverApiResponseResource> resources) throws IdResourceSelectorException;
}
