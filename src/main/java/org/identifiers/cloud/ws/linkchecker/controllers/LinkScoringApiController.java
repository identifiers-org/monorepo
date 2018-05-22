package org.identifiers.cloud.ws.linkchecker.controllers;

import org.identifiers.cloud.ws.linkchecker.models.LinkScoringApiModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * Project: link-checker
 * Package: org.identifiers.cloud.ws.linkchecker.controllers
 * Timestamp: 2018-05-21 10:40
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@RestController
public class LinkScoringApiController {
    @Autowired
    private LinkScoringApiModel model;

    public ResponseEntity<?> getScoreForUrl() {
        // TODO
    }
}
