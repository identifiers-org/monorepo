package org.identifiers.cloud.ws.linkchecker.strategies;

import java.sql.Timestamp;

/**
 * Project: link-checker
 * Package: org.identifiers.cloud.ws.linkchecker.strategies
 * Timestamp: 2018-05-28 8:29
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * Report built by a link checking strategy.
 */
public class LinkCheckerReport {
    // Checked URL
    private String url;
    // UTC time stamp when the URL was checked
    private Timestamp timestamp;
    // Returned HTTP Status
    private int httpStatus;
}
