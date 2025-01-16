package org.identifiers.cloud.ws.metadata.retrivers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpEntity;

import java.util.Map;


@Slf4j
public abstract class ApiBasedMetaDataRetriver implements MetadataRetriver {

   private final RestTemplate restTemplate;

   public ApiBasedMetaDataRetriver() {
      this.restTemplate = new RestTemplate();
   }
   protected Map runRequest(String url) {
      HttpHeaders headers = new HttpHeaders();
      headers.set("Content-Type", "application/json"); // Or whatever Content-Type you need

      // Create HttpEntity with headers
      HttpEntity<String> entity = new HttpEntity<>(headers);

      // Make the GET request with headers
      RestTemplate restTemplate = new RestTemplate();
      ResponseEntity<Map> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            entity,
            Map.class
      );

      Map responseBody = response.getBody();
      return responseBody;
   }

}
