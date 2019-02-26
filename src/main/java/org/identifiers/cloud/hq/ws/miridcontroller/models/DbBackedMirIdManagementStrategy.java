package org.identifiers.cloud.hq.ws.miridcontroller.models;

import org.identifiers.cloud.hq.ws.miridcontroller.data.repositories.ActiveMirIdRepository;
import org.identifiers.cloud.hq.ws.miridcontroller.data.repositories.MirIdDeactivationLogEntryRepository;
import org.identifiers.cloud.hq.ws.miridcontroller.data.repositories.ReturnedMirIdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Project: mirid-controller
 * Package: org.identifiers.cloud.hq.ws.miridcontroller.models
 * Timestamp: 2019-02-26 12:47
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Component
public class DbBackedMirIdManagementStrategy implements MirIdManagementStrategy {

    @Autowired
    private ActiveMirIdRepository activeMirIdRepository;
    @Autowired
    private MirIdDeactivationLogEntryRepository mirIdDeactivationLogEntryRepository;
    @Autowired
    private ReturnedMirIdRepository returnedMirIdRepository;

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
