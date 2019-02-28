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

    // NOTE - I know that using the return value as a mechanism to inform the client of possible situations that may
    // have happened is not a good idea, and this should have been done via a different mechanism, e.g. using different
    // exceptions, but then, the client code would have been using try - catch as if - else, so a return object with
    // different return statuses should have been modeled here to report back to the calling client. For this iteration
    // of the microservice we don't need that much information, so it is ok doing it this way, despite being this an
    // interface and future changes potentially affecting implementations, the thing is the scope of the consequences is
    // very little, and the need for extended information on 'bad requests' situations is not clear at this point.
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
