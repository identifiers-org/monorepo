package org.identifiers.cloud.hq.ws.registry.api.requests;

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
 * Timestamp: 2019-07-25 13:24
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceRequestRegisterResourceSessionEventPayload implements Serializable {
    // This is for all kinds of events
    private String additionalInformation;

    // This information is for comments
    private String comment;

    // This information is for 'accept' event
    private String acceptanceReason;

    // This information is for 'reject' event
    private String rejectionReason;

    // This information is mainly for 'amend' event, but it could be used by any event
    private ServiceRequestRegisterResourcePayload resourceRegistrationRequest;
}
