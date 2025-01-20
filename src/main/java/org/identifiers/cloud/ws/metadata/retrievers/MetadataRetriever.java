package org.identifiers.cloud.ws.metadata.retrievers;

import org.identifiers.cloud.libapi.models.resolver.ParsedCompactIdentifier;
import org.springframework.http.MediaType;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public interface MetadataRetriever {
   boolean isEnabled(ParsedCompactIdentifier compactIdentifier);

   Object getRawMetaData(ParsedCompactIdentifier compactIdentifier);

   Map<String, String> getParsedMetaData(ParsedCompactIdentifier compactIdentifier);

   String getId();

   default List<MediaType> getRawMediaType() {
      return Collections.singletonList(MediaType.TEXT_PLAIN);
   }
}
