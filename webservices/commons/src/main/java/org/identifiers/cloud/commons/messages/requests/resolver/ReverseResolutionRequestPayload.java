package org.identifiers.cloud.commons.messages.requests.resolver;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.NOT_REQUIRED;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Conversion API payload")
public final class ReverseResolutionRequestPayload {
    @Schema(description = "Provider URL to be converted", requiredMode = REQUIRED,
            example = "http://purl.uniprot.org/uniprot/P0DP23")
    private String url;
    @Schema(description = "Accession of data object under URL, optional",
            requiredMode = NOT_REQUIRED, nullable = true, example = "P0DP23")
    private String accession = null;
    @Schema(hidden = true)
    private boolean forceUrls = false;
}
