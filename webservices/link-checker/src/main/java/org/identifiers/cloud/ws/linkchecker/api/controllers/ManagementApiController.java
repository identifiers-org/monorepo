package org.identifiers.cloud.ws.linkchecker.api.controllers;

import lombok.RequiredArgsConstructor;
import org.identifiers.cloud.ws.linkchecker.api.models.ManagementApiModel;
import org.identifiers.cloud.commons.messages.responses.linkchecker.ServiceResponseManagementRequestPayload;
import org.identifiers.cloud.commons.messages.responses.ServiceResponse;
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
@RequiredArgsConstructor
@RequestMapping("/management")
public class ManagementApiController {
    private final ManagementApiModel model;

    @RequestMapping("flushLinkCheckingHistory")
    public ResponseEntity<ServiceResponse<ServiceResponseManagementRequestPayload>> flushLinkCheckingHistory() {
        var response = model.flushLinkCheckingHistory();
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }
}
