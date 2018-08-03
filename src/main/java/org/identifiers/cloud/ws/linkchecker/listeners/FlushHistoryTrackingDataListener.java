package org.identifiers.cloud.ws.linkchecker.listeners;

import org.identifiers.cloud.ws.linkchecker.channels.Listener;
import org.identifiers.cloud.ws.linkchecker.channels.management.flushhistorytrackingdata
        .FlushHistoryTrackingDataSubscriber;
import org.identifiers.cloud.ws.linkchecker.data.models.FlushHistoryTrackingDataMessage;
import org.identifiers.cloud.ws.linkchecker.services.HistoryTrackingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * Project: link-checker
 * Package: org.identifiers.cloud.ws.linkchecker.listeners
 * Timestamp: 2018-08-03 11:40
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
public class FlushHistoryTrackingDataListener extends Listener<FlushHistoryTrackingDataMessage> {
    private static final Logger logger = LoggerFactory.getLogger(FlushHistoryTrackingDataListener.class);

    @Autowired
    private FlushHistoryTrackingDataSubscriber subscriber;

    @Autowired
    private HistoryTrackingService historyTrackingService;

    @PostConstruct
    private void init() {
        logger.info("Adding listener for requests on flushing history tracking data");
        subscriber.addListener(this);
    }
    // TODO
}
