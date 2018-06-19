package org.identifiers.cloud.ws.linkchecker.api.responses;

import java.io.Serializable;

/**
 * Project: link-checker
 * Package: org.identifiers.cloud.ws.linkchecker.api.requests
 * Timestamp: 2018-05-26 10:23
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * Response to a Scoring request
 */
public class ServiceResponseScoringRequestPayload implements Serializable {
    // Default scoring
    private int score = 50;

    public int getScore() {
        return score;
    }

    public ServiceResponseScoringRequestPayload setScore(int score) {
        this.score = score;
        return this;
    }
}
