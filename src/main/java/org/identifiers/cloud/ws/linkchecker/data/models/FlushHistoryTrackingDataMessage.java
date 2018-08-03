package org.identifiers.cloud.ws.linkchecker.data.models;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Project: link-checker
 * Package: org.identifiers.cloud.ws.linkchecker.data.models
 * Timestamp: 2018-08-03 10:02
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This message model represents an announcement that a request for flushing history tracking data has been received.
 */
public class FlushHistoryTrackingDataMessage implements Serializable {
    // Timestamp for the message
    private String timestamp = (new Timestamp(new Date().getTime())).toString();

    public Timestamp getTimestamp() {
        return Timestamp.valueOf(timestamp);
    }

    public FlushHistoryTrackingDataMessage setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp.toString();
        return this;
    }

}
