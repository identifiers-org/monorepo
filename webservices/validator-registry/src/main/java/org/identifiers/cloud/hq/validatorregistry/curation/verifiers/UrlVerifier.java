package org.identifiers.cloud.hq.validatorregistry.curation.verifiers;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.commons.urlchecking.UrlAssessment;
import org.identifiers.cloud.commons.urlchecking.UrlChecker;
import org.identifiers.cloud.commons.messages.models.CurationWarningNotification;
import org.identifiers.cloud.hq.validatorregistry.helpers.TargetEntityHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static org.identifiers.cloud.hq.validatorregistry.curation.verifiers.CuratorReviewVerifier.CURATOR_REVIEW_VALUE;

@Slf4j
@RequiredArgsConstructor
public class UrlVerifier<T> implements RegistryEntityVerifier<T> {
    public static final String NOTIFICATION_TYPE = "url-not-ok";
    private final UrlChecker urlChecker;
    private final Function<T, String> urlGetter;
    private final String fieldName;

    @Setter @Accessors(chain = true)
    private Function<T, Boolean> protectedGetter = null;

    private final HashMap<String, UrlAssessment> assessmentCache = new HashMap<>();

    @Override
    public Optional<CurationWarningNotification> validate(T entity) {
        var urlValue = urlGetter.apply(entity);

        //Curator review value is handled by its verifier
        if (CURATOR_REVIEW_VALUE.equals(urlValue)) return Optional.empty();

        boolean isProtected = protectedGetter != null && protectedGetter.apply(entity);

        log.debug("Checking URL {}", urlValue);
        var urlAssessment = assessmentCache.computeIfAbsent(urlValue, url -> urlChecker.check(url, isProtected));
        if (urlAssessment.isOk()) {
            log.debug("URL {} is OK w/ code {}", urlValue, urlAssessment.getStatusCode());
            return Optional.empty();
        } else {
            log.debug("URL {} is NOT OK w/ status {} & error {}",
                    urlValue, urlAssessment.getStatusCode(), urlAssessment.getError());
            String entityType = TargetEntityHelper.getEntityTypeOf(entity);
            long entityId = TargetEntityHelper.getEntityIdOf(entity);
            String globalId = String.format("%s:%s:%s:%s", NOTIFICATION_TYPE, entityType, fieldName, entityId);

            var notification = CurationWarningNotification.builder()
                    .type(NOTIFICATION_TYPE)
                    .details(Map.of(
                            "url", urlValue,
                            "error", urlAssessment.getError(),
                            "http-status", urlAssessment.getStatusCode().toString()))
                    .targetType(entityType)
                    .targetId(entityId)
                    .globalId(globalId);

            return Optional.of(notification.build());
        }
    }
}
