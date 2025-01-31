package org.identifiers.cloud.commons.messages.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
public class Institution implements Serializable {
    // identifiers.org internal ID for this institution
    private long id;
    private String name;
    private String homeUrl;
    private String description;
    private String rorId;
    // Geographical location for this institution
    private Location location;
}
