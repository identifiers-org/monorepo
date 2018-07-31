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
