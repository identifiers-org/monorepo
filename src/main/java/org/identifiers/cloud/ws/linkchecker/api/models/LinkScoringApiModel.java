package org.identifiers.cloud.ws.linkchecker.api.models;

import org.identifiers.cloud.ws.linkchecker.api.responses.ServiceResponseScoringRequest;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Project: link-checker
 * Package: org.identifiers.cloud.ws.linkchecker.models
 * Timestamp: 2018-05-21 10:40
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Component
@Scope("prototype")
public class LinkScoringApiModel {

    public ServiceResponseScoringRequest getScoreForProvider() {
        // TODO
    }
}
