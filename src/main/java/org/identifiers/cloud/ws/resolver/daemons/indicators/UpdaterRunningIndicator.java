package org.identifiers.cloud.ws.resolver.daemons.indicators;

import org.identifiers.cloud.ws.resolver.daemons.ResolverDataUpdater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.health.ConditionalOnEnabledHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnEnabledHealthIndicator("updater-running")
public class UpdaterRunningIndicator implements HealthIndicator {
    @Autowired
    ResolverDataUpdater resolverDataUpdater;

    /*
    * FIXME:
    * For now this just checks if the thread is running,
    * it would also be good to check if the update was successful and change the readiness state of the instance.
    * But I couldn't make it work, so I will leave it at that until a future version upgrade of the java or spring.
    * ~ Renato
    */

    @Override
    public Health health() {
        if (resolverDataUpdater.isShutdown())
            return Health.down().build();
        else
            return Health.up().build();
    }
}
