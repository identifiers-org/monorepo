package org.identifiers.cloud.ws.resourcerecommender.models;

/**
 * Project: resource-recommender
 * Package: org.identifiers.cloud.ws.resourcerecommender.models
 * Timestamp: 2018-06-20 11:32
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This POJO represents a weighted score, it consists of the a weigth [0,100] and a score provider to which apply the
 * weight.
 */
public class WeightedScore {
    public static final int MAX_SCORE = 100;
    public static final int MIN_SCORE = 0;

    private int weight;
    private ScoreProvider scoreProvider;

    public WeightedScore(int weight, ScoreProvider scoreProvider) {
        if ((weight > MAX_SCORE) || (weight < MIN_SCORE)) {
            // TODO
        }
        this.weight = weight;
        this.scoreProvider = scoreProvider;
    }
    
}
