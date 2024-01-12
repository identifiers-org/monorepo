package org.identifiers.cloud.ws.resourcerecommender.configurations;

import org.identifiers.cloud.libapi.services.ApiServicesFactory;
import org.identifiers.cloud.libapi.services.LinkCheckerService;
import org.identifiers.cloud.ws.resourcerecommender.models.ScoreProviderOnReliability;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
            @Value("${org.identifiers.cloud.ws.resourcerecommender.backend.service.linkchecker.host}")
            String serviceLinkCheckerHost,
            @Value("${org.identifiers.cloud.ws.resourcerecommender.backend.service.linkchecker.port}")
            String serviceLinkCheckerPort
    ) {
        return new ScoreProviderOnReliability(serviceLinkCheckerHost, serviceLinkCheckerPort);
    }
}
