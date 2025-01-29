package org.identifiers.cloud.commons.messages.models;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;


@Data
@Accessors(chain = true)
public class Recommendation implements Serializable {
    // This models a recommendation attached to a particular resource in the response from this web service
    private int recommendationIndex = 0;
    private String recommendationExplanation = "--- default explanation ---";
}
