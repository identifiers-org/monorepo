package org.identifiers.cloud.hq.ws.miridcontroller.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Project: mirid-controller
 * Package: org.identifiers.cloud.hq.ws.miridcontroller.models
 * Timestamp: 2019-02-28 10:26
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This models a report on how the MIR ID Management strategy completed the requested operation. This report will allow
 * clients to get insight informatino for more detailed error reporting back to requester
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class MirIdManagementStrategyOperationReport {
    // TODO
    public enum Status {
        BAD_REQUEST;
    }

    private Status status;
    private String reportContent;
}
