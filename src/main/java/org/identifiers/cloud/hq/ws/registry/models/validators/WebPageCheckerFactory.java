package org.identifiers.cloud.hq.ws.registry.models.validators;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models.validators
 * Timestamp: 2019-03-14 15:56
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
public class WebPageCheckerFactory {
    // TODO - Remind me why I created a Factory for this...
    public static WebPageChecker getWebPageChecker() {
        return new WebPageCheckerDefault();
    }
}
