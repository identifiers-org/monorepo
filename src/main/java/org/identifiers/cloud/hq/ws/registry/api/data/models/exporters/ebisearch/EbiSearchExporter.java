package org.identifiers.cloud.hq.ws.registry.api.data.models.exporters.ebisearch;

import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.hq.ws.registry.api.data.models.exporters.ExportedDocument;
import org.identifiers.cloud.hq.ws.registry.api.data.models.exporters.RegistryExporter;
import org.identifiers.cloud.hq.ws.registry.api.data.models.exporters.RegistryExporterException;
import org.identifiers.cloud.hq.ws.registry.data.models.Namespace;
import org.identifiers.cloud.hq.ws.registry.models.SchemaOrgMetadataProvider;
import org.identifiers.cloud.hq.ws.registry.models.schemaorg.DataCatalog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class EbiSearchExporter implements RegistryExporter {
    @Autowired
    SchemaOrgMetadataProvider schemaOrgMetadataProvider;

    @Override
    public ExportedDocument export(List<Namespace> namespaces) throws RegistryExporterException {
        try {
            Database exportDatabase = new Database();
            DataCatalog idorg_metadata = (DataCatalog) schemaOrgMetadataProvider.getForPlatform();

            exportDatabase.setName(idorg_metadata.getName())
                    .setDescription(idorg_metadata.getDescription())
                    .setEntry_count(namespaces.size());

            List<Entry> entries = namespaces.stream()
                    .map(this::getEntryFromNamespace)
                    .collect(Collectors.toList());
            exportDatabase.setEntries(entries);
            return exportDatabase;
        } catch (RuntimeException ex) {
            log.error("Unexpected exception when exporting to EBI search", ex);
            throw new RegistryExporterException(ex.getMessage());
        }
    }

    private Entry getEntryFromNamespace(Namespace namespace) {
        Entry newEntry = new Entry();
        
        List<Field> fields = getFieldsOfNamespace(namespace);
        newEntry.setFields(fields);
        
        List<Ref> references = Collections.emptyList();
        newEntry.setCross_references(references);
        
        return newEntry;
    }

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    private String format(Date date) {
        return df.format(date);
    }
    private String format(Object bool) {
        return String.valueOf(bool);
    }

    private String genLandingPageUrl (Namespace namespace) {
        return String.format("https://identifiers.org/%s", namespace.getPrefix());
    }

    private List<Field> getFieldsOfNamespace(Namespace namespace) {
        List<Field> fields = new LinkedList<>();

        fields.add(new Field("id", format(namespace.getId())));
        fields.add(new Field("name", namespace.getName()));
        fields.add(new Field("description", namespace.getDescription()));
        fields.add(new Field("prefix", namespace.getPrefix()));
        fields.add(new Field("mir_id", namespace.getMirId()));

        fields.add(new Field("creation_date", format(namespace.getCreated())));
        fields.add(new Field("modification_date", format(namespace.getModified())));

        fields.add(new Field("is_deprecated", format(namespace.isDeprecated())));
        if (namespace.isDeprecated()) {
            fields.add(new Field("deprecation_date", format(namespace.getDeprecationDate())));
        }

        fields.add(new Field("landing_page", genLandingPageUrl(namespace)));

        return fields;
    }
}
