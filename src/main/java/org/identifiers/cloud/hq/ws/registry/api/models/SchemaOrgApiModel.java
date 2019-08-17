package org.identifiers.cloud.hq.ws.registry.api.models;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.models
 * Timestamp: 2019-08-17 06:54
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Component
public class SchemaOrgApiModel {
    @Value("${org.identifiers.cloud.hq.ws.registry.schemaorg.jsonld.platform.template.filename}")
    private String jsonldPlatformTemplateFilename;
    // TODO


}
