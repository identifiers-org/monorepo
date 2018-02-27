package org.identifiers.cloud.ws.resourcerecommender.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: resource-recommender
 * Package: org.identifiers.cloud.ws.resourcerecommender.models
 * Timestamp: 2018-02-27 11:43
 * ---
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecommendedResource implements Serializable {
    // This is an index [0,99] on how recommendable is this resource, 0 - not at all, 99 - way to go
    private int recommendationIndex = 0;
    private String recommendationExplanation = "no explanation has been specified";
    // This is the contextual ID of the resource in the current recommendation request
    private String id;
    private String endPointUrl;

    public int getRecommendationIndex() {
        return recommendationIndex;
    }

    public RecommendedResource setRecommendationIndex(int recommendationIndex) {
        this.recommendationIndex = recommendationIndex;
        return this;
    }

    public String getRecommendationExplanation() {
        return recommendationExplanation;
    }

    public RecommendedResource setRecommendationExplanation(String recommendationExplanation) {
        this.recommendationExplanation = recommendationExplanation;
        return this;
    }

    public String getId() {
        return id;
    }

    public RecommendedResource setId(String id) {
        this.id = id;
        return this;
    }

    public String getEndPointUrl() {
        return endPointUrl;
    }

    public RecommendedResource setEndPointUrl(String endPointUrl) {
        this.endPointUrl = endPointUrl;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecommendedResource that = (RecommendedResource) o;
        return getRecommendationIndex() == that.getRecommendationIndex() &&
                Objects.equals(getRecommendationExplanation(), that.getRecommendationExplanation()) &&
                Objects.equals(getId(), that.getId()) &&
                Objects.equals(getEndPointUrl(), that.getEndPointUrl());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getRecommendationIndex(), getRecommendationExplanation(), getId(), getEndPointUrl());
    }
}
