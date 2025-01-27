package org.identifiers.cloud.ws.resolver.api.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.identifiers.cloud.ws.resolver.models.ParsedCompactIdentifier;
import org.identifiers.cloud.ws.resolver.models.ResolvedResource;

import java.io.Serializable;
import java.util.List;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: libapi
 * Package: org.identifiers.cloud.ws.resolver.models.api.responses
 * Timestamp: 2018-03-07 7:40
 * ---
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
public class ResponseResolvePayload implements Serializable {
    private List<ResolvedResource> resolvedResources;
    private ParsedCompactIdentifier parsedCompactIdentifier;
}
