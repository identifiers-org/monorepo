package org.identifiers.cloud.hq.ws.registry.api.data.exporters.ontology;

import org.identifiers.cloud.hq.ws.registry.api.data.exporters.ExportDocumentBuilder;

import java.io.Serializable;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.data.models.exporters.ontology
 * Timestamp: 2018-11-15 13:05
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
// TODO - Affected by relational refactoring
public interface ExportOntologyDocumentBuilder extends ExportDocumentBuilder {
    void buildContext(String namespace, String reference) throws ExportOntologyDocumentBuilderException;
    void build(Serializable entry) throws ExportOntologyDocumentBuilderException;
}
