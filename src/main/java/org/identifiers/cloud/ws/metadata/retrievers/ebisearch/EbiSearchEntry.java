package org.identifiers.cloud.ws.metadata.retrievers.ebisearch;

import java.util.List;
import java.util.Map;

public record EbiSearchEntry(
      String id,
      String source,
      Map<String, List<String>> fields
) {
}
