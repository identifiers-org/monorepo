package org.identifiers.cloud.ws.resolver.models;

import org.identifiers.cloud.commons.messages.models.ParsedCompactIdentifier;

public interface CompactIdParsingHelper {
    ParsedCompactIdentifier parseCompactIdRequest(String rawCompactIdentifier);
}
