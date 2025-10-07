package org.identifiers.cloud.ws.resolver.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.identifiers.cloud.commons.messages.responses.ServiceResponse;
import org.identifiers.cloud.commons.messages.responses.resolver.ResponseResolvePayload;
import org.identifiers.cloud.ws.resolver.services.MatomoTrackingService;
import org.identifiers.cloud.ws.resolver.api.models.ResolverApiModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;

import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;

@Tag(name = "CURIE resolution API", description = "Resolve provider URLs from compact identifiers")
@RestController
public class ResolverApiController {
    private static final Logger logger = LoggerFactory.getLogger(ResolverApiController.class);

    static class ProviderCompactIdTuple {
        String provider;
        String compactIdentifier;

        public String getProvider() {
            return provider;
        }

        public ProviderCompactIdTuple setProvider(String provider) {
            this.provider = provider;
            return this;
        }

        public String getCompactIdentifier() {
            return compactIdentifier;
        }

        public ProviderCompactIdTuple setCompactIdentifier(String compactIdentifier) {
            this.compactIdentifier = compactIdentifier;
            return this;
        }
    }

    @Autowired
    private ResolverApiModel resolverApiModel;

    @Autowired
    private MatomoTrackingService matomoTrackingService;



    // Compact Identifier and provider code helper
    @Deprecated
    private ProviderCompactIdTuple extractProviderAndCompactIdentifier(String resolutionRequest) {
        String provider = null;
        String compactIdentifier = null;
        if (resolutionRequest.contains((":"))) {
            // We divide based on ':' and first '/'
            String[] splitByColon = resolutionRequest.split(":");
            if (splitByColon[0].contains("/")) {
                provider = splitByColon[0].split("/")[0];
                compactIdentifier = resolutionRequest
                        .replaceFirst(provider, "")
                        .replaceFirst("/", "");
            } else {
                compactIdentifier = resolutionRequest;
            }
        } else if (resolutionRequest.contains("/")) {
            // We look for the first '/' to find the provider code
            provider = resolutionRequest.split("/")[0];
            compactIdentifier = compactIdentifier = resolutionRequest
                    .replaceFirst(provider, "")
                    .replaceFirst("/", "");
        } else {
            // This case is very likely to be a not valid Compact Identifier
            compactIdentifier = resolutionRequest;
        }
        logger.info("For resolution request '{}', found provider code '{}' and compact identifier '{}'",
                resolutionRequest, provider, compactIdentifier);
        return new ProviderCompactIdTuple().setCompactIdentifier(compactIdentifier).setProvider(provider);
    }

    @Operation(
            summary = "Resolve resources of a given provider code for a given curie",
            parameters = {
                    @Parameter(
                            name = "curie",
                            description = "Compact identifier in one the formats 'prefix:ID', 'prefix/ID', 'provider/prefix:ID', or 'provider/prefix/ID'",
                            example = "uniprot:P12345"
                    )
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "CURIE is valid and resources were found"),
            @ApiResponse(responseCode = "400", description = "CURIE is invalid"),
            @ApiResponse(responseCode = "404", description = "Provider code was not found for prefix given")
    })
    @GetMapping(value = "/{curie}/**")
    public ResponseEntity<ServiceResponse<ResponseResolvePayload>> resolve(
            @PathVariable String curie,
            HttpServletRequest request
    ) {
        final String path =
                request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString();
        logger.info("Resolution request, PATH '{}'", path);

        ServiceResponse<ResponseResolvePayload> result = resolverApiModel
                .resolveRawCompactId(path.replaceFirst("/", ""));

        matomoTrackingService.handleCidResolution(request, result);

        return ResponseEntity
                .status(result.getHttpStatus())
                .body(result);
    }

//    @Operation(summary = "Resolve resources for a given curie")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "CURIE is valid and resources were found"),
//            @ApiResponse(responseCode = "400", description = "CURIE is invalid")
//    })
//    @GetMapping("{curie}")
//    public ResponseEntity<ServiceResponse<ResponseResolvePayload>> resolve(
//            @Parameter(
//                    description = "Compact identifier in the format 'prefix:ID' or 'prefix/ID'",
//                    example = "uniprot:P12345"
//            )
//            @PathVariable String curie,
//            HttpServletRequest request
//    ) {
//        return resolve(null, curie, request);
//    }



    @Operation(
            summary = "Resolve namespace or resolver for given MIR ID",
            deprecated = true // still used by mir namespace
    )
    @ApiResponses({
            @ApiResponse(
                    description = "MIR ID valid and found", responseCode = "302",
                    headers = @Header(name = "Location",
                                      description = "URL for namespace or resource identified by MIR ID")
            ),
            @ApiResponse(description = "MIR ID invalid or not found", responseCode = "404")
    })
    @GetMapping(value = "/resolveMirId/{mirId}")
    public ResponseEntity<Void> resolveMirId(
            @Parameter(
                    description = "Legacy MIRIAM identifier",
                    example = "MIR:00000022"
            )
            @PathVariable String mirId
    ) {
        try {
            URI namespaceLocation = resolverApiModel.resolveMirId(mirId);
            if (namespaceLocation != null) {
                HttpHeaders headers = new HttpHeaders();
                headers.setLocation(namespaceLocation);
                return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (URISyntaxException e) {
            // Should never actually happen on production.
            // If so, check the WS_RESOLVER_CONFIG_REGISTRY_NAMESPACE_REDIRECT_FORMAT application property
            logger.error("Invalid URI format for MIRID resolution. {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
