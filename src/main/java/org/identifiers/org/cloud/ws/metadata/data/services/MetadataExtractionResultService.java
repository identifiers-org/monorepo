package org.identifiers.org.cloud.ws.metadata.data.services;

import org.identifiers.org.cloud.ws.metadata.data.repositories.MetadataExtractionResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Project: metadata
 * Package: org.identifiers.org.cloud.ws.metadata.data.services
 * Timestamp: 2018-09-16 20:33
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Component
public class MetadataExtractionResultService {
    // TODO
    @Autowired
    private MetadataExtractionResultRepository repository;
}
