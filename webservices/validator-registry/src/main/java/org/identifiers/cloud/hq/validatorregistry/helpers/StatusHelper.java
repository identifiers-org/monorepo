package org.identifiers.cloud.hq.validatorregistry.helpers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StatusHelper {
    private int startedCounter = 0;
    private int finishedCounter = 0;

    public synchronized void startTask() {
        startedCounter++;
    }

    public synchronized void finishTask() {
        finishedCounter++;
    }

    @Scheduled(fixedDelayString = "${org.identifiers.cloud.progress.delay}", initialDelay = 500)
    public void reportStatus() {
        log.info("Current status: {} finished / {} started / {}%",
                finishedCounter, startedCounter, finishedCounter * 100.0f / startedCounter);
    }
}
