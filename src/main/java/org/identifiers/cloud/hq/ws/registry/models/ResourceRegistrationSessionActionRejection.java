package org.identifiers.cloud.hq.ws.registry.models;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models
 * Timestamp: 2019-07-29 17:21
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * Composite action to perform within the context of a resource registration session that has just been closed by
 * rejecting the request
 */
@Component
@Slf4j
@Qualifier("ResourceRegistrationSessionActionRejection")
public class ResourceRegistrationSessionActionRejection implements ResourceRegistrationSessionCompositeSequenceAction {
    // Related actions
    @Autowired
    private ResourceRegistrationSessionActionLogger actionLogger;
    @Autowired
    private ResourceRegistrationSessionActionNotifierEmailRejection actionNotifierEmailRejection;

    @Override
    public Logger getLogger() {
        return log;
    }

    @Override
    public String getActionName() {
        return "REJECTION";
    }

    // Director
    @Override
    public List<ResourceRegistrationSessionAction> buildActionSequence() {
        return Arrays.asList(
                actionLogger,
                actionNotifierEmailRejection
        );
    }

}
