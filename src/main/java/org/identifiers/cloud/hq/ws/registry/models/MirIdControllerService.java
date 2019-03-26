package org.identifiers.cloud.hq.ws.registry.models;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models
 * Timestamp: 2019-03-26 13:21
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This interface specifies the contract a delegate for MIR ID Services should fullfil
 */
public interface MirIdControllerService {

    /**
     * Mint a MIR ID.
     * @return the newly minted MIR ID, if anything happens, it will throw an exception
     * @throws MirIdControllerServiceException
     */
    String mintId() throws MirIdControllerServiceException;

    void keepAlive(String mirId) throws MirIdControllerServiceException;
}
