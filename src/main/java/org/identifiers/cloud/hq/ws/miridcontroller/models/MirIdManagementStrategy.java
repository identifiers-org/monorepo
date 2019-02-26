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

    /**
     * Tell the MIR ID management subsystem that a particular MIR ID has been confirmed as 'still active'
     * @param id MIR ID
     * @throws MirIdManagementStrategyException
     */
    void keepAlive(long id) throws MirIdManagementStrategyException;

    /**
     * Tell the MIR ID management subsystem to load a particular MIR ID as 'active', i.e. 'in use' ID
     * @param id MIR ID
     * @throws MirIdManagementStrategyException
     */
    void loadId(long id) throws MirIdManagementStrategyException;
    void returnId(long id) throws MirIdManagementStrategyException;
}
