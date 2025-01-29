package org.identifiers.cloud.ws.linkchecker.api.controllers;

import lombok.RequiredArgsConstructor;
import org.identifiers.cloud.ws.linkchecker.api.models.LinkScoringApiModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.identifiers.cloud.commons.messages.requests.ServiceRequest;
import org.identifiers.cloud.commons.messages.responses.ServiceResponse;
import org.identifiers.cloud.commons.messages.requests.linkchecker.*;
import org.identifiers.cloud.commons.messages.responses.linkchecker.*;

/**
 * Project: link-checker
 * Package: org.identifiers.cloud.ws.linkchecker.controllers
 * Timestamp: 2018-05-21 10:40
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@RestController
@RequiredArgsConstructor
public class LinkScoringApiController {
    private final LinkScoringApiModel model;

    @PostMapping("/getScoreForUrl")
    public ResponseEntity<ServiceResponse<ServiceResponseScoringRequestPayload>>
    getScoreForUrl(@RequestBody ServiceRequest<ScoringRequestPayload> request) {
        var response = model.getScoreForUrl(request);
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }

    @PostMapping("/getScoreForResolvedId")
    public ResponseEntity<ServiceResponse<ServiceResponseScoringRequestPayload>>
    getScoreForResolvedId(@RequestBody ServiceRequest<ScoringRequestWithIdPayload> request) {
        var response = model.getScoreForResolvedId(request);
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }

    @PostMapping("/getScoreForProvider")
    public ResponseEntity<ServiceResponse<ServiceResponseScoringRequestPayload>>
    getScoreForProvider(@RequestBody ServiceRequest<ScoringRequestWithIdPayload> request) {
        var response = model.getScoreForProvider(request);
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }
}
