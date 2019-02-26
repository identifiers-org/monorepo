package org.identifiers.cloud.hq.ws.miridcontroller.models;

/**
 * Project: mirid-controller
 * Package: org.identifiers.cloud.hq.ws.miridcontroller.models
 * Timestamp: 2019-02-26 12:47
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
public class DbBackedMirIdManagementStrategy implements MirIdManagementStrategy {
    @Override
    public long mintId() throws MirIdManagementStrategyException {
        // TODO
        return 0;
    }

    @Override
    public void keepAlive(long id) throws MirIdManagementStrategyException {
        // TODO
    }

    @Override
    public void loadId(long id) throws MirIdManagementStrategyException {
        // TODO
    }

    @Override
    public void returnId(long id) throws MirIdManagementStrategyException {
        // TODO
    }
}
