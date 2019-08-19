package org.identifiers.cloud.hq.ws.registry.api.models;

import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.hq.ws.registry.models.SchemaOrgMetadataProvider;
import org.identifiers.cloud.hq.ws.registry.models.SchemaOrgMetadataProviderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@Slf4j
public class SchemaOrgApiModel {
    // Config
    //@Value("${org.identifiers.cloud.hq.ws.registry.schemaorg.jsonld.platform.template.filename}")
    //private String jsonldPlatformTemplateFilename;
    // Dependencies
    //@Autowired
    //private ResourceLoader resourceLoader;
    // Schema.org metadata provider
    @Autowired
    private SchemaOrgMetadataProvider schemaOrgMetadataProvider;

    public ResponseEntity<?> getMetadataForPlatform() {
        try {
            return new ResponseEntity<>(schemaOrgMetadataProvider.getForPlatform(), HttpStatus.OK);
        } catch (SchemaOrgMetadataProviderException e) {
            String errorMessage = String.format("Could not produce platform metadata due to '%s'", e.getMessage());
            log.error(errorMessage);
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // TODO

//    public ResponseEntity<?> getAnnotationForPlatform() {
//        try {
//            String jsonLdContent = IOUtils.toString(resourceLoader.getResource(jsonldPlatformTemplateFilename).getInputStream(), Charset.forName("UTF-8"));
//            // TODO We want this returned as json
//            return new ResponseEntity<>(jsonLdContent, HttpStatus.OK);
//        } catch (IOException e) {
//            String errorMessage = String.format("Could not load JSON LD platform wide schema.org annotations from '%s' due to '%s'", jsonldPlatformTemplateFilename, e.getMessage());
//            log.error(errorMessage);
//            return new ResponseEntity<>(String.format("{\"error\": \"%s\"}", errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
}
