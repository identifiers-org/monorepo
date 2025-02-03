package org.identifiers.cloud.ws.metadata.configuration;

import org.identifiers.cloud.ws.metadata.TestRedisServer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {TestRedisServer.class})
class EbiSearchRetrieverConfigTest {
   @Autowired
   EbiSearchRetrieverConfig config;

   @Test
   void checkIfMappingsWereLoaded() {
      var mappings = config.getNamespaceMappings();
      assertNotNull(mappings);
      assertFalse(mappings.isEmpty());
      assertTrue(mappings.containsKey("uniprot"));
   }
}