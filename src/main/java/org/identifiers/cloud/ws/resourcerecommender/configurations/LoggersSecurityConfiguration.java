package org.identifiers.cloud.ws.resourcerecommender.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class LoggersSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Value("${org.identifiers.cloud.ws.resourcerecommender.requiredrole}")
    String requiredRole; // Assumed that user gets role directly

    @Override
    protected void configure(HttpSecurity http) throws Exception {
       String aExp = String.format(
                "isAuthenticated() and principal.claims['realm_access']['roles'].contains('%s')",
                requiredRole
        );

        http.authorizeRequests()
                .antMatchers("/actuator").access(aExp)
                .antMatchers("/actuator/loggers/**").access(aExp)
                .antMatchers(HttpMethod.GET, "/actuator/health/**").permitAll()
                .antMatchers(HttpMethod.POST,"/").permitAll()
            .and()
                .csrf().disable()
                .logout().disable()
                .formLogin().disable()
                .anonymous().disable()
                .oauth2ResourceServer().jwt();
    }
}
