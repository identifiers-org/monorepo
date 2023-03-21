package org.identifiers.cloud.ws.linkchecker.daemons.indicators;

import org.identifiers.cloud.ws.linkchecker.daemons.LinkChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.health.ConditionalOnEnabledHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnEnabledHealthIndicator("link-checker-thread")
public class LinkCheckerHealthIndicator implements HealthIndicator {
    @Autowired
    LinkChecker linkChecker;

    @Override
    public Health health() {
        if (linkChecker.isShutdown())
            return Health.down().build();
        else
            return Health.up().build();
    }
}
