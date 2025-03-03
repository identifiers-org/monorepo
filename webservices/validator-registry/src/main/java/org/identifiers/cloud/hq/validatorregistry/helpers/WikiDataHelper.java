package org.identifiers.cloud.hq.validatorregistry.helpers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;
import org.apache.commons.io.IOUtils;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.joining;


@Slf4j
@Service
public class WikiDataHelper {;

    private final TokenNameFinderModel tokenNameFinderModel;
    private final TokenizerModel tokenizerModel;
    private final UriComponentsBuilder wdActionEndpointUriBuilder;
    private final RestTemplate restTemplate;
    private final SPARQLRepository wikidataSparqlRepository;
    private final String wikidataDetailsQueryStringTemplate;
    private final String wikidataRorDetailsQueryString;
    public WikiDataHelper(TokenNameFinderModel tokenNameFinderModel, TokenizerModel tokenizerModel,
                          @Value("${org.identifiers.cloud.wikidata.action-api-endpoint}")
                          String WikiDataActionEndpoint,
                          @Value("${org.identifiers.cloud.wikidata.search-limit}")
                          int searchLimit,
                          RestTemplate restTemplate,
                          SPARQLRepository wikidataSparqlRepository,
                          @Value("classpath:wikidataDetailsQueryTemplate.sparql")
                          Resource wikidataDetailsQuery,
                          @Value("classpath:wikidataRorDetails.sparql")
                          Resource wikidataRorDetailsQuery) throws IOException {
        this.tokenNameFinderModel = tokenNameFinderModel;
        this.tokenizerModel = tokenizerModel;
        this.restTemplate = restTemplate;
        this.wikidataSparqlRepository = wikidataSparqlRepository;
        this.wikidataDetailsQueryStringTemplate = IOUtils.toString(
                wikidataDetailsQuery.getInputStream(), StandardCharsets.UTF_8
        );
        this.wikidataRorDetailsQueryString = IOUtils.toString(
                wikidataRorDetailsQuery.getInputStream(), StandardCharsets.UTF_8
        );
        wdActionEndpointUriBuilder = UriComponentsBuilder
                .fromHttpUrl(WikiDataActionEndpoint)
                .queryParam("action", "query")
                .queryParam("list", "search")
                .queryParam("format", "json")
                .queryParam("srlimit", searchLimit);
    }

    /**
     * This finds possible WikiData IDs for an organization name by doing a full text search using WikiData action endpoint.
     * Multiple IDs are returned since the query multiple entries might match the result.
     * @see <a href="https://www.mediawiki.org/wiki/API:Main_page">Main media wiki API page</a>
     * @see <a href="https://www.mediawiki.org/wiki/API:Search">Media Wiki documentation for search action</a>
     * @param organizationName name of organization to search
     * @return List of possible WikiData QIDs, for example: Q1341845 for EMBL-EBI
     */
    public List<String> searchForWikiDataIds(String organizationName) {
        var uri = wdActionEndpointUriBuilder.replaceQueryParam("srsearch", organizationName).build().toUri();
        try {
            var httpResponse = restTemplate.getForEntity(uri, ObjectNode.class);
            if (httpResponse.getStatusCode().is2xxSuccessful() && httpResponse.getBody() != null) {
                var searchResultsList = httpResponse.getBody().at("/query/search");
                return StreamSupport.stream(searchResultsList.spliterator(), false)
                        .map(n -> n.get("title").asText()).toList();
            }
            return List.of();
        } catch (RestClientException e) {
            log.error("Exception when retrieving WikiData; {}", e.getMessage());
            log.debug("Exception stacktrace", e);
            return List.of();
        }
    }

    /**
     * Retrieves organization details from the wikidata and dbpedia sparql endpoints.
     * The query used also filters results to organizations.
     * Information is not guaranteed to exist for all input QIDs.
     * @param wikidataIds list of QIDs to search details of
     * @return list of details on organizations associated with QIDs,
     * its size is not guaranteed to be of the same length as the input due to possible missing data and
     * filtering by organization class
     */
    public List<WikiDataOrganizationDetails> getOrganizationDetailsFromQids(List<String> wikidataIds) {
        var joinedIds = wikidataIds.stream()
                .map("wd:"::concat)
                .collect(joining(" "));
        var queryStr = String.format(wikidataDetailsQueryStringTemplate, joinedIds);

        var connection = wikidataSparqlRepository.getConnection();
        var query = connection.prepareTupleQuery(queryStr);

        try (var bindingSets = query.evaluate()) {
            return bindingSets.stream().map(WikiDataOrganizationDetails::fromBindingSet).toList();
        } catch (QueryEvaluationException e) {
            log.error("Exception when querying WikiData sparql: {}", e.getMessage());
            log.debug("Exception stacktrace", e);
            return List.of();
        }
    }

    public Optional<WikiDataOrganizationDetails> getOrganizationDetailsFromRorId(String rorId) {
        if (rorId.startsWith("http")) {
            rorId = UriComponentsBuilder.fromHttpUrl(rorId).build().getPathSegments().get(0);
        }
        var connection = wikidataSparqlRepository.getConnection();
        var query = connection.prepareTupleQuery(wikidataRorDetailsQueryString);
        query.setBinding("rorId", Values.literal(rorId));

        try (var bindingSets = query.evaluate()) {
            if (bindingSets.hasNext()) {
                return Optional.of(WikiDataOrganizationDetails.fromBindingSet(bindingSets.next()));
            } else {
                return Optional.empty();
            }
        } catch (QueryEvaluationException e) {
            log.error("Exception when querying WikiData sparql by ror ID: {}", e.getMessage());
            log.debug("Exception stacktrace", e);
            return Optional.empty();
        }
    }

    public String[] findOrganizationNamesUsingNlp(String name) {
        var tokenizer = new TokenizerME(tokenizerModel);
        var nameFinder = new NameFinderME(tokenNameFinderModel);

        var tokens = tokenizer.tokenize(name);
        var spans = nameFinder.find(tokens);

        return Span.spansToStrings(spans, tokens);
    }

    @Data
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class WikiDataOrganizationDetails {
        private String name, homeUrl, qid, rorId, locCode, locName;
        static WikiDataOrganizationDetails fromBindingSet(BindingSet bs) {
            var result = new WikiDataOrganizationDetails();

            result.setQid(bs.getBinding("institution").getValue().stringValue());
            if (bs.hasBinding("name")) {
                result.name = bs.getBinding("name").getValue().stringValue();
            } else if (bs.hasBinding("dbpedia_name")) {
                result.name = bs.getBinding("dbpedia_name").getValue().stringValue();
            }
            if (bs.hasBinding("homeUrl")) {
                result.homeUrl = bs.getBinding("homeUrl").getValue().stringValue();
            }
            if (bs.hasBinding("rorId")) {
                result.rorId = bs.getBinding("rorId").getValue().stringValue();
            }
            if (bs.hasBinding("locCode")) {
                result.locCode = bs.getBinding("locCode").getValue().stringValue();
            }
            if (bs.hasBinding("locName")) {
                result.locName = bs.getBinding("locName").getValue().stringValue();
            }

            return result;
        }
    }
}
