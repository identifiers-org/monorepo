package org.identifiers.cloud.ws.resourcerecommender.models;

import java.util.List;

/**
 * Project: resource-recommender
 * Package: org.identifiers.cloud.ws.resourcerecommender.models
 * Timestamp: 2018-06-20 11:23
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This defines the interface of scoring function providers, different ways to implement how the scoring function is
 * built.
 */
public interface ScoringFunctionProvider {
    List<WeightedScore> getFunctionComponents() throws ScoringFunctionProviderException;
    // TODO - Add another method that provides an explanation on the components of the scoring function
}
