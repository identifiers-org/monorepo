package org.identifiers.cloud.commons.messages.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: resource-recommender
 * Package: org.identifiers.cloud.ws.resourcerecommender.models
 * Timestamp: 2018-02-27 11:43
 * ---
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResourceRecommendation implements Serializable, Comparable<ResourceRecommendation> {
    // This is an index [0,99] on how recommendable is this resource, 0 - not at all, 99 - way to go
    private int recommendationIndex = 0;
    private String recommendationExplanation = "no explanation has been specified";
    // This is the contextual ID of the resource in the current recommendation request
    private String id;
    private String compactIdentifierResolvedUrl;

    @Override
    public int compareTo(ResourceRecommendation o) {
        return Integer.compare(recommendationIndex, o.recommendationIndex);
    }
}
