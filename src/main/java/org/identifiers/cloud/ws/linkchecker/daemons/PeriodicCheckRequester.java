package org.identifiers.cloud.ws.linkchecker.daemons;

/**
 * Project: link-checker
 * Package: org.identifiers.cloud.ws.linkchecker.daemons
 * Timestamp: 2018-05-31 15:43
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This is a check requester daemon that will use resolution insight data for periodically request link checking of
 * resources and providers.
 */
public class PeriodicCheckRequester extends Thread {
    private static final int WAIT_TIME_MAX_BEFORE_NEXT_REQUEST_SECONDS = 86400; // 24 hours
    private static final int WAIT_TIME_MIN_BEFORE_NEXT_REQUEST_SECONDS = 21600; // 6 hours
}
