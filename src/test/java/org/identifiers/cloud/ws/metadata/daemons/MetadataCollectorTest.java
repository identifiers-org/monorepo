package org.identifiers.cloud.ws.metadata.daemons;

import junit.framework.TestCase;
import org.identifiers.cloud.ws.metadata.data.models.MetadataExtractionRequest;
import org.identifiers.cloud.ws.metadata.data.models.MetadataExtractionResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.time.Instant;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MetadataCollectorTest extends TestCase {
    @Autowired
    MetadataCollector metadataCollector;

    @Test
    public void testAttendMetadataExtractionRequestWithValidUrl() {
        MetadataExtractionRequest request = new MetadataExtractionRequest();
        request.setAccessUrl("http://localhost:8082/page_with_metadata.html");
        request.setResolutionPath("resolution path");
        request.setTimestamp(Timestamp.from(Instant.now()));
        request.setResourceId("resource id");
        MetadataExtractionResult res = metadataCollector.attendMetadataExtractionRequest(request);
        assertTrue(HttpStatus.valueOf(res.getHttpStatus()).is2xxSuccessful());
    }
}