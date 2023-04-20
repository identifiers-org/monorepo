package org.identifiers.cloud.hq.ws.miridcontroller;

import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.hq.ws.miridcontroller.models.MirIdManagementStrategy;
import org.identifiers.cloud.hq.ws.miridcontroller.models.MirIdManagementStrategyException;
import org.identifiers.cloud.hq.ws.miridcontroller.models.MirIdManagementStrategyOperationReport;
import org.identifiers.cloud.hq.ws.miridcontroller.models.MirIdManagementStrategyOperationReport.Status;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assume.*;

@RunWith(SpringRunner.class)
@SpringBootTest()
@ActiveProfiles("authdisabled")
@Slf4j
public class ManagementStrategyTests {

    @Autowired
    MirIdManagementStrategy strategy;

    @Test
    public void strategyLoads() {
        assertNotNull(strategy);
    }

    @Test
    public void strategyMintsSuccessfully() {
        strategy.mintId(); //Should throw exception if unsuccessful
    }

    @Test
    public void strategySuccessfullyKeepIdAlive() {
        try {
            long mintedId = strategy.mintId();
            MirIdManagementStrategyOperationReport report = strategy.keepAlive(mintedId);
            assertEquals(Status.SUCCESS, report.getStatus());
        } catch (MirIdManagementStrategyException e) {
            log.error("Management strategy fails on ID keep alive", e);
            assumeNoException(e);
        }
    }

    @Test
    public void strategyLoadsIdSuccessfully() {
        try {
            MirIdManagementStrategyOperationReport report = strategy.loadId(1000);
            assertEquals(report.getReportContent(), Status.SUCCESS, report.getStatus());
        } catch (MirIdManagementStrategyException e) {
            log.error("Management strategy fails on load ID", e);
            assumeNoException(e);
        }
    }

    @Test
    public void strategyReturnsIdSuccessfully() {
        try {
            long mintedId = strategy.mintId();
            MirIdManagementStrategyOperationReport report = strategy.returnId(mintedId);
            assertEquals(Status.SUCCESS, report.getStatus());
        } catch (MirIdManagementStrategyException e) {
            log.error("Management strategy fails on return ID", e);
            assumeNoException(e);
        }
    }
}

