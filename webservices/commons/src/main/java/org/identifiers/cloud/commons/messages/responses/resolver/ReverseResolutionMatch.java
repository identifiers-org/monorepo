package org.identifiers.cloud.commons.messages.responses.resolver;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReverseResolutionMatch {
   String prefix;
   String possibleIdorgUrl;
   String possibleIdorgCurie;
   boolean luiPatternMatch;
   float similarityScore;
}
