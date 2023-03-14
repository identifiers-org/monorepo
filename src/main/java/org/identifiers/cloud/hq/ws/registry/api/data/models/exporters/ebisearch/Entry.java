package org.identifiers.cloud.hq.ws.registry.api.data.models.exporters.ebisearch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
public class Entry {
    String id;
    String description;
    List<Ref> cross_references;
    List<Field> fields;
}
