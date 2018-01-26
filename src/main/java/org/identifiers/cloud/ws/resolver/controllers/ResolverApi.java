package org.identifiers.cloud.ws.resolver.controllers;

import org.springframework.web.bind.annotation.*;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: resolver
 * Package: org.identifiers.org.cloud.ws.resolver.controllers
 * Timestamp: 2018-01-15 12:31
 * ---
 */
@RestController
public class ResolverApi {

    @RequestMapping(value = "{compactId}", method = RequestMethod.GET)
    public @ResponseBody String queryByCompactId(@PathVariable("compactId") String compactId) {
        // TODO
        return "[QUERY_BY_COMPACT_ID] Compact ID parameter ---> " + compactId;
    }

    public String queryBySelectorAndCompactId() {
        // TODO
    }
}
