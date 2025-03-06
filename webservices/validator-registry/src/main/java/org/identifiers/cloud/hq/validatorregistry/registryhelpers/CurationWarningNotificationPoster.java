package org.identifiers.cloud.hq.validatorregistry.registryhelpers;

import org.identifiers.cloud.commons.messages.models.CurationWarningNotification;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class CurationWarningNotificationPoster {
    public void post(Collection<CurationWarningNotification> notifications) {
        // TODO
    }
}
