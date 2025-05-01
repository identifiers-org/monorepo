package org.identifiers.cloud.ws.sparql.data;

import org.identifiers.cloud.commons.messages.responses.registry.ResolverDatasetPayload;

public interface ResolverDatasetSubscriber {
    void receive(ResolverDatasetPayload resolverDatasetPayload);
}
