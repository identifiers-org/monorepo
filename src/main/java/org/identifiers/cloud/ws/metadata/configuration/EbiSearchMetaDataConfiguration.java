package org.identifiers.cloud.ws.metadata.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class EbiSearchMetaDataConfiguration {
   private Map<String, Map<String,String>> config;
   public EbiSearchMetaDataConfiguration() {
      loadConfig();
   }

   private void loadConfig() {
      try {
         ObjectMapper objectMapper = new ObjectMapper();
         ClassPathResource resource = new ClassPathResource("ebisearchconfig.json");
         this.config = objectMapper.readValue(resource.getInputStream(), Map.class);
      } catch (IOException e) {
         throw new RuntimeException("Failed to load config.json", e);
      }
   }

   public Map<String,String> getConfig(String key) {
      return config.get(key);
   }

   public boolean hasConfig(String key) {
      return config.containsKey(key);
   }
}
