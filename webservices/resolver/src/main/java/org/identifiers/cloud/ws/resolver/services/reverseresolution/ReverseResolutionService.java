package org.identifiers.cloud.ws.resolver.services.reverseresolution;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.identifiers.cloud.commons.messages.responses.resolver.ReverseResolutionMatch;
import org.identifiers.cloud.commons.messages.requests.resolver.ReverseResolutionRequestPayload;
import org.identifiers.cloud.ws.resolver.services.reverseresolution.resolvers.SimilarityReverseResolver;
import org.identifiers.cloud.ws.resolver.services.reverseresolution.resolvers.UrlPrefixReverseResolver;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReverseResolutionService {
    private final UrlPrefixReverseResolver prefixResolver;
    private final SimilarityReverseResolver similarityResolver;

    public Optional<ReverseResolutionMatch> resolveBasedOnPrefix (ReverseResolutionRequestPayload request) {
        final var queryUrl = Utils.normalizeUrl(request.getUrl());
        final String queryAccession = StringUtils.isBlank(request.getAccession()) ?
                guessAccession(request.getUrl()) : request.getAccession();
        if (StringUtils.isBlank(queryAccession)) {
            return Optional.empty();
        }

        log.debug("Query url: {}", queryUrl);
        var likelyMatch = prefixResolver.resolve(queryUrl, queryAccession);
        if (likelyMatch.isPresent()) {
            var match = matchFromData(likelyMatch.get(), queryUrl, queryAccession, request.isForceUrls());
            return Optional.of(match);
        } else {
            return Optional.empty();
        }
    }

    public List<ReverseResolutionMatch> resolveBasedOnSimilarity (ReverseResolutionRequestPayload request) {
        final var queryUrl = Utils.normalizeUrl(request.getUrl());
        final var queryAccession = StringUtils.isBlank(request.getAccession()) ?
            guessAccession(request.getUrl()) : request.getAccession();
        if (StringUtils.isBlank(queryAccession)) {
            return List.of();
        }

        var otherMatches = similarityResolver.resolve(queryUrl, queryAccession).toList();
        return otherMatches.stream()
                .map(match -> matchFromData(match, queryUrl, queryAccession, request.isForceUrls()))
                .toList();
    }


    /**
     * This tries to guess the accession based on the URL.
     * It assumes that the accession goes at the end of the URL and is either
     * <ul>
     *     <li>a query parameter,</li>
     *     <li>an url component, or</li>
     *     <li>a fragment.</li>
     * </ul>
     * This is a fair assumption as it is true for most of the current URL patterns in the registry.
     * @param url to find accession from
     * @return guessed accession.
     */
    static String guessAccession (String url) {
        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }

        int actualIndex = Stream.of('=', '/', '#')
                .map(url::lastIndexOf)
                .max(Integer::compareTo)
                .orElse(-1);
        if (actualIndex == -1) {
            log.info("Failed to guess accession for {}", url);
            return null;
        }

        var guess = url.substring(actualIndex+1);
        log.debug("Guessed accession: '{}'", guess);
        return guess;
    }




    private static ReverseResolutionMatch matchFromData(RrResourceData match, String queryUrl,
                                                        String queryAccession, boolean forceUrls) {
        boolean luiPatternMatch = Utils.doesAccessionMatch(match, queryAccession);
        float similarityScore = Utils.getResolvedUrlSimilarity(match, queryUrl, queryAccession);

        String possibleUrl = null;
        String curie = null;
        if (luiPatternMatch || forceUrls) {
            possibleUrl = Utils.getIdorgUrl(match, queryAccession);
            curie = Utils.getIdorgCurie(match, queryAccession);
        }
        return new ReverseResolutionMatch(
                match.prefix(),
                possibleUrl, curie,
                luiPatternMatch,
                similarityScore);
    }
}
