package org.identifiers.cloud.ws.resolver.models;

import org.identifiers.cloud.commons.compactidparsing.ParsedCompactIdentifier;

public interface CompactIdParsingHelper {
    ParsedCompactIdentifier parseCompactIdRequest(String rawCompactIdentifier);
}
