package org.identifiers.cloud.commons.messages.responses.linkchecker;

import java.util.LinkedList;

public class ServiceResponseResourceAvailabilityPayload extends
        LinkedList<ServiceResponseResourceAvailabilityPayload.Item> {

    public record Item(
            long resourceId,
            Float availability
    ) {}
}
