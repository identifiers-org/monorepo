package org.identifiers.cloud.ws.metadata.daemons.indicators;

import org.identifiers.cloud.ws.metadata.daemons.MetadataCollector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.health.ConditionalOnEnabledHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnEnabledHealthIndicator("metadata-collector")
public class MetadataCollectorIndicator implements HealthIndicator {
    @Autowired
    MetadataCollector metadataCollector;

    @Override
    public Health health() {
        if (metadataCollector.isShutdown())
            return Health.down().build();
        else
            return Health.up().build();
    }
}
