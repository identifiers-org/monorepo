package org.identifiers.cloud.hq.ws.miridcontroller;

import org.identifiers.cloud.hq.ws.miridcontroller.models.MirIdManagementStrategy;
import org.identifiers.cloud.hq.ws.miridcontroller.models.MirIdManagementStrategyOperationReport;
import org.identifiers.cloud.hq.ws.miridcontroller.models.MirIdManagementStrategyOperationReport.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(classes = {TestRedisServer.class})
public class ManagementStrategyTests {

    @Autowired
    MirIdManagementStrategy strategy;

    @Test
    void strategyLoads() {
        assertNotNull(strategy);
    }

    @Test
    void strategyMintsSuccessfully() {
        assertDoesNotThrow(strategy::mintId);
    }

    @Test
    void strategySuccessfullyKeepIdAlive() {
        long mintedId = strategy.mintId();
        MirIdManagementStrategyOperationReport report = strategy.keepAlive(mintedId);
        assertEquals(Status.SUCCESS, report.getStatus());
    }

    @Test
    void strategyLoadsIdSuccessfully() {
        MirIdManagementStrategyOperationReport report = strategy.loadId(1000);
        assertEquals(Status.SUCCESS, report.getStatus());}

    @Test
    void strategyReturnsIdSuccessfully() {
        long mintedId = strategy.mintId();
        MirIdManagementStrategyOperationReport report = strategy.returnId(mintedId);
        assertEquals(Status.SUCCESS, report.getStatus());
    }
}

