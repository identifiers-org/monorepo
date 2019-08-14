package org.identifiers.cloud.hq.ws.registry.api.data.models.rororg;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.data.models.rororg
 * Timestamp: 2019-08-14 17:15
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Organization implements Serializable {
    private String id;
    private String name;
    private List<String> types = new ArrayList<>();
    private List<String> aliases = new ArrayList<>();
    private List<String> acronyms = new ArrayList<>();
    @JsonAlias({"wikipedia_url"})
    private String wikipediaUrl;
    // Country
    private Country country;
    // TODO ISNI
    // TODO OrgRef
    // TODO Wikidata
    // GRID
    @JsonAlias({"GRID"})
    private GridId grid;
}
