package org.identifiers.cloud.ws.resolver.services.mcp;

import lombok.RequiredArgsConstructor;
import org.identifiers.cloud.commons.messages.models.ResolvedResource;
import org.identifiers.cloud.ws.resolver.api.models.ResolverApiModel;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import static java.util.Comparator.comparing;

@Service
@RequiredArgsConstructor
public class CurieResolutionMcpTool {
    final ResolverApiModel resolverApiModel;

    @Tool(description = "Get URLs resolved from curie or compact identifier")
    public Collection<String> resolve(@ToolParam(description = "Curie or compact identifier") String curie) {
        var resp = resolverApiModel.resolveRawCompactId(curie);
        if (resp.getHttpStatus() == HttpStatus.OK) {
            return resp.getPayload().getResolvedResources().stream()
                    .map(ResolvedResource::getCompactIdentifierResolvedUrl)
                    .toList();
        } else {
            return List.of();
        }
    }

    @Tool(description = "Get best URLs resolved from curie or compact identifier")
    public String resolveBest(@ToolParam(description = "Curie or compact identifier") String curie) {
        var resp = resolverApiModel.resolveRawCompactId(curie);
        if (resp.getHttpStatus() == HttpStatus.OK) {
            return resp.getPayload().getResolvedResources().stream()
                    .max(comparing(rr -> rr.getRecommendation().getRecommendationIndex()))
                    .map(ResolvedResource::getCompactIdentifierResolvedUrl)
                    .orElse(null);
        } else {
            return null;
        }
    }
}
