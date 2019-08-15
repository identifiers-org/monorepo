package org.identifiers.cloud.hq.ws.registry.models.rororg;

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
import java.util.Map;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models.rororg
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
@JsonIgnoreProperties(ignoreUnknown = true, value = {"KEY_EXTERNAL_ID_ISNI", "KEY_EXTERNAL_ID_ORGREF", "KEY_EXTERNAL_ID_WIKIDATA", "KEY_EXTERNAL_ID_GRID"})
public class Organization implements Serializable {
    // Unmapped property keys
    private static final String KEY_EXTERNAL_ID_ISNI = "ISNI";
    private static final String KEY_EXTERNAL_ID_ORGREF = "OrgRef";
    private static final String KEY_EXTERNAL_ID_WIKIDATA = "Wikidata";
    private static final String KEY_EXTERNAL_ID_GRID = "GRID";

    private String id;
    private String name;
    private List<String> types = new ArrayList<>();
    private List<String> aliases = new ArrayList<>();
    private List<String> acronyms = new ArrayList<>();
    private List<String> links = new ArrayList<>();
    @JsonAlias({"wikipedia_url"})
    private String wikipediaUrl;
    // Country
    private Country country;
    // TODO Wire in external IDs, although I may not use them
    @JsonAlias({"external_ids"})
    private Map<String, String> externalIds;
}
