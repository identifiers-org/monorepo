package org.identifiers.cloud.hq.ws.registry.models;

import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models
 * Timestamp: 2019-03-26 16:04
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class MirIdServiceWsClientTest {

    @Autowired
    private MirIdService mirIdService;

/*    @Test
    public void mintId() {
        mirIdService.mintId();
    }

    @Test
    public void keepAlive() {
        mirIdService.keepAlive("MIR:00000002");
    } */
}