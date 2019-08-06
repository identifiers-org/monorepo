package org.identifiers.cloud.ws.resourcerecommender.api.data.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: resource-recommender
 * Package: org.identifiers.cloud.ws.resourcerecommender.models
 * Timestamp: 2018-02-27 11:06
 * ---
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResolvedResource implements Serializable {
    // Even if we have access to another service that, given a resource ID, could provide information on that resource,
    // these particular attributes exist within the context that resource / provider for a particular Compact ID. In the
    // future, we could include more context information related to the particularities of the current recommendation
    // request to fine tune the recommendation mechanism

    // This field references the ID of the resource within the context of the current Compact ID resolved request
    private String id;
    // This field references the final URL that points to the current resolved resource request
    private String accessURL;
    // For this particular resolved resource request, provides information on whether the resource is official or not
    private boolean official;
    // TODO - This field represents the Home URL for the given provider

}
