package org.identifiers.cloud.ws.linkchecker.configuration;

import org.identifiers.cloud.ws.linkchecker.periodictasks.LinkCheckingTask;
import org.identifiers.cloud.ws.linkchecker.periodictasks.PeriodicChecksFeederTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@EnableScheduling
public class PeriodicTasksConfiguration implements SchedulingConfigurer {
    final Logger logger = LoggerFactory.getLogger(PeriodicTasksConfiguration.class);

    final LinkCheckingTask linkCheckingTask;
    final PeriodicChecksFeederTask periodicChecksFeederTask;
    public PeriodicTasksConfiguration(@Autowired(required = false)
                                      LinkCheckingTask linkCheckingTask,
                                      @Autowired(required = false)
                                      PeriodicChecksFeederTask periodicFeederTask) {
        this.linkCheckingTask = linkCheckingTask;
        this.periodicChecksFeederTask = periodicFeederTask;
    }


    @Bean(destroyMethod="shutdown")
    public ExecutorService taskExecutor() {
        return Executors.newScheduledThreadPool(2);
    }


    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        if (linkCheckingTask != null) {
            taskRegistrar.addTriggerTask(linkCheckingTask,
                    triggerContext -> Optional
                            .ofNullable(triggerContext.lastActualExecution())
                            .orElse(Instant.now())
                            .plusSeconds(linkCheckingTask.getNextRandomWait()));
        } else {
            logger.warn(
                    "--- [DISABLED] Periodic Link Checking ---");
        }

        if (periodicChecksFeederTask != null) {
            taskRegistrar.addTriggerTask(periodicChecksFeederTask,
                    triggerContext -> Optional
                            .ofNullable(triggerContext.lastActualExecution())
                            .orElse(Instant.now())
                            .plusSeconds(periodicChecksFeederTask.getNextWaitTimeSeconds()));
        } else {
            logger.warn(
              "--- [DISABLED] Periodic Link Check Requester on Resolution Base Data ---");
        }
    }
}
