package org.identifiers.cloud.ws.metadata.retrievers.ebisearch;
import org.identifiers.cloud.commons.messages.models.ParsedCompactIdentifier;
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
   private EBISearchRetriever      ebiSearchRetriever;

   @Test
   void testIsEnabled_ReturnsTrue_WhenConfigExists() {
      Assertions.assertTrue(ebiSearchRetriever.isEnabled(this.parsedCompactIdentifier));
   }

}
