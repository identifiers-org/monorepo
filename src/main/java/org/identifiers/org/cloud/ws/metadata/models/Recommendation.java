package org.identifiers.org.cloud.ws.metadata.models;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: resolver
 * Package: org.identifiers.cloud.ws.resolver.models
 * Timestamp: 2018-02-28 14:22
 * ---
 */
public class Recommendation {
    // This models a recommendation attached to a particular resource in the response from this web service
    private int recommendationIndex = 0;
    private String recommendationExplanation = "--- default explanation ---";

    public int getRecommendationIndex() {
        return recommendationIndex;
    }

    public Recommendation setRecommendationIndex(int recommendationIndex) {
        this.recommendationIndex = recommendationIndex;
        return this;
    }

    public String getRecommendationExplanation() {
        return recommendationExplanation;
    }

    public Recommendation setRecommendationExplanation(String recommendationExplanation) {
        this.recommendationExplanation = recommendationExplanation;
        return this;
    }
}
