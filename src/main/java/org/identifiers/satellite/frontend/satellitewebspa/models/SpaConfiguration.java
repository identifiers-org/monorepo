package org.identifiers.satellite.frontend.satellitewebspa.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Project: satellite-webspa
 * Package: org.identifiers.satellite.frontend.satellitewebspa.models
 * Timestamp: 2019-05-15 13:08
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This object defines the configuration information for the Web SPA
 */
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
public class SpaConfiguration implements Serializable {

    @Value("${org.identifiers.satellite.frontend.satellitewebspa.config.ws.resolver.url}")
    private String resolverApi;

    @Value("${org.identifiers.satellite.frontend.satellitewebspa.config.ws.hqregistry.url}")
    private String registryApi;

    @Value("${org.identifiers.satellite.frontend.satellitewebspa.config.web.hqregistry.prefixregistrationform.url}")
    private String registryUrl;

    @Value("${org.identifiers.satellite.frontend.satellitewebspa.config.web.hqregistry.prefixregistrationform.url}")
    private String registryPrefixRegistrationRequestFormUrl;

    @Value("${org.identifiers.satellite.frontend.satellitewebspa.config.ws.apiversion}")
    private String apiVersion;

    @Value("${org.identifiers.satellite.frontend.satellitewebspa.config.flag.showsearchsuggestions}")
    private boolean showSearchSuggestions;
}
