package org.identifiers.cloud.hq.ws.registry.api.data.models.exporters.ebisearch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
public class Ref {
    String dbkey;
    String dbname;
}
