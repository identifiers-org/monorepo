package org.identifiers.cloud.hq.ws.miridcontroller.models;

import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.hq.ws.miridcontroller.data.models.ActiveMirId;
import org.identifiers.cloud.hq.ws.miridcontroller.data.models.MirIdDeactivationLogEntry;
import org.identifiers.cloud.hq.ws.miridcontroller.data.models.ReturnedMirId;
import org.identifiers.cloud.hq.ws.miridcontroller.data.repositories.ActiveMirIdRepository;
import org.identifiers.cloud.hq.ws.miridcontroller.data.repositories.MirIdDeactivationLogEntryRepository;
import org.identifiers.cloud.hq.ws.miridcontroller.data.repositories.ReturnedMirIdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
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
    @Retryable(label = "idMinting", maxAttempts = 24, backoff = @Backoff(delay = 200L))
    @Override
    public long mintId() throws MirIdManagementStrategyException {
        // TODO THIS BIT IS FAILING TO BE CONCURRENCY SAFE - A SOLUTION NEEDS TO BE PUT IN PLACE URGENTLY
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
        activeMirIdRepository.save(mintedId);
        return mintedId.getMirId();
    }

    @Transactional
    @Override
    public MirIdManagementStrategyOperationReport keepAlive(long id) throws MirIdManagementStrategyException {
        MirIdManagementStrategyOperationReport report = new MirIdManagementStrategyOperationReport().setStatus(MirIdManagementStrategyOperationReport.Status.SUCCESS);
        // Check if the ID is active
        // If active, update "last confirmed" date
        ActiveMirId activeMirId = activeMirIdRepository.findByMirId(id);
        if (activeMirId == null) {
            String msg = String.format("Keep alive MIR ID, %d, is [NOT ACTIVE], KEEPING ALIVE is NOT POSSIBLE", id);
            report.setStatus(MirIdManagementStrategyOperationReport.Status.BAD_REQUEST).setReportContent(msg);
            log.error(msg);
        } else {
            activeMirId.setLastConfirmed(new Date(System.currentTimeMillis()));
            activeMirIdRepository.save(activeMirId);
            String msg = String.format("KEEP ALIVE MIR ID, %d, minted on %s, confirmed on %s",
                    id, activeMirId.getCreated(), activeMirId.getLastConfirmed());
            report.setReportContent(msg);
            log.info(msg);
        }
        return report;
    }

    @Transactional
    @Override
    public MirIdManagementStrategyOperationReport loadId(long id) throws MirIdManagementStrategyException {
        MirIdManagementStrategyOperationReport report = new MirIdManagementStrategyOperationReport()
                .setStatus(MirIdManagementStrategyOperationReport.Status.SUCCESS);
        // Check the ID is not active
        ActiveMirId activeMirId = activeMirIdRepository.findByMirId(id);
        if (activeMirId != null) {
            String msg = String.format("Load MIR ID, %d, found to be ACTIVE since %s, last confirmed %s", id,
                    activeMirId.getCreated(), activeMirId.getLastConfirmed());
            log.error(msg);
            report.setStatus(MirIdManagementStrategyOperationReport.Status.BAD_REQUEST).setReportContent(msg);
            return report;
        }
        // Check it is not in 'returned' state
        ReturnedMirId returnedMirId = returnedMirIdRepository.findByMirId(id);
        if (returnedMirId != null) {
            String msg = String.format("Load MIR ID, %d, found to be in the POOL OF RETURNED IDs, CUSTOM MINTING of MIR IDs is NOT ALLOWED!", id);
            log.error(msg);
            report.setReportContent(msg).setStatus(MirIdManagementStrategyOperationReport.Status.BAD_REQUEST);
            return report;
        }
        // Load the ID
        Date now = new Date(System.currentTimeMillis());
        ActiveMirId newId = new ActiveMirId().setMirId(id).setCreated(now).setLastConfirmed(now);
        ActiveMirId registeredId = activeMirIdRepository.save(newId);
        String msg = String.format("Load MIR ID %d, on %s, last confirmed %s - COMPLETED",
                id,
                registeredId.getCreated(),
                registeredId.getLastConfirmed());
        log.info(msg);
        report.setReportContent(msg);
        return report;
    }

    @Transactional
    @Override
    public MirIdManagementStrategyOperationReport returnId(long id) throws MirIdManagementStrategyException {
        MirIdManagementStrategyOperationReport report = new MirIdManagementStrategyOperationReport()
                .setStatus(MirIdManagementStrategyOperationReport.Status.SUCCESS);
        // Check if the ID is active
        ActiveMirId activeMirId = activeMirIdRepository.findByMirId(id);
        if (activeMirId == null) {
            String msg = String.format("Return MIR ID, %d, NOT POSSIBLE for NON ACTIVE MIR IDs", id);
            log.error(msg);
            report.setStatus(MirIdManagementStrategyOperationReport.Status.BAD_REQUEST).setReportContent(msg);
            return report;
        }
        // Check if it already was returned
        ReturnedMirId returnedMirId = returnedMirIdRepository.findByMirId(id);
        if (returnedMirId != null) {
            String msg = String.format("Return MIR ID, %d, NOT POSSIBLE for ALREADY RETURNED IDs", id);
            log.error(msg);
            report.setStatus(MirIdManagementStrategyOperationReport.Status.BAD_REQUEST).setReportContent(msg);
            return report;
        }
        // Return the ID
        // remove ID from active IDs
        activeMirIdRepository.delete(activeMirId);
        // Put it in the pool of returned IDs
        returnedMirId = new ReturnedMirId().setMirId(id);
        returnedMirId = returnedMirIdRepository.save(returnedMirId);
        // Log the deactivation
        // log the return
        MirIdDeactivationLogEntry mirIdDeactivationLogEntry =
                new MirIdDeactivationLogEntry()
                        .setMirId(id)
                        .setMinted(activeMirId.getCreated())
                        .setLastConfirmed(activeMirId.getLastConfirmed());
        mirIdDeactivationLogEntryRepository.save(mirIdDeactivationLogEntry);
        String msg = String.format("RETURNED MIR ID, %d, on %s, minted on %s, and last confirmed on %s",
                id,
                returnedMirId.getCreated(),
                activeMirId.getCreated(),
                activeMirId.getLastConfirmed());
        log.info(msg);
        report.setReportContent(msg);
        return report;
    }
}
