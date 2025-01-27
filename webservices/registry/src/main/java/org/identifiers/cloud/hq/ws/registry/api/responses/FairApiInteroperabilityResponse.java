package org.identifiers.cloud.hq.ws.registry.api.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * Project: idorg-hq-registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.responses
 * Timestamp: 2020-11-10 11:33
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 * <p>
 * Data model for the FAIR API Interoperability responses
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(value = {"httpStatus"})
public class FairApiInteroperabilityResponse implements Serializable {
    private HttpStatus httpStatus = HttpStatus.OK;
    // This is the response / result
    private String response;
    // Optionally, additional information can be sent back with the response
    private String additionalInformation;
}
