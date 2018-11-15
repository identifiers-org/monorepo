package org.identifiers.cloud.hq.ws.registry.api.data.models.exporters.ontology;

import org.identifiers.cloud.hq.ws.registry.api.data.models.exporters.ExportedDocument;
import org.identifiers.cloud.hq.ws.registry.api.data.models.exporters.Exporter;
import org.identifiers.cloud.hq.ws.registry.api.data.models.exporters.ExporterException;
import org.identifiers.cloud.hq.ws.registry.data.models.Namespace;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.data.models.exporters.ontology
 * Timestamp: 2018-11-15 12:20
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
public class JsonLdExporter implements Exporter {
    private
    @Override
    public void build(Namespace namespace) throws ExporterException {

    }

    @Override
    public ExportedDocument getDocument() throws ExporterException {
        return null;
    }
}
