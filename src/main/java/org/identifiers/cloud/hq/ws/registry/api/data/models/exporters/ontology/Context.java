package org.identifiers.cloud.hq.ws.registry.api.data.models.exporters.ontology;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.data.models.exporters.ontology
 * Timestamp: 2018-11-15 12:10
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This is the first ontological view we implement for the registry dataset, it's gonna be a quick dirty hack, maybe, to
 * get the prototype up and running by the end of the day, and, in the future, all this should be refactored with the
 * concept of exporters or multiple views of the registry.
 */
public class Context implements Serializable {
    private Map<String, String> contexts = new HashMap<>();

    public Context() {
        contexts.put("dc", "http://purl.org/dc/terms/");
        contexts.put("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
        contexts.put("foaf", "http://xmlns.com/foaf/0.1/");
        contexts.put("owl", "http://www.w3.org/2002/07/owl#");
        contexts.put("skos", "http://www.w3.org/2004/02/skos/core#");
    }

}
