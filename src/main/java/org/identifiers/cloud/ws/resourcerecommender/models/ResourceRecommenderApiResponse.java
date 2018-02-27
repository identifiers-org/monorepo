package org.identifiers.cloud.ws.resourcerecommender.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.List;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: resource-recommender
 * Package: org.identifiers.cloud.ws.resourcerecommender.models
 * Timestamp: 2018-02-27 11:33
 * ---
 */
@JsonIgnoreProperties(ignoreUnknown = true, value = {"httpStatus"})
public class ResourceRecommenderApiResponse implements Serializable {
    private String errorMessage;
    private HttpStatus httpStatus = HttpStatus.OK;
    // TODO - payload
    private List<RecommendedResource> payload;
}
