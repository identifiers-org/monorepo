package org.identifiers.cloud.ws.resolver.daemons.models;

import org.identifiers.cloud.ws.resolver.data.models.PidEntry;

import java.util.List;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: resolver
 * Package: org.identifiers.cloud.ws.resolver.daemons.models
 * Timestamp: 2018-01-29 10:16
 * ---
 */
public class ResolverDataSourcerFromWs implements ResolverDataSourcer {
    @Override
    public List<PidEntry> getResolverData() throws ResolverDataSourcerException {
        return null;
    }
}
