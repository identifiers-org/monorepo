package org.identifiers.cloud.commons.messages.responses.resolver;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.identifiers.cloud.commons.messages.models.ParsedCompactIdentifier;
import org.identifiers.cloud.commons.messages.models.ResolvedResource;

import java.io.Serializable;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
public class ResponseResolvePayload implements Serializable {
    @Schema(nullable = true)
    private List<ResolvedResource>  resolvedResources;
    @Schema(nullable = true)
    private ParsedCompactIdentifier parsedCompactIdentifier;
}
