package org.identifiers.cloud.ws.resourcerecommender.models;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
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
    private List<WeightedScore> weightedScores;
    @Value("${org.identifiers.cloud.ws.resourcerecommender.backend.service.linkchecker.host}")
    private String serviceLinkCheckerHost;
    @Value("${org.identifiers.cloud.ws.resourcerecommender.backend.service.linkchecker.port}")
    private String serviceLinkCheckerPort;

    @PostConstruct
    public void init() {
        weightedScores = new ArrayList<>(3);
        weightedScores.add(new WeightedScore(55, new ScoreProviderOfficiality()));
        weightedScores.add(new WeightedScore(40,
                new ScoreProviderOnReliability(serviceLinkCheckerHost, serviceLinkCheckerPort)));
        weightedScores.add(new WeightedScore(5, new ScoreProviderEbi()));
    }

    @Override
    public synchronized List<WeightedScore> getFunctionComponents() throws ScoringFunctionProviderException {
        return weightedScores;
    }
}
