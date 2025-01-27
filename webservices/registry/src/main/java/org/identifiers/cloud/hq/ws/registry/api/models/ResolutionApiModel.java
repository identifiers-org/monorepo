package org.identifiers.cloud.hq.ws.registry.api.models;

import lombok.RequiredArgsConstructor;
import org.identifiers.cloud.hq.ws.registry.api.ApiCentral;
import org.identifiers.cloud.hq.ws.registry.api.data.models.Namespace;
import org.identifiers.cloud.hq.ws.registry.api.data.models.Resource;
import org.identifiers.cloud.hq.ws.registry.api.data.services.NamespaceApiService;
import org.identifiers.cloud.hq.ws.registry.api.responses.ResolverDatasetPayload;
import org.identifiers.cloud.hq.ws.registry.api.responses.ServiceResponseGetResolverDataset;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import static org.springframework.util.StringUtils.startsWithIgnoreCase;


/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.models
 * Timestamp: 2018-10-16 12:56
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Component
@RequiredArgsConstructor
public class ResolutionApiModel {
    private final NamespaceApiService namespaceApiService;

    // Model API
    public ServiceResponseGetResolverDataset getResolverDataset(boolean rewriteForEmbeddedPrefixes) {
        // Default response
        ServiceResponseGetResolverDataset response = new ServiceResponseGetResolverDataset();
        response.setApiVersion(ApiCentral.apiVersion);
        response.setHttpStatus(HttpStatus.OK);
        response.setPayload(new ResolverDatasetPayload());
        try {
            response.getPayload().setNamespaces(namespaceApiService.getNamespaceTreeDownToLeaves());
            if (rewriteForEmbeddedPrefixes) {
                response.getPayload()
                        .getNamespaces()
                        .stream().filter(Namespace::isNamespaceEmbeddedInLui)
                        .forEach(ResolutionApiModel::rewriteForEmbeddedPrefixes);
            }
        } catch (RuntimeException e) {
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setErrorMessage(e.getMessage());
        }
        return response;
    }

    /**
     * This function changes the input namespace URL pattern and sample ID to account for the prefix being embedded in the LUI.
     *  This will remove the prefix from the url pattern if it's right before the id template variable.
     *  It will also add the prefix to the sample ID if it already doesn't have it.
     * It is assumed that the namespace has the prefix embedded in lui flag set.
     * @param namespace to be rewritten in place.
     */
    static void rewriteForEmbeddedPrefixes(Namespace namespace) {
        String prefix = namespace.getPrefix();
        for (Resource resource : namespace.getResources()) {
            String urlPattern = resource.getUrlPattern();
            int templateVarIdx = urlPattern.indexOf("{$id}");
            int prefixExpectedStart = templateVarIdx - prefix.length() - 1; // -1 to account for colon (:) character
            String possiblePrefix = urlPattern.substring(prefixExpectedStart, templateVarIdx);

            if (possiblePrefix.equalsIgnoreCase(prefix + ':')) {
                String newUrlPattern = //URL pattern without prefix before template variable
                        urlPattern.substring(0, prefixExpectedStart) +
                        urlPattern.substring(templateVarIdx);
                resource.setUrlPattern(newUrlPattern);

                if (!startsWithIgnoreCase(resource.getSampleId(), possiblePrefix)) {
                    // Also rewrite resource sample ID if necessary and URL was rewritten
                    resource.setSampleId(possiblePrefix + resource.getSampleId());
                }
            }
        }
    }
}
