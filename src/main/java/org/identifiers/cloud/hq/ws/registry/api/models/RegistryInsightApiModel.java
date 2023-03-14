package org.identifiers.cloud.hq.ws.registry.api.models;

import org.identifiers.cloud.hq.ws.registry.api.data.models.exporters.ExportedDocument;
import org.identifiers.cloud.hq.ws.registry.api.data.models.exporters.ebisearch.EbiSearchExporter;
import org.identifiers.cloud.hq.ws.registry.data.models.Namespace;
import org.identifiers.cloud.hq.ws.registry.data.services.NamespaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.models
 * Timestamp: 2019-08-21 02:02
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Component
public class RegistryInsightApiModel {
    @Autowired
    private NamespaceService namespaceService;

    @Autowired
    private EbiSearchExporter ebiSearchExporter;

    public ResponseEntity<?> getAllNamespacePrefixes() {
        return new ResponseEntity<>(namespaceService.getAllNamespacePrefixes(), HttpStatus.OK);
    }

    public ExportedDocument getEbiSearchExport(Date start) {
        List<Namespace> namespaces;
        if (start != null)
            namespaces = namespaceService.findNamespacesModifiedSince(start);
        else
            namespaces = namespaceService.getAllNamespaces();

        if (namespaces.isEmpty())
            return null;
        else
            return ebiSearchExporter.export(namespaces);
    }
}
