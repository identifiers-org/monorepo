package org.identifiers.cloud.ws.resourcerecommender.models;

import org.springframework.stereotype.Component;

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
    @Override
    public List<WeightedScore> getFunctionComponents() throws ScoringFunctionProviderException {
        return null;
    }
}
