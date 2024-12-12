package org.identifiers.cloud.ws.metadata.retrivers.ebisearch;
import org.identifiers.cloud.libapi.models.resolver.ParsedCompactIdentifier;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;

@SpringBootTest
public class EbiSearchMetadataRetrieverTest {


   final ParsedCompactIdentifier parsedCompactIdentifier =
         new ParsedCompactIdentifier()
               .setNamespace("uniprot")
               .setLocalId("P12345")
               .setRawRequest("uniprot:P12345");
   @Autowired
   private EBISearchRetriver ebiSearchRetriver;

   @Test
   void testIsEnabled_ReturnsTrue_WhenConfigExists() {
      Assertions.assertTrue(ebiSearchRetriver.isEnabled(this.parsedCompactIdentifier));
   }

}
