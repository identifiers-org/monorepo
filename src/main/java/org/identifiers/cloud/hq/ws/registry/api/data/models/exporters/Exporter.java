package org.identifiers.cloud.hq.ws.registry.api.data.models.exporters;

import org.identifiers.cloud.hq.ws.registry.data.models.Namespace;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.data.models.exporters
 * Timestamp: 2018-11-15 12:20
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
public interface Exporter {

    void build(Namespace namespace) throws ExporterException;

    ExportedDocument getDocument() throws ExporterException;
}
