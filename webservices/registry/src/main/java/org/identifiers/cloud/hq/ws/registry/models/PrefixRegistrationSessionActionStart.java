package org.identifiers.cloud.hq.ws.registry.models;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models
 * Timestamp: 2019-08-05 14:21
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Slf4j
@Component
@RequiredArgsConstructor
@Qualifier("PrefixRegistrationSessionActionStart")
public class PrefixRegistrationSessionActionStart implements PrefixRegistrationSessionCompositeSequenceAction {
    private final PrefixRegistrationSessionActionLogger actionLogger;
    private final PrefixRegistrationSessionActionEmailNotifier prefixCuratorStartEmailNotificationAction;
    private final PrefixRegistrationSessionActionEmailNotifier prefixRequesterStartEmailNotificationAction;

    @Override
    public Logger getLogger() {
        return log;
    }

    @Override
    public String getActionName() {
        return "START";
    }

    @Override
    public List<PrefixRegistrationSessionAction> buildActionSequence() {
        return Arrays.asList(
                actionLogger,
                prefixCuratorStartEmailNotificationAction,
                prefixRequesterStartEmailNotificationAction
        );
    }
}
