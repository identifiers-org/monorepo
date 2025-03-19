package org.identifiers.cloud.hq.validatorregistry.curation.verifiers;

import lombok.AllArgsConstructor;
import org.identifiers.cloud.commons.messages.models.CurationWarningNotification;
import org.identifiers.cloud.hq.validatorregistry.helpers.StatusHelper;

import java.util.Optional;

@AllArgsConstructor
public abstract class RegistryEntityVerifier<T> {
    private final StatusHelper statusHelper;

    public Optional<CurationWarningNotification> validate(T entity) {
        statusHelper.startTask();
        var out = doValidate(entity);
        statusHelper.finishTask();
        return out;
    }

    protected abstract Optional<CurationWarningNotification> doValidate(T entity);

    public void preValidateTask() {
        // Do nothing by default
    }
}
