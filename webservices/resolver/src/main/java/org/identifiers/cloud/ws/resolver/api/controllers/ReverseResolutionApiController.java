package org.identifiers.cloud.ws.resolver.api.controllers;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import lombok.RequiredArgsConstructor;
import org.identifiers.cloud.commons.messages.responses.resolver.ReverseResolutionMatch;
import org.identifiers.cloud.commons.messages.requests.ServiceRequest;
import org.identifiers.cloud.commons.messages.requests.resolver.ReverseResolutionRequestPayload;
import org.identifiers.cloud.commons.messages.responses.ServiceResponse;
import org.identifiers.cloud.ws.resolver.services.reverseresolution.ReverseResolutionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("reverse")
public class ReverseResolutionApiController {
    private final ReverseResolutionService service;

    @PostMapping(value = "byPrefix", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<ServiceResponse<ReverseResolutionMatch>> prefixBasedReverseResolution(
            @RequestBody ServiceRequest<ReverseResolutionRequestPayload> data
    ) {
        var payload = data.getPayload();
        if (payload == null || isRequestInvalid(payload)) {
            log.debug("Invalid request payload {}", payload);
            ServiceResponse<ReverseResolutionMatch> serviceResponse =
                    ServiceResponse.ofError(HttpStatus.BAD_REQUEST,
                            "Invalid payload, HTTP url must be provided");
            return ResponseEntity.badRequest().body(serviceResponse);
        }
        var match = service.resolveBasedOnPrefix(payload);
        if (match.isPresent()) {
            ServiceResponse<ReverseResolutionMatch> serviceResponse =
                    ServiceResponse.of(match.get());
            return ResponseEntity.ok().body(serviceResponse);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping(value = "bySimilarity", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<ServiceResponse<List<ReverseResolutionMatch>>> similarityBasedReverseResolution(
            @RequestBody ServiceRequest<ReverseResolutionRequestPayload> data
    ) {
        var payload = data.getPayload();
        if (payload == null || isRequestInvalid(payload)) {
            log.debug("Invalid request payload {}", payload);
            ServiceResponse<List<ReverseResolutionMatch>> serviceResponse =
                    ServiceResponse.ofError(HttpStatus.BAD_REQUEST,
                            "Invalid payload, url must be provided");
            return ResponseEntity.badRequest().body(serviceResponse);
        }
        var matches = service.resolveBasedOnSimilarity(payload);
        if (matches.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            var response = ServiceResponse.of(matches);
            return ResponseEntity.ok().body(response);
        }
    }

    public boolean isRequestInvalid(ReverseResolutionRequestPayload payload) {
        String url = payload.getUrl();
        return StringUtils.isBlank(url) || !StringUtils.startsWithIgnoreCase(url, "http");
    }
}
