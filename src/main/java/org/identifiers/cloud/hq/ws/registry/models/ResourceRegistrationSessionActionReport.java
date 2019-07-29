package org.identifiers.cloud.hq.ws.registry.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models
 * Timestamp: 2019-07-29 11:50
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ResourceRegistrationSessionActionReport {
    // Whether the action completed successfully or not
    private boolean success = true;
    // Error message that can be provided upon unsuccessful completion of action
    private String errorMessage = "--- No error message set ---";
    // Additional Information to action completion, optional
    private String additionalInformation = "--- No additional information set ---";

    public boolean isError() {
        return !isSuccess();
    }
}
