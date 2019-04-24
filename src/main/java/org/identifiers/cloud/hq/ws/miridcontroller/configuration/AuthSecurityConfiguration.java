package org.identifiers.cloud.hq.ws.miridcontroller.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.annotation.PostConstruct;

/**
 * Project: mirid-controller
 * Package: org.identifiers.cloud.hq.ws.miridcontroller.configuration
 * Timestamp: 2019-04-24 12:34
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Configuration
@Profile("authprofile")
@Slf4j
@EnableWebSecurity
public class AuthSecurityConfiguration extends WebSecurityConfigurerAdapter {
    static final String JWT_SCOPE_RESOURCE_ACCESS = "resource_access";

    @PostConstruct
    private void postConstruct() {
        log.info("[CONFIG] (AAA) ENABLED");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    // TODO
        http
                .authorizeRequests()
                    .antMatchers(HttpMethod.GET, "/restApi/**").permitAll()
                    .anyRequest().denyAll()
                .and()
                .oauth2ResourceServer().jwt();
    }
}
