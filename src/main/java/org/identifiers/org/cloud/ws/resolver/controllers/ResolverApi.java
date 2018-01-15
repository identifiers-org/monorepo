package org.identifiers.org.cloud.ws.resolver.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: resolver
 * Package: org.identifiers.org.cloud.ws.resolver.controllers
 * Timestamp: 2018-01-15 12:31
 * ---
 */
@RestController
public class ResolverApi {
    
    @RequestMapping("/")
    public String index() {
        return "Hey!, I'm your resolver!";
    }
}
