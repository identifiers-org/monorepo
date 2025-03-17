package org.identifiers.cloud.commons.messages.responses.registry;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Data
public class WarningsSummaryPayload implements Serializable {
    @Serial
    private static final long serialVersionUID = 9217375570885513090L;

    Map<String, Long> namespaceUsage;

    List<Entry> summaryEntries = new LinkedList<>();

    @Data
    public static class Entry implements Serializable {
        @Serial
        private static final long serialVersionUID = -1691596847001425546L;

        TargetInfo targetInfo;
        int failingInstitutionUrl = 0;
        int lowAvailabilityResources = 0;
        boolean hasCurationValues = false;
        boolean hasPossibleWikidataError = false;
    }

    @Data
    @Accessors(chain = true)
    public static class TargetInfo implements Serializable {
        @Serial
        private static final long serialVersionUID = -8920431359177208195L;

        String type;
        String identifier;
        String label;
    }
}
