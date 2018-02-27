package org.identifiers.cloud.ws.resourcerecommender.models;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.IntStream;

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

    @BeforeClass
    public static void prepareResolvedResources() {
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
}