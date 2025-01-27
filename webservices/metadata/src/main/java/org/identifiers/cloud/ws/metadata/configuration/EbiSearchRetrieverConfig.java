package org.identifiers.cloud.ws.metadata.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Data
@Configuration
@ConfigurationProperties("ebi-search-retriever")
public class EbiSearchRetrieverConfig {
   Map<String, String> namespaceMappings = new HashMap<>();
   Map<String, String> domainFields = new HashMap<>();

   public String getDomainId(String namespacePrefix) {
      return namespaceMappings.get(namespacePrefix);
   }

   public String getDomainFields(String domainId) {
      return domainFields.get(domainId);
   }

   public boolean hasMapping(String namespacePrefix) {
      return namespaceMappings.containsKey(namespacePrefix);
   }
}
