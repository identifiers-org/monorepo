package org.identifiers.cloud.ws.resourcerecommender.models;

import org.assertj.core.util.Lists;
import org.identifiers.cloud.commons.messages.models.ResolvedResource;
import org.identifiers.cloud.commons.messages.models.ResourceRecommendation;
import org.identifiers.cloud.commons.messages.responses.ServiceResponse;
import org.identifiers.cloud.commons.messages.responses.linkchecker.ServiceResponseScoringRequestPayload;
import org.identifiers.cloud.libapi.services.LinkCheckerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: resource-recommender
 * Package: org.identifiers.cloud.ws.resourcerecommender.models
 * Timestamp: 2018-02-27 12:28
 * ---
 */
// TODO - These unit tests are no longer valid with the new recommendation strategies
@SpringBootTest
class RecommendationStrategyTest {
    static List<ResolvedResource> officialResolvedResources;
    static List<ResolvedResource> unOfficialResolvedResources;
    static ResolvedResource ebiResource;

    @Autowired
    RecommendationStrategy recommendationStrategy;

    @MockBean
    LinkCheckerService linkCheckerService;

    @BeforeEach
    public void prepareResolvedResources() {
        unOfficialResolvedResources = new CopyOnWriteArrayList<>();
        officialResolvedResources = new CopyOnWriteArrayList<>();
        LongStream.range(0, 5).parallel().forEach(operand ->
                unOfficialResolvedResources.add(new ResolvedResource()
                        .setOfficial(false)
                        .setId(operand)
                        .setCompactIdentifierResolvedUrl(String.format("http://endpoint/%d", operand)))
        );
        IntStream.range(5, 10).parallel().forEach(operand ->
                officialResolvedResources.add(new ResolvedResource()
                        .setOfficial(true)
                        .setId(operand)
                        .setCompactIdentifierResolvedUrl(String.format("http://endpoint/%d", operand)))
        );
        ebiResource = new ResolvedResource()
                .setOfficial(true)
                .setId(10)
                .setCompactIdentifierResolvedUrl("https://ebi.ac.uk/something/20");

        // Make results of link checker the same for everyone
        ServiceResponseScoringRequestPayload payload = new ServiceResponseScoringRequestPayload().setScore(100);
        var req = ServiceResponse.of(payload);
        when(linkCheckerService.getScoreForProvider(anyString(), anyString())).thenReturn(req);
        when(linkCheckerService.getScoreForProvider(anyString(), anyString(), anyBoolean())).thenReturn(req);
        when(linkCheckerService.getScoreForResolvedId(anyString(), anyString(), anyBoolean())).thenReturn(req);
    }

    static boolean isOfficial (ResourceRecommendation i) {
        return Long.parseLong(i.getId()) >= 5;
    }
    static boolean isUnofficial (ResourceRecommendation i) {
        return !isOfficial(i);
    }

    @Test
    void testMixOfficialAndUnofficial() {
        List<ResolvedResource> dataset = Stream.concat(
                unOfficialResolvedResources.stream(),
                officialResolvedResources.stream()).collect(Collectors.toList());
        Collections.shuffle(dataset);

        List<ResourceRecommendation> recommendations = recommendationStrategy.getRecommendations(dataset);
        int minOfficialRecommendation = recommendations.stream()
                .filter(RecommendationStrategyTest::isOfficial)
                .map(ResourceRecommendation::getRecommendationIndex)
                .min(Comparator.naturalOrder()).get();
        int maxUnofficialRecommendation = recommendations.stream()
                .filter(RecommendationStrategyTest::isUnofficial)
                .map(ResourceRecommendation::getRecommendationIndex)
                .min(Comparator.naturalOrder()).get();
        assertTrue(minOfficialRecommendation > maxUnofficialRecommendation,
                "All official resources are rated larger than unofficial ones");
    }

    @Test
    void testEbiResourceWithLargerIndexThanNonEbi() {
        List<ResolvedResource> resources = Lists.newArrayList(officialResolvedResources);
        resources.add(ebiResource);
        Collections.shuffle(resources);
        List<ResourceRecommendation> recommendations = recommendationStrategy.getRecommendations(resources);
        int ebiRecommendation = recommendations.stream()
                .filter(r -> Objects.equals(String.valueOf(r.getId()), ebiResource.getId()))
                .mapToInt(ResourceRecommendation::getRecommendationIndex)
                .findFirst().orElse(0);
        int maxOtherRecommendations = recommendations.stream()
                .filter(r -> !Objects.equals(String.valueOf(r.getId()), ebiResource.getId()))
                .mapToInt(ResourceRecommendation::getRecommendationIndex)
                .max().orElse(Integer.MAX_VALUE);

        assertTrue(maxOtherRecommendations < ebiRecommendation,
                "EBI resources take preference when other parameters are equivalent");
    }

    @Test
    void testSingleUnofficialResolvedResource() {
        List<ResolvedResource> unOfficial = Lists.newArrayList(unOfficialResolvedResources);
        Collections.shuffle(unOfficial);
        List<ResourceRecommendation> recommendations = recommendationStrategy.getRecommendations(unOfficial.subList(0, 1));
        assertTrue((recommendations.size() == 1) && (recommendations.get(0).getRecommendationIndex() == 100),
                "When there is only one resource, this resource scores max.");
    }

    @Test
    void testSingleOfficialResolvedResource() {
        List<ResolvedResource> official = Lists.newArrayList(officialResolvedResources);
        Collections.shuffle(official);
        List<ResourceRecommendation> recommendations = recommendationStrategy.getRecommendations(official.subList(0, 1));
        assertTrue((recommendations.size() == 1) && (recommendations.get(0).getRecommendationIndex() == 100),
                "When there is only one resource, this resource scores max.");
    }

    @Test
    void testAllUnofficialResolvedResources() {
        List<ResolvedResource> unOfficial = Lists.newArrayList(unOfficialResolvedResources);
        Collections.shuffle(unOfficial);
        List<ResolvedResource> dataset = unOfficial.subList(0, unOfficial.size() / 2);
        List<ResourceRecommendation> recommendations = recommendationStrategy.getRecommendations(dataset);
        assertEquals(recommendations.size(), dataset.size(),
                "When all resources are unofficial, all of them go back");
    }

}