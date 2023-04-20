package org.identifiers.cloud.ws.resourcerecommender.healthindicators;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.health.ConditionalOnEnabledHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.web.util.UriComponentsBuilder;
import javax.annotation.PostConstruct;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@Component
@ConditionalOnEnabledHealthIndicator("link-checker")
@Slf4j
public class LinkCheckerOnlineIndicator implements HealthIndicator {
    private URL serviceLinkCheckerHealthCheckUrl;

    @Value("${org.identifiers.cloud.ws.resourcerecommender.backend.service.linkchecker.host}")
    String serviceLinkCheckerHost;
    @Value("${org.identifiers.cloud.ws.resourcerecommender.backend.service.linkchecker.port}")
    String serviceLinkCheckerPort;
    @Value("${org.identifiers.cloud.resourcerecommender.backend.service.linkchecker.healthcheckpath}")
    String healthCheckPath;

    @PostConstruct
    public void init() throws MalformedURLException {
        serviceLinkCheckerHealthCheckUrl = UriComponentsBuilder.newInstance()
                .scheme("http").host(serviceLinkCheckerHost).port(serviceLinkCheckerPort)
                .path(healthCheckPath).build().toUri().toURL();
        log.debug("Link checker health url: {}", serviceLinkCheckerHealthCheckUrl);
    }

    @Override
    public Health health() {
        if (isExternalApiRunning()) {
            return Health.up().build();
        } else {
            return Health.outOfService()
                    .withDetail("Health test URL", serviceLinkCheckerHealthCheckUrl).build();
        }
    }

    private boolean isExternalApiRunning() {
        try {
            HttpURLConnection con = (HttpURLConnection) serviceLinkCheckerHealthCheckUrl.openConnection();
            return HttpStatus.valueOf(con.getResponseCode()).is2xxSuccessful();
        } catch (Exception e) {
            log.error("Error on checking link checker readiness", e);
        }
        return false;
    }
}
