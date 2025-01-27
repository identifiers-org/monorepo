package org.identifiers.cloud.hq.ws.registry.api.models;

import org.identifiers.cloud.hq.ws.registry.models.helpers.AuthHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.models
 * Timestamp: 2019-08-20 14:50
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Component
public class DevAuthApiModel {
    @Autowired
    private AuthHelper authHelper;

    public ResponseEntity<?> getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>(authentication.getPrincipal(), HttpStatus.OK);
    }

    public ResponseEntity<?> getDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>(authentication.getDetails(), HttpStatus.OK);
    }

    public ResponseEntity<?> getCredentials() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>(authentication.getCredentials(), HttpStatus.OK);
    }

    public ResponseEntity<?> getName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>(authentication.getName(), HttpStatus.OK);
    }

    public ResponseEntity<?> getAuthorities() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>(authentication.getAuthorities(), HttpStatus.OK);
    }

    public ResponseEntity<?> getUsername() {
        return new ResponseEntity<>(authHelper.getCurrentUsername(), HttpStatus.OK);
    }
}
