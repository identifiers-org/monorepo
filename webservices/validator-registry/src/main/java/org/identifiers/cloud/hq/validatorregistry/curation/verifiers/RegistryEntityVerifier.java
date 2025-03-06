package org.identifiers.cloud.hq.validatorregistry.curation.verifiers;

import org.identifiers.cloud.commons.messages.models.CurationWarningNotification;

import java.util.Optional;

public interface RegistryEntityVerifier<T> {
    Optional<CurationWarningNotification> validate(T entity);

    default void preValidateTask() {
        // Do nothing by default
    }
}
