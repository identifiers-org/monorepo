package org.identifiers.cloud.hq.ws.registry.api.responses;

import org.identifiers.cloud.hq.ws.registry.data.models.Namespace;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.responses
 * Timestamp: 2018-10-16 14:23
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
public class ResolverDatasetPayload implements Serializable {
    private List<Namespace> namespaces = new ArrayList<>();

    public List<Namespace> getNamespaces() {
        return namespaces;
    }

    public ResolverDatasetPayload setNamespaces(List<Namespace> namespaces) {
        this.namespaces = namespaces;
        return this;
    }
}
