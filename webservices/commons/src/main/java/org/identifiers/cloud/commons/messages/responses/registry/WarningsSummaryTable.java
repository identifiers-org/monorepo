package org.identifiers.cloud.commons.messages.responses.registry;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class WarningsSummaryTable extends LinkedList<WarningsSummaryTable.Row> implements Serializable {
    @Serial
    private static final long serialVersionUID = 9217375570885513090L;

    @Data
    public static class Row implements Serializable {
        @Serial
        private static final long serialVersionUID = -1691596847001425546L;

        TargetInfo targetInfo;
        int accessNumber = 0;
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
