package org.identifiers.cloud.ws.resourcerecommender.models;

/**
 * Project: resource-recommender
 * Package: org.identifiers.cloud.ws.resourcerecommender.models
 * Timestamp: 2018-06-20 9:11
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This models the contract for any metric point provider. A metric point is a measure [0, 100] on a particular
 * dimension of a given resovled resource.
 */
public interface SubscoreProvider {

    int getMetricPointForResource(ResolvedResource resolvedResource);

    int getMetricPointForProvider(ResolvedResource resolvedResource);
}
