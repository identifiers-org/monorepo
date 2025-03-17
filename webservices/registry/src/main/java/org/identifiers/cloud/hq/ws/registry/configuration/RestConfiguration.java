package org.identifiers.cloud.hq.ws.registry.configuration;

import org.identifiers.cloud.hq.ws.registry.data.models.Institution;
import org.identifiers.cloud.hq.ws.registry.data.models.curationwarnings.CurationWarning;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class RestConfiguration implements RepositoryRestConfigurer {
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        RepositoryRestConfigurer.super.configureRepositoryRestConfiguration(config, cors);
        config.exposeIdsFor(Institution.class);

        var expConfig = config.getExposureConfiguration();

        expConfig.forDomainType(CurationWarning.class).disablePutForCreation()
                .withItemExposure((metadata, httpMethods) ->
                    httpMethods.disable(HttpMethod.DELETE, HttpMethod.POST, HttpMethod.PUT, HttpMethod.PATCH)
                );
    }
}
