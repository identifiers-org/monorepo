package org.identifiers.cloud.hq.ws.registry.models.rororg;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models.rororg
 * Timestamp: 2019-08-14 17:25
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * ror.org "country" data model
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Country {
    @JsonAlias({"country_name"})
    private String countryName;
    @JsonAlias({"country_code"})
    private String countryCode;
}
