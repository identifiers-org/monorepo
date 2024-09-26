package org.identifiers.cloud.hq.ws.registry.configuration;

import lombok.NonNull;
import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefixPayload;
import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterResourcePayload;
import org.identifiers.cloud.hq.ws.registry.data.services.NamespaceService;
import org.identifiers.cloud.hq.ws.registry.data.services.ResourceService;
import org.identifiers.cloud.hq.ws.registry.models.validators.*;
import org.identifiers.cloud.hq.ws.registry.models.validators.payload.*;
import org.identifiers.cloud.hq.ws.registry.models.validators.singlevalue.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;

@Configuration
public class ValidatorsConfiguration {
    final int minDescriptionLength;
    final NamespaceService namespaceService;
    final ResourceService resourceService;
    final RestTemplate urlValidatorRestTemplate;
    public ValidatorsConfiguration(@Value("${org.identifiers.cloud.hq.ws.registry.validation.mindescriptionlength}")
                                   int minDescriptionLength,
                                   RestTemplateBuilder restTemplateBuilder,
                                   NamespaceService namespaceService,
                                   ResourceService resourceService) {
        this.minDescriptionLength = minDescriptionLength;
        this.namespaceService = namespaceService;
        this.urlValidatorRestTemplate = restTemplateBuilder
                .errorHandler(new NoopErrorHandler())
                .setConnectTimeout(Duration.ofMinutes(2))
                .setReadTimeout(Duration.ofMinutes(2))
                .build();
        this.resourceService = resourceService;
    }





    @Bean
    public SingleValueValidator isNotBlankValidator() {
        return new IsNotBlankValidator();
    }

    @Bean
    public SingleValueValidator minLengthStringValidator() {
        return new MinLengthValidator(minDescriptionLength);
    }

    @Bean
    public SingleValueValidator locationCodeStringValidator() {
        return new LocationCodeValidator();
    }

    @Bean
    public RegistrationPayloadValidator providerCodeValueValidator() {
        return new ProviderCodeValueValidator(namespaceService);
    }

    @Bean
    public RegistrationPayloadValidator prefixValueValidator() {
        return new PrefixStringValidator(namespaceService);
    }

    @Bean
    public UrlValidator urlValidator() {
        return new UrlValidator(urlValidatorRestTemplate);
    }

    @Bean
    public RegistrationPayloadValidator sampleUrlRequestValidator() {
        return new SampleUrlRequestValidator(urlValidator());
    }

    @Bean
    public RegistrationPayloadValidator sampleIdMatchesPatternValidator() {
        return new SampleIdMatchesPatternValidator(namespaceService);
    }

    @Bean
    public SingleValueValidator emailStringValidator() {
        return new EmailValidator();
    }

    @Bean
    public SingleValueValidator regexSyntaxStringValidator() {
        return new RegexSyntaxValidator();
    }

    @Bean
    public SingleValueValidator urlPatternContainsIdTemplateValidator() {
        return new UrlPatternContainsIdTemplateValidator();
    }

    @Bean
    public RegistrationPayloadValidator similarUrlTemplateValidator() {
        return new SimilarUrlPatternValidator(resourceService);
    }




    @Bean("namespaceName")
    public RegistrationValidationChain namespaceNameValidatorChain() {
        return new RegistrationValidationChain(
                getLabelFor("namespaceName"),
                ServiceRequestRegisterPrefixPayload::getName, null,
                isNotBlankValidator());
    }

    @Bean("namespaceDescription")
    public RegistrationValidationChain namespaceDescriptionValidatorChain() {
        return new RegistrationValidationChain(
                getLabelFor("namespaceDescription"),
                ServiceRequestRegisterPrefixPayload::getDescription, null,
                isNotBlankValidator(), minLengthStringValidator()
        );
    }

    @Bean("providerHomeUrl")
    public RegistrationValidationChain providerHomeUrlValidatorChain() {
        return new RegistrationValidationChain(
                getLabelFor("providerHomeUrl"),
                ServiceRequestRegisterPrefixPayload::getProviderHomeUrl,
                ServiceRequestRegisterResourcePayload::getProviderHomeUrl,
                isNotBlankValidator(), urlValidator());
    }

    @Bean("providerName")
    public RegistrationValidationChain providerNameValidatorChain() {
        return new RegistrationValidationChain(
                getLabelFor("providerName"),
                ServiceRequestRegisterPrefixPayload::getProviderName,
                ServiceRequestRegisterResourcePayload::getProviderName,
                isNotBlankValidator());
    }

    @Bean("providerDescription")
    public RegistrationValidationChain providerDescriptionValidatorChain() {
        return new RegistrationValidationChain(
                getLabelFor("providerDescription"),
                ServiceRequestRegisterPrefixPayload::getProviderDescription,
                ServiceRequestRegisterResourcePayload::getProviderDescription,
                isNotBlankValidator(), minLengthStringValidator());
    }

    @Bean("providerLocation")
    public RegistrationValidationChain providerLocationValidatorChain() {
        return new RegistrationValidationChain(
                getLabelFor("providerLocation"),
                ServiceRequestRegisterPrefixPayload::getProviderLocation,
                ServiceRequestRegisterResourcePayload::getProviderLocation,
                isNotBlankValidator(), locationCodeStringValidator());
    }

    @Bean("providerCode")
    public RegistrationValidationChain providerCodeValidatorChain() {
        return new RegistrationValidationChain(
                getLabelFor("providerCode"),
                ServiceRequestRegisterPrefixPayload::getProviderCode,
                ServiceRequestRegisterResourcePayload::getProviderCode,
                isNotBlankValidator(), providerCodeValueValidator());
    }

    @Bean("institutionName")
    public RegistrationValidationChain institutionNameValidatorChain() {
        return new RegistrationValidationChain(
                getLabelFor("institutionName"),
                ServiceRequestRegisterPrefixPayload::getInstitutionName,
                ServiceRequestRegisterResourcePayload::getInstitutionName,
                isNotBlankValidator());
    }

    @Bean("institutionHomeUrl")
    public RegistrationValidationChain institutionHomeUrlValidatorChain() {
        return new RegistrationValidationChain(
                getLabelFor("institutionHomeUrl"),
                ServiceRequestRegisterPrefixPayload::getInstitutionHomeUrl,
                ServiceRequestRegisterResourcePayload::getInstitutionHomeUrl,
                isNotBlankValidator(), urlValidator());
    }

    @Bean("institutionDescription")
    public RegistrationValidationChain institutionDescriptionValidatorChain() {
        return new RegistrationValidationChain(
                getLabelFor("institutionDescription"),
                ServiceRequestRegisterPrefixPayload::getInstitutionDescription,
                ServiceRequestRegisterResourcePayload::getInstitutionDescription,
                isNotBlankValidator(), minLengthStringValidator());
    }

    @Bean("institutionLocation")
    public RegistrationValidationChain institutionLocationValidatorChain() {
        return new RegistrationValidationChain(
                getLabelFor("institutionLocation"),
                ServiceRequestRegisterPrefixPayload::getInstitutionLocation,
                ServiceRequestRegisterResourcePayload::getInstitutionLocation,
                isNotBlankValidator(), locationCodeStringValidator());
    }

    @Bean("prefix")
    public RegistrationValidationChain prefixValidatorChain() {
        return new RegistrationValidationChain(
                getLabelFor("prefix"),
                ServiceRequestRegisterPrefixPayload::getRequestedPrefix,
                ServiceRequestRegisterResourcePayload::getNamespacePrefix,
                isNotBlankValidator(), prefixValueValidator());
    }

    @Bean("providerUrlPattern")
    public RegistrationValidationChain providerUrlPatternValidatorChain() {
        return new RegistrationValidationChain(
                getLabelFor("providerUrlPattern"),
                ServiceRequestRegisterPrefixPayload::getProviderUrlPattern,
                ServiceRequestRegisterResourcePayload::getProviderUrlPattern,
                isNotBlankValidator(), urlPatternContainsIdTemplateValidator(),
                similarUrlTemplateValidator(), sampleUrlRequestValidator());
    }

    @Bean("sampleId")
    public RegistrationValidationChain sampleIdValidatorChain() {
        return new RegistrationValidationChain(
                getLabelFor("sampleId"),
                ServiceRequestRegisterPrefixPayload::getSampleId,
                ServiceRequestRegisterResourcePayload::getSampleId,
                isNotBlankValidator(), sampleIdMatchesPatternValidator(), sampleUrlRequestValidator()
        );
    }

    @Bean("idRegexPattern")
    public RegistrationValidationChain idRegexPatternValidatorChain() {
        return new RegistrationValidationChain(
                getLabelFor("idRegexPattern"),
                ServiceRequestRegisterPrefixPayload::getIdRegexPattern, null,
                isNotBlankValidator(), regexSyntaxStringValidator(), sampleIdMatchesPatternValidator()
        );
    }

    @Bean("supportingReferences")
    public RegistrationValidationChain supportingReferencesValidatorChain() {
        return RegistrationValidationChain.acceptAllChain();
    }

    @Bean("additionalInformation")
    public RegistrationValidationChain additionalInformationValidatorChain() {
        return RegistrationValidationChain.acceptAllChain();
    }

    @Bean("requesterName")
    public RegistrationValidationChain requesterNameValidatorChain() {
        return new RegistrationValidationChain(
                getLabelFor("requesterName"),
                ValidatorsConfiguration::getRequesterName,
                ValidatorsConfiguration::getRequesterName,
                isNotBlankValidator());
    }

    @Bean("requesterEmail")
    public RegistrationValidationChain requesterEmailValidatorChain() {
        return new RegistrationValidationChain(
                getLabelFor("requesterEmail"),
                ValidatorsConfiguration::getRequesterEmail,
                ValidatorsConfiguration::getRequesterEmail,
                isNotBlankValidator(), emailStringValidator());
    }

    @Bean("authHelpUrl")
    public RegistrationValidationChain authHelpUrlValidatorChain() {
        return new RegistrationValidationChain(
                getLabelFor("authHelpUrl"),
                ServiceRequestRegisterPrefixPayload::getAuthHelpUrl,
                ServiceRequestRegisterPrefixPayload::isProtectedUrls,
                ServiceRequestRegisterResourcePayload::getAuthHelpUrl,
                ServiceRequestRegisterResourcePayload::isProtectedUrls,
                isNotBlankValidator(), urlValidator());
    }

    @Bean("authHelpDescription")
    public RegistrationValidationChain authHelpDescriptionValidatorChain() {
        return new RegistrationValidationChain(
                getLabelFor("authHelpDescription"),
                ServiceRequestRegisterPrefixPayload::getAuthHelpDescription,
                ServiceRequestRegisterPrefixPayload::isProtectedUrls,
                ServiceRequestRegisterResourcePayload::getAuthHelpDescription,
                ServiceRequestRegisterResourcePayload::isProtectedUrls,
                isNotBlankValidator(), minLengthStringValidator());
    }





    private static String getRequesterName(ServiceRequestRegisterPrefixPayload payload) {
        if (payload.getRequester() != null) {
            return payload.getRequester().getName();
        }
        return null;
    }

    private static String getRequesterName(ServiceRequestRegisterResourcePayload payload) {
        if (payload.getRequester() != null) {
            return payload.getRequester().getName();
        }
        return null;
    }

    private static String getRequesterEmail(ServiceRequestRegisterPrefixPayload payload) {
        if (payload.getRequester() != null) {
            return payload.getRequester().getEmail();
        }
        return null;
    }

    private static String getRequesterEmail(ServiceRequestRegisterResourcePayload payload) {
        if (payload.getRequester() != null) {
            return payload.getRequester().getEmail();
        }
        return null;
    }

    //This could probably be done using a string utils function, but this is more customizable
    private static final Map<String, String> translations = Map.ofEntries(
            Map.entry("prefix", "Prefix"),
            Map.entry("namespaceName", "Name"),
            Map.entry("namespaceDescription", "Description"),
            Map.entry("providerHomeUrl", "Provider Home URL"),
            Map.entry("providerName", "Provider Name"),
            Map.entry("providerDescription", "Provider Description"),
            Map.entry("providerLocation", "Provider Location"),
            Map.entry("providerCode", "Provider Code"),
            Map.entry("institutionName", "Institution Name"),
            Map.entry("institutionHomeUrl", "Institution Home URL"),
            Map.entry("institutionDescription", "Institution Description"),
            Map.entry("institutionLocation", "Institution Location"),
            Map.entry("providerUrlPattern", "Provider URL Pattern"),
            Map.entry("sampleId", "Sample ID"),
            Map.entry("idRegexPattern", "ID Regex Pattern"),
            Map.entry("supportingReferences", "Supporting References"),
            Map.entry("additionalInformation", "Additional Information"),
            Map.entry("requesterName", "Requester Name"),
            Map.entry("requesterEmail", "Requester Email"),
            Map.entry("authHelpUrl", "Authentication Instructions URL"),
            Map.entry("authHelpDescription", "Authentication Instructions")
    );
    private static String getLabelFor(String attribute) {
        return translations.getOrDefault(attribute, attribute);
    }

    private static class NoopErrorHandler implements ResponseErrorHandler {
        @Override
        public boolean hasError(@NonNull ClientHttpResponse response) throws IOException {
            return false;
        }

        @Override
        public void handleError(@NonNull ClientHttpResponse response) throws IOException {
            // Error handling done on validator
        }
    }
}
