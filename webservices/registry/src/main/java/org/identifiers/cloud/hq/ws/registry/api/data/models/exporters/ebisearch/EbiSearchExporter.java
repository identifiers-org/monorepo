package org.identifiers.cloud.hq.ws.registry.api.data.models.exporters.ebisearch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.hq.ws.registry.api.data.models.exporters.ExportedDocument;
import org.identifiers.cloud.hq.ws.registry.api.data.models.exporters.RegistryExporter;
import org.identifiers.cloud.hq.ws.registry.api.data.models.exporters.RegistryExporterException;
import org.identifiers.cloud.hq.ws.registry.data.models.Namespace;
import org.identifiers.cloud.hq.ws.registry.models.SchemaOrgMetadataProvider;
import org.identifiers.cloud.hq.ws.registry.models.schemaorg.DataCatalog;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class EbiSearchExporter implements RegistryExporter {
    final SchemaOrgMetadataProvider schemaOrgMetadataProvider;

    @Override
    public ExportedDocument export(List<Namespace> namespaces) throws RegistryExporterException {
        try {
            Database exportDatabase = new Database();
            DataCatalog idorgMetadata = (DataCatalog) schemaOrgMetadataProvider.getForPlatform();

            exportDatabase.setName(idorgMetadata.getName())
                    .setDescription(idorgMetadata.getDescription())
                    .setEntry_count(namespaces.size());

            List<Entry> entries = namespaces.stream()
                    .map(this::getEntryFromNamespace)
                    .toList();
            exportDatabase.setEntries(entries);
            return exportDatabase;
        } catch (RuntimeException ex) {
            log.error("Unexpected exception when exporting to EBI search", ex);
            throw new RegistryExporterException(ex.getMessage());
        }
    }

    private Entry getEntryFromNamespace(Namespace namespace) {
        Entry newEntry = new Entry();
        
        List<Field> fields = getFieldsOf(namespace);
        newEntry.setFields(fields);
        
        List<Ref> references = Collections.emptyList();
        newEntry.setCross_references(references);
        
        return newEntry;
    }

    final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    private String format(Date date) {
        return df.format(date);
    }
    private String format(Object bool) {
        return String.valueOf(bool);
    }

    private String genLandingPageUrl (Namespace namespace) {
        return String.format("https://identifiers.org/%s", namespace.getPrefix());
    }

    private List<Field> getFieldsOf(Namespace namespace) {
        List<Field> fields = new LinkedList<>();

        fields.add(new Field("id", format(namespace.getId())));
        fields.add(new Field("name", namespace.getName()));
        fields.add(new Field("description", namespace.getDescription()));
        fields.add(new Field("prefix", namespace.getPrefix()));
        fields.add(new Field("mir_id", namespace.getMirId()));
        fields.add(new Field("lui_pattern", namespace.getPattern()));
        fields.add(new Field("sample_id", namespace.getSampleId()));

        fields.add(new Field("creation_date", format(namespace.getCreated())));
        fields.add(new Field("modification_date", format(namespace.getModified())));

        fields.add(new Field("is_deprecated", format(namespace.isDeprecated())));
        if (namespace.isDeprecated()) {
            fields.add(new Field("deprecation_date", format(namespace.getDeprecationDate())));
        }

        fields.add(new Field("landing_page", genLandingPageUrl(namespace)));

        for (var resource : namespace.getResources()) {
            fields.add(new Field("mir_id", resource.getMirId()));
            fields.add(new Field("resource_name", resource.getName()));
            fields.add(new Field("resource_description", resource.getDescription()));
            fields.add(new Field("institution_name", resource.getInstitution().getName()));
            fields.add(new Field("institution_description", resource.getInstitution().getDescription()));
        }

        return fields.stream().filter(f -> !f.getValue().equals("CURATOR_REVIEW")).toList();
    }
}
