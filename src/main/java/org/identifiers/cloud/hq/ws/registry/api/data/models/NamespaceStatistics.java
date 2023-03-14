package org.identifiers.cloud.hq.ws.registry.api.data.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
public class NamespaceStatistics implements Serializable {
    int nb_uniq_visitors;
    int nb_visits;
}
