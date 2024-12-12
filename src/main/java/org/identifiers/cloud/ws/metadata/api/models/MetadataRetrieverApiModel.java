package org.identifiers.cloud.ws.metadata.api.models;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.identifiers.cloud.libapi.models.resolver.ParsedCompactIdentifier;
import org.identifiers.cloud.ws.metadata.retrivers.MetadataRetriver;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MetadataRetrieverApiModel {
    final List<MetadataRetriver> retrivers;
    private final List<MetadataRetriver> retrievers;

    public List<String> getListOfEnabledRetrieverEndpoints(HttpServletRequest request,
                                                            ParsedCompactIdentifier parsedCurie) {

        return retrivers.stream()
                .filter(r -> r.isEnabled(parsedCurie))
                .map(r -> getUrlForRetriever(r, request, parsedCurie))
                .toList();
    }

    public Optional<MetadataRetriver> getRetrieverFor(ParsedCompactIdentifier pci,
                                                      String retrieverId) {
        return retrievers.stream()
                .filter(r -> retrieverId.equals(r.getId()))
                .filter(r -> r.isEnabled(pci))
                .findAny();
    }

    private static String getUrlForRetriever(MetadataRetriver metadataRetriver,
                                               HttpServletRequest request,
                                               ParsedCompactIdentifier parsedCurie) {
        String scheme = request.getScheme();
        String host = request.getServerName();
        int port = request.getServerPort();

        String retrieverId = metadataRetriver.getId();
        String curie = parsedCurie.getRawRequest();

        var builder = UriComponentsBuilder
                .newInstance()
                .host(host).scheme(scheme)
                .path("/retrievers/{retriever}/{curie}");
        if ((scheme.equals("http") && port != 80) ||
                (scheme.equals("https") && port != 443)) {
            builder.port(port);
        }
        return builder.buildAndExpand(retrieverId, curie).toUriString();
    }

    public Optional<MediaType> getMediatypeForResponse(MetadataRetriver retriever,
                                                       String acceptHeader) {
        var acceptedMediaTypes = MediaType.parseMediaTypes(acceptHeader);
        var rawMediaTypes = retriever.getRawMediaType();
        return rawMediaTypes.stream()
                .filter(rmt -> acceptedMediaTypes.stream().anyMatch(rmt::isCompatibleWith))
                .findAny();
    }
}

