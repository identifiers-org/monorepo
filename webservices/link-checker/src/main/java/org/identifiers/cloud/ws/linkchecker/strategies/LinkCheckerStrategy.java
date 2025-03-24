package org.identifiers.cloud.ws.linkchecker.strategies;

import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.commons.urlchecking.UrlChecker;

import java.net.URL;

/**
 * Project: link-checker
 * Package: org.identifiers.cloud.ws.linkchecker.strategies
 * Timestamp: 2018-05-28 8:24
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This is the interface of Link Checking strategies
 */
@Slf4j
public abstract class LinkCheckerStrategy {
    protected final String idorgAgentStr;
    protected final UrlChecker urlChecker;
    protected LinkCheckerStrategy(String appVersion, String javaVersion,
                                  String appHomepage, UrlChecker urlChecker) {
        this.urlChecker = urlChecker;
        this.idorgAgentStr = String.format("LinkChecker/%s +%s Java/%s", appVersion, appHomepage, javaVersion);
    }

    public abstract LinkCheckerReport check(URL url, boolean accept401or403) throws LinkCheckerException;
}
