package org.identifiers.cloud.ws.linkchecker.channels.linkcheckresults;

import org.identifiers.cloud.ws.linkchecker.data.models.LinkCheckResult;

/**
 * Project: link-checker
 * Package: org.identifiers.cloud.ws.linkchecker.channels.linkcheckresults
 * Timestamp: 2018-08-02 14:12
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
public interface LinkCheckResultListener {
    void process(LinkCheckResult linkCheckResult);
}
