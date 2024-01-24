package org.identifiers.cloud.ws.resolver.configuration;

import jakarta.annotation.PostConstruct;
import org.identifiers.cloud.ws.resolver.periodictasks.ResolverDataUpdater;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@EnableScheduling
@ConditionalOnProperty("org.identifiers.cloud.ws.resolver.data.updater.is-enabled")
public class PeriodicTasksConfiguration implements SchedulingConfigurer {
    final ResolverDataUpdater resolverDataUpdater;
    public PeriodicTasksConfiguration(ResolverDataUpdater resolverDataUpdater) {
        this.resolverDataUpdater = resolverDataUpdater;
    }

    final Executor taskExecutor = Executors.newSingleThreadScheduledExecutor();

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskExecutor);
        taskRegistrar.addTriggerTask(resolverDataUpdater,
                triggerContext -> Optional
                        .ofNullable(triggerContext.lastActualExecution())
                        .orElse(Instant.now())
                        .plusSeconds(resolverDataUpdater.getNextWait()));
    }

    @PostConstruct
    public void runUpdaterAtStartup() {
        taskExecutor.execute(resolverDataUpdater);
    }
}
