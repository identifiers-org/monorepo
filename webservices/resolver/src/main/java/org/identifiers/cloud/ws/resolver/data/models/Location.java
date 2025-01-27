package org.identifiers.cloud.ws.resolver.data.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Project: resolver
 * Package: org.identifiers.cloud.ws.resolver.data.models
 * Timestamp: 2019-04-02 11:28
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
public class Location implements Serializable {
    private String countryCode;
    private String countryName;
}