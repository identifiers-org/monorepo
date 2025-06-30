package org.identifiers.cloud.ws.resourcerecommender.configurations;

import org.identifiers.cloud.libapi.services.ApiServicesFactory;
import org.identifiers.cloud.libapi.services.LinkCheckerService;
import org.identifiers.cloud.ws.resourcerecommender.models.ScoreProviderOnReliability;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import java.time.Duration;

@Configuration
public class LinkCheckerServiceConfiguration {
    @Bean
    public LinkCheckerService getLinkCheckerService (
            @Value("${org.identifiers.cloud.ws.resourcerecommender.backend.service.linkchecker.host}")
            String serviceLinkCheckerHost,
            @Value("${org.identifiers.cloud.ws.resourcerecommender.backend.service.linkchecker.port}")
            String serviceLinkCheckerPort
    ) {
        return ApiServicesFactory.getLinkCheckerService(serviceLinkCheckerHost, serviceLinkCheckerPort);
    }

    @Bean
    public ScoreProviderOnReliability scoreProviderOnReliability(
            LinkCheckerService linkCheckerService,
            @Value("${org.identifiers.cloud.ws.resourcerecommender.scorer.reliability.max-cache-size}")
            int maxCacheSize,
            @Value("${org.identifiers.cloud.ws.resourcerecommender.scorer.reliability.max-cache-duration}")
            Duration maxCacheDuration
    ) {
        Assert.state(maxCacheSize > 0, "Max cache size must be greater than 0.");
        Assert.state(maxCacheDuration.getSeconds() > 0, "Max cache duration must be greater than 0.");
        return new ScoreProviderOnReliability(linkCheckerService, maxCacheSize, maxCacheDuration);
    }
}
