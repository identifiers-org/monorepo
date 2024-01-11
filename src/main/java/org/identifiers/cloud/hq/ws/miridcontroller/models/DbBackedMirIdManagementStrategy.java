package org.identifiers.cloud.hq.ws.miridcontroller.models;

import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.hq.ws.miridcontroller.data.models.ActiveMirId;
import org.identifiers.cloud.hq.ws.miridcontroller.data.models.MirIdDeactivationLogEntry;
import org.identifiers.cloud.hq.ws.miridcontroller.data.models.ReturnedMirId;
import org.identifiers.cloud.hq.ws.miridcontroller.data.repositories.ActiveMirIdRepository;
import org.identifiers.cloud.hq.ws.miridcontroller.data.repositories.MirIdDeactivationLogEntryRepository;
import org.identifiers.cloud.hq.ws.miridcontroller.data.repositories.ReturnedMirIdRepository;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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
    // Defaults
    private static final String CONCURRENCY_LOCK_OPERATION_ID_MINTING = "DbBackedMirIdManagementStrategy_concurrency_lock_operation_id_minting";
    private static final int CONCURRENCY_LOCK_OPERATION_ID_MINTING_TIME_SECONDS_LEASE_TIME = 3;
    private static final int CONCURRENCY_LOCK_OPERATION_ID_MINTING_TIME_SECONDS_WAIT_FOR_LOCK_TIME = 6;
    private static final String CONCURRENCY_LOCK_OPERATION_KEEP_ALIVE = "DbBackedMirIdManagementStrategy_concurrency_lock_operation_keep_alive";
    private static final int CONCURRENCY_LOCK_OPERATION_KEEP_ALIVE_TIME_SECONDS_LEASE_TIME = 3;
    private static final int CONCURRENCY_LOCK_OPERATION_KEEP_ALIVE_TIME_SECONDS_WAIT_FOR_LOCK_TIME = 6;
    private static final String CONCURRENCY_LOCK_OPERATION_LOAD_ID = "DbBackedMirIdManagementStrategy_concurrency_lock_operation_load_id";
    private static final int CONCURRENCY_LOCK_OPERATION_LOAD_ID_TIME_SECONDS_LEASE_TIME = 3;
    private static final int CONCURRENCY_LOCK_OPERATION_LOAD_ID_TIME_SECONDS_WAIT_FOR_LOCK_TIME = 6;
    private static final String CONCURRENCY_LOCK_OPERATION_RETURN_ID = "DbBackedMirIdManagementStrategy_concurrency_lock_operation_return_id";
    private static final int CONCURRENCY_LOCK_OPERATION_RETURN_ID_TIME_SECONDS_LEASE_TIME = 3;
    private static final int CONCURRENCY_LOCK_OPERATION_RETURN_ID_TIME_SECONDS_WAIT_FOR_LOCK_TIME = 6;

    // Repositories
    @Autowired
    private ActiveMirIdRepository activeMirIdRepository;
    @Autowired
    private MirIdDeactivationLogEntryRepository mirIdDeactivationLogEntryRepository;
    @Autowired
    private ReturnedMirIdRepository returnedMirIdRepository;

    // Concurrency
    @Autowired
    private RedissonClient redissonClient;

    // Persistence context
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public long mintId() throws MirIdManagementStrategyException {
        // Lock Acquisition
        RLock operationLock = redissonClient.getLock(CONCURRENCY_LOCK_OPERATION_ID_MINTING);
        try {
            if (!operationLock.tryLock(CONCURRENCY_LOCK_OPERATION_ID_MINTING_TIME_SECONDS_WAIT_FOR_LOCK_TIME,
                    CONCURRENCY_LOCK_OPERATION_ID_MINTING_TIME_SECONDS_LEASE_TIME,
                    TimeUnit.SECONDS)) {
                throw new MirIdManagementStrategyException("LOCK ACQUISITION TIMED OUT while minting MIR ID");
            }
        } catch (InterruptedException e) {
            throw new MirIdManagementStrategyException(String.format("LOCK ACQUISITION ERROR while minting MIR ID, '%s'", e.getMessage()));
        }
        // ID Minting
        try {
            // Check if we can use one from the returned IDs
            Date now = new Date(System.currentTimeMillis());
            ReturnedMirId returnedMirId = returnedMirIdRepository.findTopByOrderByCreatedAsc();
            ActiveMirId mintedId = new ActiveMirId().setCreated(now).setLastConfirmed(now);
            String logMessage = "";
            if (returnedMirId != null) {
                mintedId.setMirId(returnedMirId.getMirId());
                returnedMirIdRepository.delete(returnedMirId);
                logMessage = String.format("ID Minted on %s, REUSING returned ID, %d, returned on %s",
                        now, returnedMirId.getMirId(), returnedMirId.getCreated());
            } else {
                // If not, mint a new one after the last one in use
                mintedId.setMirId(activeMirIdRepository.getMaxMirId() + 1L);
                logMessage = String.format("ID Minted on %s, as a NEW ID %d", now.toString(), mintedId.getMirId());
            }
            activeMirIdRepository.save(mintedId);
            // Apparently, the Entity Manager cache is a troublemaker for this particular operation
            entityManager.flush();
            entityManager.getEntityManagerFactory().getCache().evictAll();
            log.info(logMessage);
            return mintedId.getMirId();
        } catch (RuntimeException e) {
            throw new MirIdManagementStrategyException(e.getMessage());
        } finally {
            operationLock.unlock();
        }
    }

    @Transactional
    @Override
    public MirIdManagementStrategyOperationReport keepAlive(long id) throws MirIdManagementStrategyException {
        // Lock Acquisition
        RLock operationLock = redissonClient.getLock(CONCURRENCY_LOCK_OPERATION_KEEP_ALIVE);
        try {
            if (!operationLock.tryLock(CONCURRENCY_LOCK_OPERATION_KEEP_ALIVE_TIME_SECONDS_WAIT_FOR_LOCK_TIME,
                    CONCURRENCY_LOCK_OPERATION_KEEP_ALIVE_TIME_SECONDS_LEASE_TIME,
                    TimeUnit.SECONDS)) {
                throw new MirIdManagementStrategyException("LOCK ACQUISITION TIMED OUT while keeping a MIR ID alive");
            }
        } catch (InterruptedException e) {
            throw new MirIdManagementStrategyException(String.format("LOCK ACQUISITION INTERRUPTED while keeping a MIR ID alive, '%s'", e.getMessage()));
        }
        // Operation - Keep Alive
        try {
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
                // Apparently, the Entity Manager cache is a troublemaker for this particular operation
                entityManager.flush();
                entityManager.getEntityManagerFactory().getCache().evictAll();
                String msg = String.format("KEEP ALIVE MIR ID, %d, minted on %s, confirmed on %s",
                        id, activeMirId.getCreated(), activeMirId.getLastConfirmed());
                report.setReportContent(msg);
                log.info(msg);
            }
            return report;
        } catch (RuntimeException e) {
            throw new MirIdManagementStrategyException(e.getMessage());
        } finally {
            operationLock.unlock();
        }

    }

    @Transactional
    @Override
    public MirIdManagementStrategyOperationReport loadId(long id) throws MirIdManagementStrategyException {
        // Lock Acquisition
        RLock operationLock = redissonClient.getLock(CONCURRENCY_LOCK_OPERATION_LOAD_ID);
        try {
            if (!operationLock.tryLock(CONCURRENCY_LOCK_OPERATION_LOAD_ID_TIME_SECONDS_WAIT_FOR_LOCK_TIME,
                    CONCURRENCY_LOCK_OPERATION_LOAD_ID_TIME_SECONDS_LEASE_TIME,
                    TimeUnit.SECONDS)) {
                throw new MirIdManagementStrategyException(String.format("LOCK ACQUISITION TIMED OUT while loading MIR ID '%s'", MirIdHelper.prettyPrintMirId(id)));
            }
        } catch (InterruptedException e) {
            throw new MirIdManagementStrategyException(String.format("LOCK ACQUISITION INTERRUPTED while loading MIR ID '%s', '%s'", MirIdHelper.prettyPrintMirId(id), e.getMessage()));
        }
        // Operation - Load ID
        try {
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
            // Apparently, the Entity Manager cache is a troublemaker for this particular operation
            entityManager.flush();
            entityManager.getEntityManagerFactory().getCache().evictAll();
            String msg = String.format("Load MIR ID %d, on %s, last confirmed %s - COMPLETED",
                    id,
                    registeredId.getCreated(),
                    registeredId.getLastConfirmed());
            log.info(msg);
            report.setReportContent(msg);
            return report;
        } catch (RuntimeException e) {
            throw new MirIdManagementStrategyException(e.getMessage());
        } finally {
            operationLock.unlock();
        }
    }

    @Transactional
    @Override
    public MirIdManagementStrategyOperationReport returnId(long id) throws MirIdManagementStrategyException {
        // Lock Acquisition
        RLock operationLock = redissonClient.getLock(CONCURRENCY_LOCK_OPERATION_RETURN_ID);
        try {
            if (!operationLock.tryLock(CONCURRENCY_LOCK_OPERATION_RETURN_ID_TIME_SECONDS_WAIT_FOR_LOCK_TIME,
                    CONCURRENCY_LOCK_OPERATION_RETURN_ID_TIME_SECONDS_LEASE_TIME,
                    TimeUnit.SECONDS)) {
                throw new MirIdManagementStrategyException(String.format("LOCK ACQUISITION TIMED OUT while returning MIR ID '%s'", MirIdHelper.prettyPrintMirId(id)));
            }
        } catch (InterruptedException e) {
            throw new MirIdManagementStrategyException(String.format("\"LOCK ACQUISITION INTERRUPTED while returning " +
                    "MIR ID '%s', '%s'", MirIdHelper.prettyPrintMirId(id), e.getMessage()));
        }
        // Operation - Return ID
        try {
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
            // Apparently, the Entity Manager cache is a troublemaker for this particular operation
            entityManager.flush();
            entityManager.getEntityManagerFactory().getCache().evictAll();
            String msg = String.format("RETURNED MIR ID, %d, on %s, minted on %s, and last confirmed on %s",
                    id,
                    returnedMirId.getCreated(),
                    activeMirId.getCreated(),
                    activeMirId.getLastConfirmed());
            log.info(msg);
            report.setReportContent(msg);
            return report;
        } catch (RuntimeException e) {
            throw new MirIdManagementStrategyException(e.getMessage());
        } finally {
            operationLock.unlock();
        }
    }
}
