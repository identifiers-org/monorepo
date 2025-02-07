package org.identifiers.cloud.ws.metadata.retrievers.ebisearch;

import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.commons.messages.models.ParsedCompactIdentifier;
import org.identifiers.cloud.ws.metadata.configuration.EbiSearchRetrieverConfig;
import org.identifiers.cloud.ws.metadata.retrievers.MetadataRetriever;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Component
public class EBISearchRetriever implements MetadataRetriever {
   private final EbiSearchRetrieverConfig config;

   private final String       ebiSearchEndPoint;
   private final RestTemplate restTemplate;
   public EBISearchRetriever(EbiSearchRetrieverConfig config,
                             @Value("${org.identifiers.cloud.ws.metadata.retrivers.ebisearch.ebisearchendpoint}")
                             String ebiSearchEndPoint,
                             RestTemplateBuilder restTemplateBuilder) {
      this.config = config;
      this.ebiSearchEndPoint = ebiSearchEndPoint;
      this.restTemplate = restTemplateBuilder.build();
   }

   @Override
   public boolean isEnabled(ParsedCompactIdentifier compactIdentifier) {
      return config.hasMapping(compactIdentifier.getNamespace());
   }

   @Override
   public EbiSearchResult getRawMetaData(ParsedCompactIdentifier compactIdentifier) {

      String prefix = compactIdentifier.getNamespace();
      var domainId = config.getDomainId(prefix);
      var fields = config.getDomainFields(domainId);
      return this.getEbiSearchEntry(domainId, compactIdentifier.getLocalId(), fields);
   }

   @Override
   public MultiValueMap<String, String> getParsedMetaData(ParsedCompactIdentifier compactIdentifier) {

      var result = getBaseMap();
      String prefix = compactIdentifier.getNamespace();
      var domainId = config.getDomainId(prefix);
      var fields = config.getDomainFields(domainId);
      var searchResult = this.getEbiSearchEntry(domainId, compactIdentifier.getLocalId(), fields);

      var entries = searchResult.entries();
      if (entries != null && !entries.isEmpty()) {
         // This assumes an entry query that is only returns up to one result
         var resultFields = entries.get(0).fields();
         if (resultFields != null) {
            for (var fieldMapEntry : resultFields.entrySet()) {
               String fieldName = fieldMapEntry.getKey();
               List<String> values = fieldMapEntry.getValue();
               if (!CollectionUtils.isEmpty(values)) {
                  result.addAll(fieldName, values);
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

   public List<MediaType> getRawMediaType() {
      return List.of(MediaType.APPLICATION_JSON);
   }

   private EbiSearchResult getEbiSearchEntry(String domainName,
                                             String entryId,
                                             String fields) {
      var url = String.format("%s/%s/entry/%s?fields=%s",
                              this.ebiSearchEndPoint,
                              domainName, entryId, fields);
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);

      // Create HttpEntity with headers
      HttpEntity<String> entity = new HttpEntity<>(headers);

      // Make the GET request with headers
      return restTemplate.exchange(url, HttpMethod.GET, entity,
                                   EbiSearchResult.class).getBody();
   }

}
