package org.identifiers.cloud.ws.metadata.retrivers.ebisearch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.libapi.models.resolver.ParsedCompactIdentifier;
import org.identifiers.cloud.ws.metadata.configuration.EbiSearchMetaDataConfiguration;
import org.identifiers.cloud.ws.metadata.retrivers.ApiBasedMetaDataRetriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.*;

import static org.eclipse.rdf4j.query.resultio.TupleQueryResultFormat.JSON;

@Slf4j
@Component
public class EBISearchRetriver extends ApiBasedMetaDataRetriver {

   @Autowired
   private EbiSearchMetaDataConfiguration config;

   final String ebiSearchEndPoint;

   public EBISearchRetriver(@Value("${org.identifiers.cloud.ws.metadata.retrivers.ebisearch.ebisearchendpoint}")
                            String ebiSearchEndPoint) {
      this.ebiSearchEndPoint = ebiSearchEndPoint;
   }

   @Override
   public boolean isEnabled(ParsedCompactIdentifier compactIdentifier) {
      return config.hasConfig(compactIdentifier.getNamespace());
   }

   @Override
   public Object getRawMetaData(ParsedCompactIdentifier compactIdentifier) {

      Object searchResult = this.searchEntry(compactIdentifier, config.getConfig(compactIdentifier.getNamespace()));
      try {
         ObjectMapper objectMapper = new ObjectMapper();
         return objectMapper.writeValueAsString(searchResult);
      } catch (JsonProcessingException ex) {
         log.error("Json Parsing exception ", ex.getMessage());
         return null;
      }
   }

   @Override
   public Map<String, String> getParsedMetaData(ParsedCompactIdentifier compactIdentifier) {

      HashMap<String, String> result = new HashMap<>();
      Map searchResult = this.searchEntry(compactIdentifier, config.getConfig(compactIdentifier.getNamespace()));
      List<Map<String, Object>> entries = (List<Map<String, Object>>) searchResult.get("entries");
      if (entries != null && !entries.isEmpty()) {
         Map<String, Object> fields = (Map<String, Object>) entries.get(0).get("fields");
         if (fields != null) {
            for (String fieldName : fields.keySet()) {
               Object value = fields.get(fieldName);
               if (value instanceof List && !((List<?>) value).isEmpty()) {
                  result.put(fieldName, ((List<?>) value).get(0).toString());
               }
               else {
                  result.put(fieldName, value.toString());
               }
            }
         }
      }
      return result;
   }

   @Override
   public String getId() {
      return "ebisearch";
   }

   private Map searchEntry(ParsedCompactIdentifier compactIdentifier, Map<String, String> domainConfiguration) {

      StringBuilder url = new StringBuilder(this.ebiSearchEndPoint);
      url.append("/ws/rest/").append(domainConfiguration.get("domain_name")).append("/entry/");
      url.append(compactIdentifier.getLocalId()).append("?fields=").append(domainConfiguration.get("fields"));
      return this.runRequest(url.toString());

   }

   public List<MediaType> getRawMediaType() {
      return MediaType.parseMediaTypes(JSON.getMIMETypes());
   }
}
