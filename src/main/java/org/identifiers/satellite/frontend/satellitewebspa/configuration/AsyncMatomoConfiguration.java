package org.identifiers.satellite.frontend.satellitewebspa.configuration;

import org.identifiers.satellite.frontend.satellitewebspa.services.GenericAsyncExceptionHandler;
import org.matomo.java.tracking.MatomoTracker;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncMatomoConfiguration implements AsyncConfigurer {

    @Value("${org.identifiers.matomo.baseUrl}")
    private String matomoBaseUrl;

    @Bean
    public MatomoTracker getMatomoTracker() {
        return new MatomoTracker(matomoBaseUrl);
    }

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new GenericAsyncExceptionHandler();
    }
}
