package org.identifiers.cloud.ws.resourcerecommender.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Lists;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: resource-recommender
 * Package: org.identifiers.cloud.ws.resourcerecommender.models
 * Timestamp: 2018-02-27 12:28
 * ---
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RecommendationStrategySimpleTest {
    private static List<ResolvedResource> officialResolvedResources;
    private static List<ResolvedResource> unOfficialResolvedResources;

    private RecommendationStrategy recommendationStrategy = new RecommendationStrategySimple();

    @BeforeClass
    public static void prepareResolvedResources() {
        unOfficialResolvedResources = new CopyOnWriteArrayList<>();
        officialResolvedResources = new CopyOnWriteArrayList<>();
        IntStream.range(0, 10).parallel().forEach(operand ->
                unOfficialResolvedResources.add(new ResolvedResource()
                        .setOfficial(false)
                        .setId(Integer.toString(operand))
                        .setEndPointUrl(String.format("http://endpoint/%d", operand)))
        );
        IntStream.range(10, 20).parallel().forEach(operand ->
                officialResolvedResources.add(new ResolvedResource()
                        .setOfficial(true)
                        .setId(Integer.toString(operand))
                        .setEndPointUrl(String.format("http://endpoint/%d", operand)))
        );
    }

    @Test
    public void testMixOfficialAndUnofficial() {
        List<ResolvedResource> unofficial = Lists.newArrayList(unOfficialResolvedResources);
        List<ResolvedResource> official = Lists.newArrayList(officialResolvedResources);
        Collections.shuffle(unofficial);
        Collections.shuffle(official);
        List<ResolvedResource> dataset = unofficial.subList(0, 3);
        dataset.add(official.get(0));
        // Evaluate the recommendation
        List<RecommendedResource> recommendations = recommendationStrategy.getRecommendations(dataset);
        ObjectMapper objectMapper = new ObjectMapper();
        recommendations.stream().forEach(recommendedResource -> {
            try {
                System.out.println(String.format("%s\n", objectMapper.writeValueAsString(recommendedResource)));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
        assertThat("All non-official resources are rated as '0'",
                (recommendations.size() - recommendations.parallelStream()
                        .filter(recommendedResource -> recommendedResource.getRecommendationIndex() == 0).count()) == 1,
                is(true));
    }
}