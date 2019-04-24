package org.identifiers.cloud.hq.ws.miridcontroller.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
    private String clientId;

    @PostConstruct
    private void postConstruct() {
        log.info("[CONFIG] (AAA) ENABLED");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // TODO - Spring Security is a big f*****g black box that is incredibly difficult to figure out. When a not
        //  authenticated call is made to the endpoints protected by 'access' rules in the antMatchers, an exception is
        //  thrown and HTTP 500 returned back to the client, instead of a 401, because, as there is no principal, the
        //  expression fails to evaluate. FUCK! I haven't found a fucking way around this, and all the fucking
        //  documentation I've found on the internet doesn't even want to touch this "expression based
        //  authentication". I've tried to decipher SpEL, and using "#principal != null ? principal?
        //  .claims['%s']['%s']['roles'].contains('idMinting') : false", and then, some-fucking-how, it works for
        //  non-authenticated requests by returning an HTTP 401, but for authenticated requests it says something
        //  like "The token provided has insufficient scope for this request".
        http
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/restApi/**").permitAll()
                .antMatchers("/mirIdApi/mintId").access(String.format("principal?.claims['%s']['%s']['roles'].contains('idMinting')", JWT_SCOPE_RESOURCE_ACCESS, clientId))
                .anyRequest().denyAll()
                .and()
                .oauth2ResourceServer().jwt();
    }
}
