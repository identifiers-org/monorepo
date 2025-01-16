package org.identifiers.cloud.ws.metadata.api.responses;

import lombok.Data;
import lombok.experimental.Accessors;
import org.identifiers.cloud.libapi.models.resolver.ParsedCompactIdentifier;

import java.io.Serializable;
import java.net.URL;
import java.util.List;

/**
 * @author Renato Caminha Juacaba Neto <rjuacaba@ebi.ac.uk>
 * Project: metadata
 * Package: org.identifiers.cloud.ws.metadata.models.api.responses
 * Timestamp: 2024-12-11 08:32
 * ---
 */
@Data @Accessors(chain = true)
public class ResponseRetrieverListPayload implements Serializable {
    ParsedCompactIdentifier parsedCompactIdentifier;
    List<String> ableRetrievers;
}
