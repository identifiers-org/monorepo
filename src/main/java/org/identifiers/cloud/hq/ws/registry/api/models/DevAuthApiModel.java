package org.identifiers.cloud.hq.ws.registry.api.models;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
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
    public ResponseEntity<?> getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>(authentication.getPrincipal(), HttpStatus.OK);
    }

    public ResponseEntity<?> getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof Jwt) {
            Jwt jwtToken = (Jwt) authentication.getPrincipal();
            return new ResponseEntity<>(jwtToken.getClaims().getOrDefault("preferred_username", "UNKNOWN_USER"), HttpStatus.OK);
        }
        return new ResponseEntity<>("NOT JWT", HttpStatus.OK);
    }
}
