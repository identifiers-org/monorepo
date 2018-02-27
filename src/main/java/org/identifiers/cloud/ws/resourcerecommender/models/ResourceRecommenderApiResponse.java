package org.identifiers.cloud.ws.resourcerecommender.models;

import org.springframework.http.HttpStatus;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: resource-recommender
 * Package: org.identifiers.cloud.ws.resourcerecommender.models
 * Timestamp: 2018-02-27 11:33
 * ---
 */
public class ResourceRecommenderApiResponse {
    private String errorMessage;
    private HttpStatus httpStatus = HttpStatus.OK;
}
