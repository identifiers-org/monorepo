package org.identifiers.cloud.ws.metadata.api.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.libapi.services.ResolverService;
import org.identifiers.cloud.ws.metadata.api.models.MetadataRetrieverApiModel;
import org.identifiers.cloud.ws.metadata.api.responses.ResponseRetrieverListPayload;
import org.identifiers.cloud.ws.metadata.api.responses.ServiceResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author Renato Caminha Juacaba Neto <rjuacaba@ebi.ac.uk>
 * Project: metadata
 * Package: org.identifiers.cloud.ws.metadata.controllers
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/retrievers")
public class MetadataRetrieverApiController {
    private final MetadataRetrieverApiModel model;
    private final ResolverService resolverService;


    @GetMapping(value = "/{curie:.*}")
    public ResponseEntity<ServiceResponse<ResponseRetrieverListPayload>>
                getRetrieversFor(@PathVariable String curie,
                                 HttpServletRequest request) {
        var resolverResponse = resolverService.requestResolutionRawRequest(curie);

        ServiceResponse<ResponseRetrieverListPayload> response;
        if (resolverResponse.getHttpStatus().is2xxSuccessful()) {
            var parsedCurie = resolverResponse.getPayload().getParsedCompactIdentifier();
            List<String> ableRetrievers = model
                    .getListOfEnabledRetrieverEndpoints(request, parsedCurie);

            var payload = new ResponseRetrieverListPayload()
                    .setAbleRetrievers(ableRetrievers)
                    .setParsedCompactIdentifier(parsedCurie);
            response = ServiceResponse.of(payload);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response = ServiceResponse.ofError(
                    HttpStatus.BAD_REQUEST,
                    resolverResponse.getErrorMessage()
            );
            return ResponseEntity.badRequest().body(response);
        }
    }


    @GetMapping(value = "/{retrieverId}/{curie:.+}")
    public ResponseEntity<?>
                getRetrieverParsedMetadata(@PathVariable String retrieverId,
                                           @PathVariable String curie) {
        var resolverResponse = resolverService.requestResolutionRawRequest(curie);
        if (resolverResponse.getHttpStatus().is2xxSuccessful()) {
            var pci = resolverResponse.getPayload().getParsedCompactIdentifier();
            var retriever = model.getRetrieverFor(pci, retrieverId);
            if (retriever.isPresent()) {
                return ResponseEntity.ok().body(
                        retriever.get().getParsedMetaData(pci)
                );
            }
        }
        var body = ServiceResponse.ofError(
                HttpStatus.BAD_REQUEST,
                "Retriever not found or not enabled for this curie");
        return ResponseEntity.badRequest().body(body);
    }


    @GetMapping(value = "/{retrieverId}/raw/{curie:.+}")
    public ResponseEntity<?> getRetrieverRawMetadata(@PathVariable String retrieverId,
                                                     @PathVariable String curie,
                                                     @RequestHeader String accept) {
        var resolverResponse = resolverService.requestResolutionRawRequest(curie);
        if (resolverResponse.getHttpStatus().is2xxSuccessful()) {
            var pci = resolverResponse.getPayload().getParsedCompactIdentifier();
            var retriever = model.getRetrieverFor(pci, retrieverId);
            if (retriever.isPresent()) {
                var responseDatatype = model.getMediatypeForResponse(retriever.get(), accept);
                if (responseDatatype.isPresent()) {
                    return ResponseEntity.ok()
                            .contentType(responseDatatype.get())
                            .body(retriever.get().getRawMetaData(pci));
                } else {
                    var rawMediaTypes = retriever.get().getRawMediaType();
                    return ResponseEntity
                            .status(HttpStatus.NOT_ACCEPTABLE)
                            .contentType(MediaType.TEXT_PLAIN)
                            .body("Raw type for this retriever are " + rawMediaTypes);
                }
            }
        }
        var body = ServiceResponse.ofError(
                HttpStatus.BAD_REQUEST,
                "Retriever not found or not enabled for this curie");
        return ResponseEntity.badRequest().body(body);
    }
}
