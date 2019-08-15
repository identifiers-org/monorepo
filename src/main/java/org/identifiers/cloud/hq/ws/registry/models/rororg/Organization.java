package org.identifiers.cloud.hq.ws.registry.models.rororg;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
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
@JsonIgnoreProperties(ignoreUnknown = true, value = {"KEY_EXTERNAL_ID_ISNI", "KEY_EXTERNAL_ID_ORGREF", "KEY_EXTERNAL_ID_WIKIDATA", "KEY_EXTERNAL_ID_GRID", "isni", "orgRef", "wikidata", "grid"})
@Slf4j
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
    private Map<String, Object> externalIds;
    // Made up
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private OrganizationIsniInformation isni;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private OrganizationOrgRefInformation orgRef;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private OrganizationWikidataInformation wikidata;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private OrganizationGridIdInfo grid;

    // Tricky getters
    @JsonIgnore
    public OrganizationIsniInformation getIsni() {
        if (isni == null) {
            if (externalIds.get(KEY_EXTERNAL_ID_ISNI) != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    isni = objectMapper.readValue(objectMapper.writeValueAsString(externalIds.get(KEY_EXTERNAL_ID_ISNI)), OrganizationIsniInformation.class);
                } catch (IOException e) {
                    log.error(String.format("Could NOT build ISNI from existing information in Organization with ROR ID '%s'", id));
                }
            }
        }
        return isni;
    }
}
