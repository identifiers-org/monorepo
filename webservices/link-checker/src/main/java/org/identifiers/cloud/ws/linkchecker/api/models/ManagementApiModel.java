package org.identifiers.cloud.ws.linkchecker.api.models;

import org.identifiers.cloud.commons.messages.responses.ServiceResponse;
import org.identifiers.cloud.commons.messages.responses.linkchecker.ServiceResponseManagementRequestPayload;
import org.identifiers.cloud.ws.linkchecker.services.HistoryTrackingService;
import org.identifiers.cloud.ws.linkchecker.services.HistoryTrackingServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * Project: link-checker
 * Package: org.identifiers.cloud.ws.linkchecker.api.models
 * Timestamp: 2018-07-31 11:24
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Component
@Scope("prototype")
public class ManagementApiModel {
    private static final Logger logger = LoggerFactory.getLogger(ManagementApiModel.class);

    private HistoryTrackingService historyTrackingService;

    public ServiceResponse<ServiceResponseManagementRequestPayload> flushLinkCheckingHistory() {
        logger.warn("FLUSH REQUEST for link checking historical data");
        // It is responsibility of the history tracking service used to flush the link checking history
        // change this when the code is completed
        var payload = new ServiceResponseManagementRequestPayload().setMessage("This is a default response");
        var response = ServiceResponse.of(payload);
        try {
            historyTrackingService.deleteHistoryTrackingData();
        } catch (HistoryTrackingServiceException e) {
            logger.error("The following error occurred while trying to flush link checking historical data, '{}'", e.getMessage());
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            payload.setMessage("An error occurred while trying to flush link checking historical data, but DON'T PANIC, we'll get back on track");
        }
        return response;
    }

    @Autowired
    public void setHistoryTrackingService(HistoryTrackingService historyTrackingService) {
        this.historyTrackingService = historyTrackingService;
    }
}
