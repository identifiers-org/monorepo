package org.identifiers.cloud.ws.linkchecker.data.models;

import java.sql.Timestamp;

/**
 * Project: link-checker
 * Package: org.identifiers.cloud.ws.linkchecker.data.models
 * Timestamp: 2018-05-22 10:48
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This entity models a checked URL
 */
public class CheckedUrl {
    // URL that has been checked
    private String url;
    // When it has been checked (UTC)
    private Timestamp timestamp;
    // Returning HTTP Status code
    private int httpStatus;
}
