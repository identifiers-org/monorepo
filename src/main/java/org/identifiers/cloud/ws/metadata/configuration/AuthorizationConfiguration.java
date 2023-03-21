package org.identifiers.cloud.ws.metadata.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class AuthorizationConfiguration extends WebSecurityConfigurerAdapter {

    @Value("${org.identifiers.cloud.ws.metadata.requiredrole}")
    String requiredRole;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String aExp = String.format(
                "isAuthenticated() and principal.claims['realm_access']['roles'].contains('%s')",
                requiredRole
        );

        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/getMetadataForUrl*").permitAll()
                .antMatchers("/actuator").access(aExp)
                .antMatchers("/actuator/loggers/**").access(aExp)
                .antMatchers("/actuator/health/**").permitAll()
                .antMatchers(HttpMethod.GET, "/**").permitAll()
            .and()
                .csrf().disable()
                .logout().disable()
                .formLogin().disable()
                .anonymous().and()
                .oauth2ResourceServer().jwt();
    }
}
