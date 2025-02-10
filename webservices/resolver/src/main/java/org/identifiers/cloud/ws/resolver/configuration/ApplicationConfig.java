package org.identifiers.cloud.ws.resolver.configuration;

import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.libapi.services.ApiServicesFactory;
import org.identifiers.cloud.ws.resolver.periodictasks.models.ResolverDataSourcer;
import org.identifiers.cloud.ws.resolver.periodictasks.models.ResolverDataSourcerFromWs;
import org.identifiers.cloud.ws.resolver.models.ResolverDataFetcher;
import org.identifiers.cloud.ws.resolver.models.ResolverDataFetcherFromDataBackend;
import org.identifiers.cloud.ws.resolver.services.ResolutionService;
import org.identifiers.cloud.ws.resolver.services.ResolveFirstResolutionStrategy;
import org.matomo.java.tracking.MatomoTracker;
import org.matomo.java.tracking.TrackerConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.web.client.RestTemplate;
import org.identifiers.cloud.libapi.services.ResourceRecommenderService;

import java.net.URI;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: resolver
 * Package: org.identifiers.org.cloud.ws.resolver.configuration
 * Timestamp: 2018-01-17 16:25
 * ---
 */
@Configuration
@EnableRedisRepositories
@Slf4j
public class ApplicationConfig {
    @Bean
    public RedisConnectionFactory redisConnectionFactory (
        @Value("${spring.redis.port}") int redisPort,
        @Value("${spring.redis.host}") String redisHost
    ) {
        log.info("Using Redis on host '{}' port '{}'", redisHost, redisPort);
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(redisHost,
                redisPort);
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    public RedisTemplate<?, ?> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<byte[], byte[]> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    @Bean
    public ResolverDataSourcer resolverDataSourcer() {
        return new ResolverDataSourcerFromWs();
    }

    @Bean
    public ResolverDataFetcher resolverDataFetcher() {
        return new ResolverDataFetcherFromDataBackend();
    }

    @Bean
    public ResolutionService resolutionService() {
        log.info("[CONFIG] Resolution Strategy - Resolve First");
        return new ResolveFirstResolutionStrategy();
    }

    @Bean
    public RestTemplate getRestTempalte() {
        return new RestTemplate();
    }

    @Bean
    public ResourceRecommenderService getResourceRecommenderService(
            @Value("${org.identifiers.cloud.ws.resolver.service.recommender.host}")
            String resourceRecommenderServiceHost,
            @Value("${org.identifiers.cloud.ws.resolver.service.recommender.port}")
            String resourceRecommenderServicePort
    ) {
        return ApiServicesFactory.getResourceRecommenderService
                (resourceRecommenderServiceHost, resourceRecommenderServicePort);
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

    @Bean(destroyMethod = "shutdown")
    public ExecutorService matomoTrackerExecutor(
            @Value("${org.identifiers.matomo.thread-pool-size}")
            int poolSize
    ) {
        return Executors.newFixedThreadPool(poolSize);
    }
}
