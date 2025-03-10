package org.identifiers.cloud.hq.validatorregistry.curation.verifiers;

import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.commons.messages.models.CurationWarningNotification;
import org.identifiers.cloud.hq.validatorregistry.helpers.StatusHelper;
import org.identifiers.cloud.hq.validatorregistry.helpers.TargetEntityHelper;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
public class CuratorReviewVerifier<T> extends RegistryEntityVerifier<T> {
    public static final String CURATOR_REVIEW_VALUE = "CURATOR_REVIEW";
    private static final String NOTIFICATION_TYPE = "curator-review";

    private final String fieldName;
    private final Function<T, String> valueGetter;

    public CuratorReviewVerifier(String fieldName, Function<T, String> valueGetter, StatusHelper statusHelper) {
        super(statusHelper);
        this.fieldName = fieldName;
        this.valueGetter = valueGetter;
    }

    @Override
    public Optional<CurationWarningNotification> doValidate(T entity) {
        String value = valueGetter.apply(entity);
        if (CURATOR_REVIEW_VALUE.equals(value)) {
            String entityType = TargetEntityHelper.getEntityTypeOf(entity);
            long entityId = TargetEntityHelper.getEntityIdOf(entity);

            log.debug("Value {} of {} {} is set to {}", fieldName, entityType, entityId, CURATOR_REVIEW_VALUE);

            String globalId = String.format("%s:%s:%s:%s", NOTIFICATION_TYPE, entityType, fieldName, entityId);
            var notification = CurationWarningNotification.builder()
                    .globalId(globalId).type(NOTIFICATION_TYPE)
                    .targetType(entityType).targetId(entityId)
                    .details(Map.of("field-name", fieldName))
                    .build();
            return Optional.of(notification);
        } else {
            return Optional.empty();
        }
    }
}
