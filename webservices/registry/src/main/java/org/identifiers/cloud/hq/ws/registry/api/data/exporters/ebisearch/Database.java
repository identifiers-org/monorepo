package org.identifiers.cloud.hq.ws.registry.api.data.exporters.ebisearch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.identifiers.cloud.hq.ws.registry.api.data.exporters.ExportedDocument;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Database extends ExportedDocument {
    String name;
    String description;
    int entry_count;
    List<Entry> entries;
}
