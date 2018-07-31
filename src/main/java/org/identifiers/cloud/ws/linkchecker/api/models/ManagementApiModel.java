package org.identifiers.cloud.ws.linkchecker.api.models;

import org.identifiers.cloud.ws.linkchecker.api.responses.ServiceResponseManagementRequest;
import org.identifiers.cloud.ws.linkchecker.api.responses.ServiceResponseManagementRequestPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
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

    public ServiceResponseManagementRequest flushLinkCheckingHistory() {
        logger.warn("FLUSH REQUEST for link checking hisotrical data");
        // TODO It is responsibility of the history tracking service used to flush the link checking history
        // TODO change this when the code is completed
        ServiceResponseManagementRequest response = new ServiceResponseManagementRequest();
        response.setPayload(new ServiceResponseManagementRequestPayload()
                .setMessage("This is a default response"));
        return response;
    }
}
