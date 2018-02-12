package org.identifiers.org.cloud.ws.metadata.models;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
}