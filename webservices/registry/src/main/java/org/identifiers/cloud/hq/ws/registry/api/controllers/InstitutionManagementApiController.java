package org.identifiers.cloud.hq.ws.registry.api.controllers;

import org.identifiers.cloud.hq.ws.registry.api.models.InstitutionManagementApiModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.controllers
 * Timestamp: 2019-08-22 11:16
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@RestController
@RequestMapping("institutionManagementApi")
public class InstitutionManagementApiController {
    @Autowired
    private InstitutionManagementApiModel model;

    // --- Institution Lifecycle Management
    @GetMapping("deleteById/{id}")
    public ResponseEntity<?> deleteById(@PathVariable long id) {
        return model.deleteById(id);
    }
}
