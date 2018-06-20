package org.identifiers.cloud.ws.resourcerecommender.models;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Project: resource-recommender
 * Package: org.identifiers.cloud.ws.resourcerecommender.models
 * Timestamp: 2018-06-20 11:44
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Component
public class ScoringFunctionProviderSimple implements ScoringFunctionProvider {
    private List<WeightedScore> weightedScores = new ArrayList<>();
    @Value("${org.identifiers.cloud.ws.resourcerecommender.backend.service.linkchecker.host}")
    private String serviceLinkCheckerHost;
    @Value("${org.identifiers.cloud.ws.resourcerecommender.backend.service.linkchecker.port}")
    private String serviceLinkCheckerPort;

    @Override
    public List<WeightedScore> getFunctionComponents() throws ScoringFunctionProviderException {
        if (weightedScores.isEmpty()) {
            // TODO - This can be improved in the future, and the weights dynamically set through environment variables
            weightedScores.add(new WeightedScore(60, new ScoreProviderOfficiality()));
            weightedScores.add(new WeightedScore(40,
                    new ScoreProviderOnReliability(serviceLinkCheckerHost, serviceLinkCheckerPort)));
        }
        return weightedScores;
    }
}
