package org.identifiers.cloud.hq.validatorregistry.curation;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.commons.messages.models.CurationWarningNotification;
import org.identifiers.cloud.commons.messages.models.Institution;
import org.identifiers.cloud.commons.messages.models.Namespace;
import org.identifiers.cloud.commons.messages.models.Resource;
import org.identifiers.cloud.hq.validatorregistry.curation.verifiers.RegistryEntityVerifier;
import org.identifiers.cloud.hq.validatorregistry.helpers.StatusHelper;
import org.identifiers.cloud.hq.validatorregistry.registryhelpers.CurationWarningNotificationPoster;
import org.identifiers.cloud.hq.validatorregistry.registryhelpers.ResolutionDatasetFetcher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

@Slf4j
@Component
@RequiredArgsConstructor
public class CurationEngine {
    private final Set<RegistryEntityVerifier<Institution>> institutionValidators;
    private final Set<RegistryEntityVerifier<Namespace>> namespaceValidators;
    private final Set<RegistryEntityVerifier<Resource>> resourceValidators;
    private final ExecutorService executorService;
    private final StatusHelper statusHelper;

    private final ResolutionDatasetFetcher datasetFetcher;
    private final CurationWarningNotificationPoster curationWarningNotificationPoster;

    @Value("${org.identifiers.cloud.verifiers.namespaces.enabled}") @Setter
    private boolean namespacesEnabled;
    @Value("${org.identifiers.cloud.verifiers.resources.enabled}") @Setter
    private boolean resourcesEnabled;
    @Value("${org.identifiers.cloud.verifiers.institutions.enabled}") @Setter
    private boolean institutionsEnabled;

    @Value("${org.identifiers.cloud.verifier.engine.post-enabled}") @Setter
    private boolean posterEnabled;

    public void execute() {
        if (namespacesEnabled) runPreValidateTasks(namespaceValidators);
        if (resourcesEnabled) runPreValidateTasks(resourceValidators);
        if (institutionsEnabled) runPreValidateTasks(institutionValidators);

        var namespaces = datasetFetcher.fetch();

        var namespaceNotificationStream = getNamespaceNotificationStream(namespaces);
        var resourceNotificationStream = getResourceNotificationStream(namespaces);
        var institutionNotificationStream = getInstitutionNotificationStream(namespaces);
        var futures = Stream.of(namespaceNotificationStream,
                                resourceNotificationStream,
                                institutionNotificationStream)
                .flatMap(Function.identity())
                .parallel().toList();

        statusHelper.setTotal(futures.size());

        var allNotifications = futures.stream()
                .map(this::getFromFuture)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        executorService.shutdown();

        statusHelper.stopPeriodicReporting();

        if (posterEnabled) {
            curationWarningNotificationPoster.post(allNotifications);
        } else if (log.isInfoEnabled()){
            log.info("Poster is disabled, found {} notifications", allNotifications.size());
            var warningTypes = allNotifications.stream()
                    .map(CurationWarningNotification::getType)
                    .distinct().toList();
            log.info("Notification types generated: {}", warningTypes);
        }
    }

    private <T> void runPreValidateTasks(Set<RegistryEntityVerifier<T>> validators) {
        for (var validator : validators) {
            try {
                validator.preValidateTask();
            } catch (RuntimeException e) {
                log.error("Error when running pre validation task", e);
            }
        }
    }


    Stream<CompletableFuture<Optional<CurationWarningNotification>>> validate(Namespace namespace) {
        return namespaceValidators.parallelStream()
                .map(v ->
                        CompletableFuture.supplyAsync(
                                () -> v.validate(namespace),
                                executorService
                        )
                );
    }

    Stream<CompletableFuture<Optional<CurationWarningNotification>>> validate(Resource resource) {
        return resourceValidators.parallelStream()
                .map(v ->
                        CompletableFuture.supplyAsync(
                                () -> v.validate(resource),
                                executorService
                        )
                );
    }

    Stream<CompletableFuture<Optional<CurationWarningNotification>>> validate(Institution institution) {
        return institutionValidators.parallelStream()
                .map(v ->
                        CompletableFuture.supplyAsync(
                                () -> v.validate(institution),
                                executorService
                        )
                );
    }



    private Stream<CompletableFuture<Optional<CurationWarningNotification>>>
    getInstitutionNotificationStream(Collection<Namespace> namespaces) {
        if (institutionsEnabled) {
            return namespaces.parallelStream()
                    .filter(not(Namespace::isDeprecated))
                    .flatMap(n -> n.getResources().stream())
                    .filter(not(Resource::isDeprecated))
                    .map(Resource::getInstitution)
                    .flatMap(this::validate);
        } else {
            return Stream.empty();
        }
    }

    private Stream<CompletableFuture<Optional<CurationWarningNotification>>>
    getResourceNotificationStream(Collection<Namespace> namespaces) {
        if (resourcesEnabled) {
            return namespaces.parallelStream()
                    .filter(not(Namespace::isDeprecated))
                    .flatMap(n -> n.getResources().stream())
                    .filter(not(Resource::isDeprecated))
                    .flatMap(this::validate);
        } else {
            return Stream.empty();
        }
    }

    private Stream<CompletableFuture<Optional<CurationWarningNotification>>>
    getNamespaceNotificationStream(Collection<Namespace> namespaces) {
        if (namespacesEnabled) {
            return namespaces.parallelStream()
                    .filter(not(Namespace::isDeprecated))
                    .flatMap(this::validate);
        } else {
            return Stream.empty();
        }
    }

    private <T> Optional<T> getFromFuture(CompletableFuture<Optional<T>> future) {
        try {
            return future.join();
        } catch (CancellationException | CompletionException e) {
            log.error("Error during validation task", e);
            return Optional.empty();
        }
    }
}
