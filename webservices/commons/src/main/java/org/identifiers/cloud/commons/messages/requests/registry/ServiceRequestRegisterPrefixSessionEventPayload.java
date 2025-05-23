package org.identifiers.cloud.commons.messages.requests.registry;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.requests
 * Timestamp: 2019-03-29 10:34
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This model represents the superset of information needed for every event within a prefix registration session.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceRequestRegisterPrefixSessionEventPayload implements Serializable {
    // This is for all kinds of events
    private String additionalInformation;

    // This information is for comments
    private String comment;

    // This information is for 'accept' event
    private String acceptanceReason;

    // This information is for 'reject' event
    private String rejectionReason;

    // This information is mainly for 'amend' event, but it could be used by any event
    private ServiceRequestRegisterPrefixPayload prefixRegistrationRequest;
}
