package org.identifiers.cloud.ws.resolver.daemons.models;

import org.identifiers.cloud.ws.resolver.data.models.PidEntry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: resolver
 * Package: org.identifiers.cloud.ws.resolver.daemons.models
 * Timestamp: 2018-01-29 10:16
 * ---
 */
public class ResolverDataSourcerFromWs implements ResolverDataSourcer {
    @Value("${org.identifiers.cloud.ws.resolver.data.source.url}")
    private String resolverDataDumpWsEndpoint;

    @Override
    public List<PidEntry> getResolverData() throws ResolverDataSourcerException {
        List<PidEntry> result = new ArrayList<>();
        // TODO - Get this code into the re-try pattern
        RestTemplate restTemplate = new RestTemplate();
        result = Arrays.asList(restTemplate.getForObject(resolverDataDumpWsEndpoint, PidEntry[].class));
        return result;
    }
}
