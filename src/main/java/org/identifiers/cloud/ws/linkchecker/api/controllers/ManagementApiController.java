package org.identifiers.cloud.ws.linkchecker.api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Project: link-checker
 * Package: org.identifiers.cloud.ws.linkchecker.api.controllers
 * Timestamp: 2018-07-31 11:13
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * The initial idea behind this API, is to expose endpoints that will allow clients to ask this service to perform tasks
 * like flushing the link checking historical data.
 */
@RestController
@RequestMapping("/management")
public class ManagementApiController {

    @RequestMapping("flushLinkCheckingHistory")
    public ResponseEntity<?> flushLinkCheckingHistory() {
        //ServiceResponseScoringRequest response = model.getScoreForUrl(request);
        //return new ResponseEntity<>(response, response.getHttpStatus());
        return new ResponseEntity<>("Flushing link checking history data", HttpStatus.OK);
    }
}
