package org.identifiers.cloud.hq.ws.registry.api.data.models.exporters.ontology;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.identifiers.cloud.hq.ws.registry.api.data.models.exporters.ExportedDocument;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.data.models.exporters.ontology
 * Timestamp: 2018-11-15 12:33
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
public class jsonLdDocument extends ExportedDocument implements Serializable {
    @JsonProperty(value = "context")
    private Map<String, String> contexts = new HashMap<>();
}
