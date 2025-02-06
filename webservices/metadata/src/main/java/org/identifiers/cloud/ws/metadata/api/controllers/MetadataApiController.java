package org.identifiers.cloud.ws.metadata.api.controllers;

import org.identifiers.cloud.commons.messages.requests.ServiceRequest;
import org.identifiers.cloud.commons.messages.requests.metadata.RequestFetchMetadataForUrlPayload;
import org.identifiers.cloud.ws.metadata.api.models.MetadataApiModel;
import org.identifiers.cloud.commons.messages.responses.ServiceResponse;
import org.identifiers.cloud.commons.messages.responses.metadata.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: metadata
 * Package: org.identifiers.cloud.ws.metadata.controllers
 * Timestamp: 2018-02-07 11:29
 * ---
 */
@RestController
public class MetadataApiController {

    private final MetadataApiModel model;
    public MetadataApiController(MetadataApiModel model) {
        this.model = model;
    }

    // Now this is always a RAW request
    @GetMapping(value = "/{identifierRequest}/**")
    public @ResponseBody
    ResponseEntity<ServiceResponse<ResponseFetchMetadataPayload>>
    getMetadataFor(@PathVariable String identifierRequest, HttpServletRequest request) {
        final String path = request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString();
        var response = model.getMetadataForRawRequest(path.replaceFirst("/", ""));
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }

    @PostMapping(value = "/getMetadataForUrl")
    public ResponseEntity<ServiceResponse<ResponseFetchMetadataPayload>>
    getMetadataForUrl(@RequestBody ServiceRequest<RequestFetchMetadataForUrlPayload> request) {
        var response = model.getMetadataForUrl(request);
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }
}
