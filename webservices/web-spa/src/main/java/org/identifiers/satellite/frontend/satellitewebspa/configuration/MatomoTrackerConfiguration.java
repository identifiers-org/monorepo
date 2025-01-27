package org.identifiers.satellite.frontend.satellitewebspa.configuration;

import io.micrometer.common.util.StringUtils;
import org.matomo.java.tracking.MatomoTracker;
import org.matomo.java.tracking.TrackerConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class MatomoTrackerConfiguration {
    @Bean(destroyMethod = "shutdown")
    public ExecutorService matomoTrackerExecutor(
            @Value("${org.identifiers.matomo.thread-pool-size}")
            int poolSize
    ) {
        return Executors.newFixedThreadPool(poolSize);
    }

    @Bean
    public MatomoTracker getMatomoTracker(
            @Value("${org.identifiers.matomo.baseUrl}") URI matomoBaseUrl,
            @Value("${org.identifiers.matomo.authToken:}") String authToken,
            @Value("${org.identifiers.matomo.enabled}") boolean enabled
    ) {
        var confBuilder = TrackerConfiguration.builder()
                .apiEndpoint(matomoBaseUrl)
                .enabled(enabled);
        if (StringUtils.isNotBlank(authToken) && authToken.length() == 32) {
            confBuilder = confBuilder.defaultAuthToken(authToken);
        }
        return new MatomoTracker(confBuilder.build());
    }
}
