package org.identifiers.cloud.hq.ws.miridcontroller.models;

/**
 * Project: mirid-controller
 * Package: org.identifiers.cloud.hq.ws.miridcontroller.models
 * Timestamp: 2019-02-26 12:24
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This is the interface of any MIR ID Management strategy
 */
public interface MirIdManagementStrategy {
    /**
     * Mint a MIR ID
     * @return a newly minted MIR ID
     * @throws MirIdManagementStrategyException
     */
    long mintId() throws MirIdManagementStrategyException;

    void keepAlive(long id);
    void loadId(long id);
    void returnId(long id);
}
