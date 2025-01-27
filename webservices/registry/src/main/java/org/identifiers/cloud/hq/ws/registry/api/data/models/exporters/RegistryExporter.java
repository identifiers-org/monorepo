package org.identifiers.cloud.hq.ws.registry.api.data.models.exporters;

import org.identifiers.cloud.hq.ws.registry.data.models.Namespace;

import java.util.List;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.data.models.exporters
 * Timestamp: 2018-11-15 12:57
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This is the director of the export process for the registry data
 */
public interface RegistryExporter {
    ExportedDocument export(List<Namespace> namespaces) throws RegistryExporterException;
}
