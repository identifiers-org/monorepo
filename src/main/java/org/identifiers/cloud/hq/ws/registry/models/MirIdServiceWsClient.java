package org.identifiers.cloud.hq.ws.registry.models;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models
 * Timestamp: 2019-03-26 14:22
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This implementation of MIR ID Service delegates the operations on identifiers.org MIR ID Controller API.
 */
public class MirIdServiceWsClient implements MirIdService {
    @Override
    public String mintId() throws MirIdServiceException {
        return null;
    }

    @Override
    public void keepAlive(String mirId) throws MirIdServiceException {

    }
}
