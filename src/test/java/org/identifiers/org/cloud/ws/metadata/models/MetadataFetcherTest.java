package org.identifiers.org.cloud.ws.metadata.models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: metadata
 * Package: org.identifiers.org.cloud.ws.metadata.models
 * Timestamp: 2018-02-12 13:28
 * ---
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MetadataFetcherTest {

    @Autowired
    private MetadataFetcher metadataFetcher;

    @Test
    public void testValidMetadata() {
        // NOTE - Possible future extension here to make it more exhaustive
        // This unit test is too dependant on that particular URL to be up, as soon as this is working, I may
        // choose to deactivate this test
        String url = "https://reactome.org/content/detail/R-HSA-177929";
        // TODO - The following URL uses Javascript to setup the metadata information, and it doesn't work
        //String url = "https://www.omicsdi.org/dataset/arrayexpress-repository/E-GEOD-37196";
        String metadata = metadataFetcher.fetchMetadataFor(url);
        assertThat(String.format("URL '%s' contains VALID metadata", url), metadata.isEmpty(), is(false));
    }
}