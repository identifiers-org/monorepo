package org.identifiers.cloud.ws.metadata.retrivers;

import org.identifiers.cloud.libapi.models.resolver.ParsedCompactIdentifier;
import org.springframework.http.MediaType;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public interface MetadataRetriver {
   public boolean isEnabled(ParsedCompactIdentifier compactIdentifier);

   public Object getRawMetaData(ParsedCompactIdentifier compactIdentifier);

   public Map<String,String> getParsedMetaData(ParsedCompactIdentifier compactIdentifier);

    String getId();

    default List<MediaType> getRawMediaType() {
        return Collections.singletonList(MediaType.TEXT_PLAIN);
    }
}
