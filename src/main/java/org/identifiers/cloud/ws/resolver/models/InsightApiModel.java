package org.identifiers.cloud.ws.resolver.models;

import org.identifiers.cloud.ws.resolver.models.api.responses.ResolvedResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Project: resolver
 * Package: org.identifiers.cloud.ws.resolver.models
 * Timestamp: 2018-05-16 13:50
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 * <p>
 * Main model for Insight API Controller.
 */
@Component
@Scope("prototype")
public class InsightApiModel {
    // TODO
    @Autowired
    private ResolverDataHelper resolverDataHelper;

    // NOTE - This is fine, don't panic ^_^
    public List<ResolvedResource> getAllSampleIdsResolved() {
        // TODO
        return null;
    }

    public List<ResolvedResource> getAllHomeUrls() {
        // TODO
        return null;
    }
}
