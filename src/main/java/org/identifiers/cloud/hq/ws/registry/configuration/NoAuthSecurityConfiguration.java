package org.identifiers.cloud.hq.ws.registry.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.configuration
 * Timestamp: 2019-04-25 07:46
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Configuration
@Slf4j
@Profile("authdisabled")
@EnableWebSecurity
public class NoAuthSecurityConfiguration extends WebSecurityConfigurerAdapter {
    // TODO - Maybe configurable in the future?
    // Connection parameters
    private static final int WS_REQUEST_CONNECT_TIMEOUT = 2000; // 2 seconds
    private static final int WS_REQUEST_READ_TIMEOUT = 2000; // 2 seconds

    @PostConstruct
    private void postConstruct() {
        log.info("[CONFIG] NO AUTH configuration loaded");
    }

    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        // Configure requests time outs
        simpleClientHttpRequestFactory.setConnectTimeout(WS_REQUEST_CONNECT_TIMEOUT);
        simpleClientHttpRequestFactory.setReadTimeout(WS_REQUEST_READ_TIMEOUT);
        return new RestTemplate(simpleClientHttpRequestFactory);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        log.info("Opening all requests, without authorization");
        http.authorizeRequests().anyRequest().permitAll().and().csrf().disable();
    }
}