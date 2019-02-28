package org.identifiers.cloud.hq.ws.miridcontroller.models;

import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.hq.ws.miridcontroller.data.models.ActiveMirId;
import org.identifiers.cloud.hq.ws.miridcontroller.data.models.ReturnedMirId;
import org.identifiers.cloud.hq.ws.miridcontroller.data.repositories.ActiveMirIdRepository;
import org.identifiers.cloud.hq.ws.miridcontroller.data.repositories.MirIdDeactivationLogEntryRepository;
import org.identifiers.cloud.hq.ws.miridcontroller.data.repositories.ReturnedMirIdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Date;

/**
 * Project: mirid-controller
 * Package: org.identifiers.cloud.hq.ws.miridcontroller.models
 * Timestamp: 2019-02-26 12:47
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Component
@Slf4j
public class DbBackedMirIdManagementStrategy implements MirIdManagementStrategy {

    @Autowired
    private ActiveMirIdRepository activeMirIdRepository;
    @Autowired
    private MirIdDeactivationLogEntryRepository mirIdDeactivationLogEntryRepository;
    @Autowired
    private ReturnedMirIdRepository returnedMirIdRepository;

    @Transactional
    @Override
    public long mintId() throws MirIdManagementStrategyException {
        // Check if we can use one from the returned IDs
        Date now = new Date(System.currentTimeMillis());
        ReturnedMirId returnedMirId = returnedMirIdRepository.findTopByOrderByCreatedAsc();
        ActiveMirId mintedId = new ActiveMirId().setCreated(now).setLastConfirmed(now);
        if (returnedMirId != null) {
            mintedId.setMirId(returnedMirId.getMirId());
            returnedMirIdRepository.delete(returnedMirId);
            log.info(String.format("ID Minted on %s, REUSING returned ID, %d, returned on %s",
                    now, returnedMirId.getMirId(), returnedMirId.getCreated()));
        } else {
            // If not, mint a new one after the last one in use
            mintedId.setMirId(activeMirIdRepository.getMaxMirId() + 1L);
            log.info(String.format("ID Minted on %s, as a NEW ID %d - COMPLETED", now.toString(), mintedId.getMirId()));
        }
        // TODO
        activeMirIdRepository.save(mintedId);
        return mintedId.getMirId();
    }

    @Transactional
    @Override
    public void keepAlive(long id) throws MirIdManagementStrategyException {
        // TODO
    }

    @Transactional
    @Override
    public void loadId(long id) throws MirIdManagementStrategyException {
        // TODO
    }

    @Transactional
    @Override
    public void returnId(long id) throws MirIdManagementStrategyException {
        // TODO
    }
}
