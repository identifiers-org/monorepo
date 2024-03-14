package org.identifiers.cloud.hq.ws.registry.api.data.models.exporters.ontology;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.identifiers.cloud.hq.ws.registry.api.data.models.exporters.ExportedDocument;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.data.models.exporters.ontology
 * Timestamp: 2018-11-15 12:33
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
public class JsonLdDocument extends ExportedDocument implements Serializable {
    @JsonProperty(value = "@context")
    private Map<String, String> contexts = new HashMap<>();
    @JsonProperty(value = "@graph")
    private final List<Serializable> graphEntries = new ArrayList<>();

    public String addContext(String key, String value) {
        return getContexts().put(key, value);
    }

    public void addEntry(Serializable entry) {
        graphEntries.add(entry);
    }

    public Map<String, String> getContexts() {
        return contexts;
    }

    public JsonLdDocument setContexts(Map<String, String> contexts) {
        this.contexts = contexts;
        return this;
    }
}
