package org.identifiers.cloud.ws.resolver.daemons.models;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.identifiers.cloud.ws.resolver.data.models.PidEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: resolver
 * Package: org.identifiers.cloud.ws.resolver.daemons.models
 * Timestamp: 2018-01-18 10:06
 * ---
 */
public class ResolverDataSourcerFromSampleFile implements ResolverDataSourcer {
    private Logger logger = LoggerFactory.getLogger(ResolverDataSourcerFromSampleFile.class);

    @Value("${org.identifiers.cloud.ws.resolver.data.source.file.local.path}")
    private String sampleDataFileLocalPath;

    @Override
    public List<PidEntry> getResolverData() throws ResolverDataSourcerException {
        List<PidEntry> result = new ArrayList<>();
        // TODO
        try {
            File dataFile = new ClassPathResource(sampleDataFileLocalPath).getFile();
            logger.info("Loading resolver data sample from '{}'", dataFile.getAbsolutePath());
            ObjectMapper objectMapper = new ObjectMapper();
            result = objectMapper.readValue(dataFile, new TypeReference<List<PidEntry>>(){});
        } catch (IOException e) {
            logger.error("There was a problem reading the file at '{}' -> '{}'", sampleDataFileLocalPath, e.getMessage());
        }
        return result;
    }
}
