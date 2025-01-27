package org.identifiers.cloud.ws.resourcerecommender.models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
    private final List<WeightedScore> weightedScores;

    public ScoringFunctionProviderSimple(ScoreProviderOnReliability scoreProviderOnReliability) {
        Assert.notNull(scoreProviderOnReliability, "Reliability provided required");
        weightedScores = Arrays.asList(
                new WeightedScore(55, new ScoreProviderOfficiality()),
                new WeightedScore(40, scoreProviderOnReliability),
                new WeightedScore(5, new ScoreProviderEbi()));
    }

    @Override
    public List<WeightedScore> getFunctionComponents() throws ScoringFunctionProviderException {
        return weightedScores;
    }
}
