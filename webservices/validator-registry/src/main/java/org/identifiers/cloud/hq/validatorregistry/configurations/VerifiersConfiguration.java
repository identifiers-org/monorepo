package org.identifiers.cloud.hq.validatorregistry.configurations;

import lombok.RequiredArgsConstructor;
import org.identifiers.cloud.commons.messages.models.Institution;
import org.identifiers.cloud.commons.messages.models.Namespace;
import org.identifiers.cloud.commons.messages.models.Resource;
import org.identifiers.cloud.commons.urlchecking.UrlChecker;
import org.identifiers.cloud.hq.validatorregistry.curation.verifiers.CuratorReviewVerifier;
import org.identifiers.cloud.hq.validatorregistry.curation.verifiers.RegistryEntityVerifier;
import org.identifiers.cloud.hq.validatorregistry.curation.verifiers.UrlVerifier;
import org.identifiers.cloud.hq.validatorregistry.helpers.StatusHelper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class VerifiersConfiguration {
    private final StatusHelper statusHelper;

    @Bean
    @ConditionalOnProperty(value = {
            "org.identifiers.cloud.verifiers.curator-review.enabled",
            "org.identifiers.cloud.verifiers.curator-review.enabled.namespace.name"
    }, matchIfMissing = true)
    RegistryEntityVerifier<Namespace> namespaceNameCuratorReviewVerifier() {
        return new CuratorReviewVerifier<>("name", Namespace::getName, statusHelper);
    }
    @Bean
    @ConditionalOnProperty(value = {
            "org.identifiers.cloud.verifiers.curator-review.enabled",
            "org.identifiers.cloud.verifiers.curator-review.enabled.namespace.description"
    }, matchIfMissing = true)
    RegistryEntityVerifier<Namespace> namespaceDescriptionCuratorReviewVerifier() {
        return new CuratorReviewVerifier<>("description", Namespace::getDescription, statusHelper);
    }


    @Bean
    @ConditionalOnProperty(value = {
            "org.identifiers.cloud.verifiers.curator-review.enabled",
            "org.identifiers.cloud.verifiers.curator-review.enabled.resource.name"
    }, matchIfMissing = true)
    RegistryEntityVerifier<Resource> resourceNameCuratorReviewVerifier() {
        return new CuratorReviewVerifier<>("name", Resource::getName, statusHelper);
    }
    @Bean
    @ConditionalOnProperty(value = {
            "org.identifiers.cloud.verifiers.curator-review.enabled",
            "org.identifiers.cloud.verifiers.curator-review.enabled.resource.description"
    }, matchIfMissing = true)
    RegistryEntityVerifier<Resource> resourceDescriptionCuratorReviewVerifier() {
        return new CuratorReviewVerifier<>("description", Resource::getDescription, statusHelper);
    }
    @Bean
    @ConditionalOnProperty(value = {
            "org.identifiers.cloud.verifiers.curator-review.enabled",
            "org.identifiers.cloud.verifiers.curator-review.enabled.resource.home-url"
    }, matchIfMissing = true)
    RegistryEntityVerifier<Resource> resourceHomeUrlCuratorReviewVerifier() {
        return new CuratorReviewVerifier<>("home-url", Resource::getResourceHomeUrl, statusHelper);
    }
    @Bean
    @ConditionalOnProperty(value = {
            "org.identifiers.cloud.verifiers.curator-review.enabled",
            "org.identifiers.cloud.verifiers.curator-review.enabled.resource.provider-code"
    }, matchIfMissing = true)
    RegistryEntityVerifier<Resource> resourceProviderCodeCuratorReviewVerifier() {
        return new CuratorReviewVerifier<>("provider-code", Resource::getProviderCode, statusHelper);
    }

    @Bean
    @ConditionalOnProperty(value = {
            "org.identifiers.cloud.verifiers.curator-review.enabled",
            "org.identifiers.cloud.verifiers.curator-review.enabled.institution.name"
    }, matchIfMissing = true)
    RegistryEntityVerifier<Institution> institutionNameCuratorReviewVerifier() {
        return new CuratorReviewVerifier<>("name", Institution::getName, statusHelper);
    }
    @Bean
    @ConditionalOnProperty(value = {
            "org.identifiers.cloud.verifiers.curator-review.enabled",
            "org.identifiers.cloud.verifiers.curator-review.enabled.institution.description"
    }, matchIfMissing = true)
    RegistryEntityVerifier<Institution> institutionDescriptionCuratorReviewVerifier() {
        return new CuratorReviewVerifier<>("description", Institution::getDescription, statusHelper);
    }
    @Bean
    @ConditionalOnProperty(value = {
            "org.identifiers.cloud.verifiers.curator-review.enabled",
            "org.identifiers.cloud.verifiers.curator-review.enabled.institution.home-url"
    }, matchIfMissing = true)
    RegistryEntityVerifier<Institution> institutionHomeUrlCuratorReviewVerifier() {
        return new CuratorReviewVerifier<>("home-url", Institution::getHomeUrl, statusHelper);
    }





    @Bean
    @ConditionalOnProperty("org.identifiers.cloud.verifiers.url-verifier.enabled")
    RegistryEntityVerifier<Institution> institutionHomePageVerifier(UrlChecker urlChecker) {
        return new UrlVerifier<>(statusHelper, urlChecker, Institution::getHomeUrl, "home-url");
    }

    // These are done by the availability verifier
//    @Bean
//    RegistryEntityVerifier<Resource> resourceHomePageVerifier(UrlChecker urlChecker) {
//        return new UrlVerifier<>(urlChecker, Resource::getResourceHomeUrl, "home-url");
//    }
//    @Bean
//    RegistryEntityVerifier<Resource> resourceSampleUrlVerifier(UrlChecker urlChecker) {
//        Function<Resource, String> sampleUrlGetter = r -> {
//            var urlPattern = r.getUrlPattern();
//            var sampleId = r.getSampleId();
//            return urlPattern.replace("{$id}", sampleId);
//        };
//        return new UrlVerifier<>(urlChecker, sampleUrlGetter, "sample-url")
//                .setProtectedGetter(Resource::isProtectedUrls);
//    }
}
