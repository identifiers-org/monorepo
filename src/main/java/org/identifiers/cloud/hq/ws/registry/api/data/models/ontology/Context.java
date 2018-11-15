package org.identifiers.cloud.hq.ws.registry.api.data.models.ontology;

import java.io.Serializable;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.data.models.ontology
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
    private String dc = "";
    private String rdfs = "";
    private String foaf = "";
    private String owl = "";
    private String skos = "";
}
