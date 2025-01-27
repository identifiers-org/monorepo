package org.identifiers.cloud.ws.metadata.retrievers.ebisearch;

import java.util.List;

public record EbiSearchResult(
      List<EbiSearchEntry> entries
) {
}
