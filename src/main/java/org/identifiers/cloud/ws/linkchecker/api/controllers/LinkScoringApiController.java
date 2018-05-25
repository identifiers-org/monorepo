package org.identifiers.cloud.ws.linkchecker.api.controllers;

import org.identifiers.cloud.ws.linkchecker.api.models.LinkScoringApiModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    @RequestMapping(value = "/getScoreForUrl", method = RequestMethod.POST)
    public ResponseEntity<?> getScoreForUrl() {
        // TODO
        return new ResponseEntity<>("getScoreForUrl()", HttpStatus.OK);
    }

    @RequestMapping(value = "/getScoreForResolvedId", method = RequestMethod.POST)
    public ResponseEntity<?> getScoreForResolvedId() {
        // TODO
        return new ResponseEntity<>("getScoreForResolvedId()", HttpStatus.OK);
    }

    @RequestMapping(value = "/getScoreForProvider", method = RequestMethod.POST)
    public ResponseEntity<?> getScoreForProvider() {
        // TODO
        return new ResponseEntity<>("getScoreForProvider()", HttpStatus.OK);
    }
}
