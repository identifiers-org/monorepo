package org.identifiers.cloud.ws.resolver.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: libapi
 * Package: org.identifiers.cloud.ws.resolver.models.api.responses
 * Timestamp: 2018-03-07 7:28
 * ---
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
public class ResolvedResource implements Serializable {
    private String id;
    private String resourcePrefix;
    private String accessUrl;
    private String info;
    private String institution;
    private String location;
    private boolean official;
    private String resourceURL;
    private Recommendation recommendation;
}
