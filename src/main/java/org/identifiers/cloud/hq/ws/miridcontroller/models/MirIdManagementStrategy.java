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
     * @return the given MIR ID if the request was completed successfully or 'null' if it was a bad request, e.g. the
     * MIR ID is not active, thus, it cannot be kept alive
     * @throws MirIdManagementStrategyException
     */
    long keepAlive(long id) throws MirIdManagementStrategyException;

    /**
     * Tell the MIR ID management subsystem to load a particular MIR ID as 'active', i.e. 'in use' ID
     * @param id MIR ID
     * @return the given MIR ID if the request was completed successfully or 'null' if it was a bad request, e.g. the
     * MIR ID to be loaded was already active.
     * @throws MirIdManagementStrategyException
     */
    long loadId(long id) throws MirIdManagementStrategyException;

    /**
     * Request deactivation of a given MIR ID to the MIR ID management subsystem.
     * @param id MIR ID to be deactivated
     * @return the given MIR ID if the request was completed successfully or 'null' if it was a bad request, e.g. the
     * given ID was not active, thus it cannot be returned
     * @throws MirIdManagementStrategyException
     */
    long returnId(long id) throws MirIdManagementStrategyException;
}
