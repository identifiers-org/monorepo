package org.identifiers.cloud.hq.ws.registry.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models.validators
 * Timestamp: 2019-03-25 11:04
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This is a report built by any prefix registration session action after running.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
public class PrefixRegistrationSessionActionReport {
    // Whether the action completed successfully or not
    private boolean success = true;
    // Error message that can be provided upon unsuccessful completion of action
    private String errorMessage = "--- No error message set ---";
    // Additional Information to action completion, optional
    private String additionalInformation = "--- No additional information set ---";
}
