package org.identifiers.cloud.ws.resolver.services.mcp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.commons.messages.requests.resolver.ReverseResolutionRequestPayload;
import org.identifiers.cloud.commons.messages.responses.resolver.ReverseResolutionMatch;
import org.identifiers.cloud.ws.resolver.services.reverseresolution.ReverseResolutionService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class UrlConversionMcpTool {
    private final ReverseResolutionService rrService;

    @Tool(description = "Get identifiers.org URI for URL")
    public String convert(
            @ToolParam(description = "URL to be converted")
            String providerUrl,
            @ToolParam(description = "Accession of object under URL", required = false)
            String accession
    ) {
        log.debug("Get identifiers.org URI for {} (accession {})", providerUrl, accession);
        var request = getRequest(providerUrl, accession);
        var resp = rrService.resolveBasedOnPrefix(request);
        return resp.map(ReverseResolutionMatch::getPossibleIdorgUrl).orElse(null);
    }


    @Tool(description = "Get identifiers.org URIs that reference URLs similar to provider URL")
    public Collection<String> getSimilar(
            @ToolParam(description = "Provider URL to match with identifiers.org URIs")
            String providerUrl,
            @ToolParam(description = "Accession of object", required = false)
            String accession
    ) {
        log.debug("Get similar identifiers.org URI for {} (accession {})", providerUrl, accession);
        var request = getRequest(providerUrl, accession);
        var resp = rrService.resolveBasedOnSimilarity(request);
        return resp.stream()
                .map(ReverseResolutionMatch::getPossibleIdorgUrl)
                .toList();
    }



    private ReverseResolutionRequestPayload getRequest(String url, String accession) {
        ReverseResolutionRequestPayload request = new ReverseResolutionRequestPayload();
        request.setUrl(url);
        if (accession != null) {
            request.setAccession(accession);
        }
        return request;
    }
}
