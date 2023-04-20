package org.identifiers.cloud.ws.linkchecker.daemons.indicators;

import org.identifiers.cloud.ws.linkchecker.daemons.PeriodicCheckRequesterResolutionBaseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.health.ConditionalOnEnabledHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnEnabledHealthIndicator("periodic-checker-thread")
public class PeriodicCheckRequesterResolutionBaseDataHealthIndicator implements HealthIndicator {

    @Autowired
    PeriodicCheckRequesterResolutionBaseData periodicCheckRequesterResolutionBaseData;

    @Override
    public Health health() {
        if (periodicCheckRequesterResolutionBaseData.isShutdown())
            return Health.down().build();
        else
            return Health.up().build();
    }
}
