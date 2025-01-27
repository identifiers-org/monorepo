package org.identifiers.cloud.ws.resourcerecommender.models;

import org.assertj.core.util.Lists;
import org.identifiers.cloud.ws.resourcerecommender.api.data.models.ResolvedResource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: resource-recommender
 * Package: org.identifiers.cloud.ws.resourcerecommender.models
 * Timestamp: 2018-02-27 12:28
 * ---
 */

@SpringBootTest
@Disabled // For not being the active strategy
public class RecommendationStrategySimpleTest {
    private static List<ResolvedResource> officialResolvedResources;
    private static List<ResolvedResource> unOfficialResolvedResources;

    private final RecommendationStrategy recommendationStrategy = new RecommendationStrategySimple();

    @BeforeAll
    public static void prepareResolvedResources() {
        unOfficialResolvedResources = new CopyOnWriteArrayList<>();
        officialResolvedResources = new CopyOnWriteArrayList<>();
        LongStream.range(0, 10).parallel().forEach(operand ->
                unOfficialResolvedResources.add(new ResolvedResource()
                        .setOfficial(false)
                        .setId(Long.toString(operand))
                        .setCompactIdentifierResolvedUrl(String.format("http://endpoint/%d", operand)))
        );
        IntStream.range(10, 20).parallel().forEach(operand ->
                officialResolvedResources.add(new ResolvedResource()
                        .setOfficial(true)
                        .setId(Long.toString(operand))
                        .setCompactIdentifierResolvedUrl(String.format("http://endpoint/%d", operand)))
        );
    }

    @Test
    void testMixOfficialAndUnofficial() {
        List<ResolvedResource> unofficial = Lists.newArrayList(unOfficialResolvedResources);
        List<ResolvedResource> official = Lists.newArrayList(officialResolvedResources);
        Collections.shuffle(unofficial);
        Collections.shuffle(official);
        List<ResolvedResource> dataset = unofficial.subList(0, 3);
        dataset.add(official.get(0));
        // Evaluate the recommendation
        List<ResourceRecommendation> recommendations = recommendationStrategy.getRecommendations(dataset);
/*      ObjectMapper objectMapper = new ObjectMapper();
        recommendations.stream().forEach(recommendedResource -> {
            try {
                System.out.println(String.format("%s\n", objectMapper.writeValueAsString(recommendedResource)));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
*/
        assertThat("All non-official resources are rated as '0'",
                (recommendations.size() - recommendations.parallelStream()
                        .filter(recommendedResource -> recommendedResource.getRecommendationIndex() == 0).count()) == 1,
                is(true));
    }

    @Test
    void testSingleOfficialResolvedResource() {
        List<ResolvedResource> official = Lists.newArrayList(officialResolvedResources);
        Collections.shuffle(official);
        List<ResourceRecommendation> recommendations = recommendationStrategy.getRecommendations(official.subList(0, 1));
        assertThat("When there is only one resource, this resource scores max.",
                ((recommendations.size() == 1) && (recommendations.get(0).getRecommendationIndex() == 99)),
                is(true));
    }

    @Test
    void testSingleUnofficialResolvedResource() {
        List<ResolvedResource> unOfficial = Lists.newArrayList(unOfficialResolvedResources);
        Collections.shuffle(unOfficial);
        List<ResourceRecommendation> recommendations = recommendationStrategy.getRecommendations(unOfficial.subList(0, 1));
        assertThat("When there is only one resource, this resource scores max.",
                ((recommendations.size() == 1) && (recommendations.get(0).getRecommendationIndex() == 99)),
                is(true));
    }

    @Test
    void testAllUnofficialResolvedResources() {
        List<ResolvedResource> unOfficial = Lists.newArrayList(unOfficialResolvedResources);
        Collections.shuffle(unOfficial);
        List<ResolvedResource> dataset = unOfficial.subList(0, unOfficial.size() / 2);
        List<ResourceRecommendation> recommendations = recommendationStrategy.getRecommendations(dataset);
        assertThat("When all resources are unofficial, all of them go back",
                recommendations.size() == dataset.size(),
                is(true));
        assertThat("When all resources are unofficial, one of them scores max",
                recommendations
                        .parallelStream()
                        .filter(recommendedResource -> recommendedResource
                                .getRecommendationIndex() == 99)
                        .count() == 1,
                is(true));
    }

}