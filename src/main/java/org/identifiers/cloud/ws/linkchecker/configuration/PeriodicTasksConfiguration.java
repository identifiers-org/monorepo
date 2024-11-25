package org.identifiers.cloud.ws.linkchecker.configuration;

import org.identifiers.cloud.ws.linkchecker.periodictasks.LinkCheckingTask;
import org.identifiers.cloud.ws.linkchecker.periodictasks.PeriodicChecksFeederTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.time.Instant;
import java.util.Optional;

@Configuration
@EnableScheduling
public class PeriodicTasksConfiguration implements SchedulingConfigurer {
    final Logger feederTaskLogger = LoggerFactory.getLogger(PeriodicChecksFeederTask.class);
    final Logger checkingTaskLogger = LoggerFactory.getLogger(LinkCheckingTask.class);

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
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(2);
        return scheduler;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setTaskScheduler(taskScheduler());
        final Logger configLogger = LoggerFactory.getLogger(PeriodicTasksConfiguration.class);
        if (linkCheckingTask != null) {
            taskRegistrar.addTriggerTask(linkCheckingTask, this::getNextRunForLinkCheckingTask);
        } else {
            configLogger.warn("--- [DISABLED] Periodic Link Checking ---");
        }

        if (periodicChecksFeederTask != null) {
            taskRegistrar.addTriggerTask(periodicChecksFeederTask, this::getNextRunForCheckFeederTask);
        } else {
            configLogger.warn("--- [DISABLED] Periodic check request feeder ---");
        }
    }


    public Instant getNextRunForLinkCheckingTask(TriggerContext triggerContext) {
        var waitSeconds = linkCheckingTask.getNextRandomWait();
        var nextRun = Optional.ofNullable(triggerContext.lastCompletion())
                              .orElse(Instant.now())
                              .plusSeconds(waitSeconds);
        checkingTaskLogger.info("Next run of link checker after {}s, at {}", waitSeconds, nextRun);
        return nextRun;
    }

    public Instant getNextRunForCheckFeederTask(TriggerContext triggerContext) {
        var waitSeconds = periodicChecksFeederTask.getNextWaitTimeSeconds();
        var nextRun = Optional.ofNullable(triggerContext.lastCompletion())
                              .orElse(Instant.now())
                              .plusSeconds(waitSeconds);
        feederTaskLogger.info("Next run of feeder task after {}s, at {}", waitSeconds, nextRun);
        return nextRun;
    }
}
