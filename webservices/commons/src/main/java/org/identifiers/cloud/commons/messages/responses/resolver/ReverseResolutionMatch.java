package org.identifiers.cloud.commons.messages.responses.resolver;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReverseResolutionMatch {
   @Schema(description = "Prefix of matching namespace")
   String prefix;
   @Schema(description = "Tentative identifiers.org URI for URL", nullable = true)
   String possibleIdorgUrl;
   @Schema(description = "Tentative compact identifier for URL")
   String possibleIdorgCurie;
   @Schema(description = "Flag indicating if ID matches the namespace ID pattern")
   boolean luiPatternMatch;
   @Schema(description = "Score from 0 to 100 indicating how similar the resolved URL is to the query URL")
   float similarityScore;
}
