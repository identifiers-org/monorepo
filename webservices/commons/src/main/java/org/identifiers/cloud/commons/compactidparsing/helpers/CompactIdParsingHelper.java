package org.identifiers.cloud.commons.compactidparsing.helpers;

import org.identifiers.cloud.commons.compactidparsing.ParsedCompactIdentifier;

public interface CompactIdParsingHelper {
    ParsedCompactIdentifier parseCompactIdRequest(String rawCompactIdentifier);
}
