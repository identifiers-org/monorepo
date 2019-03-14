package org.identifiers.cloud.hq.ws.registry.models.validators;

import org.identifiers.cloud.hq.ws.registry.api.data.models.Requester;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models.validators
 * Timestamp: 2019-03-14 15:01
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This interface models the contract every validator around a 'requester' data model should fullfil.
 */
public interface RequesterValidator {
    boolean validate(Requester requester) throws RequesterValidatorException;
}
