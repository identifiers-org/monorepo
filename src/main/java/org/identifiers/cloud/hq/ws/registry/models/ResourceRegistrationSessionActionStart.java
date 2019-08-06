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
 * Timestamp: 2019-08-06 03:48
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Component
@Slf4j
@Qualifier("ResourceRegistrationSessionActionStart")
public class ResourceRegistrationSessionActionStart implements ResourceRegistrationSessionCompositeSequenceAction {
    @Autowired
    private ResourceRegistrationSessionActionLogger actionLogger;
    @Autowired
    private ResourceRegistrationSessionActionNotifierEmailCuratorStart actionNotifierEmailCuratorStart;

    @Override
    public Logger getLogger() {
        return log;
    }

    @Override
    public String getActionName() {
        return "START";
    }

    @Override
    public List<ResourceRegistrationSessionAction> buildActionSequence() {
        return Arrays.asList(
                actionLogger,
                actionNotifierEmailCuratorStart
        );
    }
}
