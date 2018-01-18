package org.identifiers.cloud.ws.resolver.daemons.models;

import org.identifiers.cloud.ws.resolver.data.models.PidEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

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

    @Value("${org.identifiers.cloud.ws.resolver.data.source.local.sample.file.name}")
    private String sampleDataFileName;

    @Override
    public List<PidEntry> getResolverData() throws ResolverDataSourcerException {
        ArrayList<PidEntry> result = new ArrayList<>();
        // TODO
        
        return result;
    }
}
