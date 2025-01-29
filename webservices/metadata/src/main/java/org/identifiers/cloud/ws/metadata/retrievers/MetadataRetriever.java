package org.identifiers.cloud.ws.metadata.retrievers;

import org.identifiers.cloud.commons.compactidparsing.ParsedCompactIdentifier;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Collections;
import java.util.List;

public interface MetadataRetriever {
   boolean isEnabled(ParsedCompactIdentifier compactIdentifier);

   Object getRawMetaData(ParsedCompactIdentifier compactIdentifier);

   /**
    * Returns a [label -> value] map that is easier to use by users. Label can be anything.
    * The returned map is meant to always expected to return an entry containing the key "retrieverId"
    * that is equal to the return from MetadataRetriever::getId().
    * This is a way for users to find the source of the data.
    * @param compactIdentifier Object contained parsed identifier collected from resolver API
    * @return [label -> value] map containing key "retrieverId"
    */
   MultiValueMap<String, String> getParsedMetaData(ParsedCompactIdentifier compactIdentifier);

   String getId();

   default List<MediaType> getRawMediaType() {
      return Collections.singletonList(MediaType.TEXT_PLAIN);
   }

   default MultiValueMap<String, String> getBaseMap() {
      var map = new LinkedMultiValueMap<String, String>();
      map.add("retrieverId", getId());
      return map;
   }
}
