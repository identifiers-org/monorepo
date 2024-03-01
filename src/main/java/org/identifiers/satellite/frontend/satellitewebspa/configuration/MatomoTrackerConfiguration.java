package org.identifiers.satellite.frontend.satellitewebspa.configuration;

import io.micrometer.common.util.StringUtils;
import org.matomo.java.tracking.MatomoTracker;
import org.matomo.java.tracking.TrackerConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

@Configuration
public class MatomoTrackerConfiguration {
    @Bean
    public MatomoTracker getMatomoTracker(
            @Value("${org.identifiers.matomo.baseUrl}") URI matomoBaseUrl,
            @Value("${org.identifiers.matomo.authToken:}") String authToken,
            @Value("${org.identifiers.matomo.enabled}") boolean enabled
    ) {
        var confBuilder = TrackerConfiguration.builder()
                .apiEndpoint(matomoBaseUrl)
                .threadPoolSize(30)
                .enabled(enabled);
        if (StringUtils.isNotBlank(authToken) && authToken.length() == 32) {
            confBuilder = confBuilder.defaultAuthToken(authToken);
        }
        return new MatomoTracker(confBuilder.build());
    }
}
