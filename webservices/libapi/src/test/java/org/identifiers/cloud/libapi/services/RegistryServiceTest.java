package org.identifiers.cloud.libapi.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.commons.messages.models.Requester;
import org.identifiers.cloud.commons.messages.requests.registry.ServiceRequestRegisterPrefixPayload;
import org.identifiers.cloud.commons.messages.responses.ServiceResponse;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * Project: libapi
 * Package: org.identifiers.cloud.libapi.services
 * Timestamp: 2018-03-08 12:56
 * ---
 */
@Slf4j
public class RegistryServiceTest {
    private static final ServiceRequestRegisterPrefixPayload payload = new ServiceRequestRegisterPrefixPayload()
          .setName("Unit test name")
          .setDescription("This is a sample prefix registration request from a unit test of libapi, " +
                                "we need enouch characters for the description")
          .setProviderName("Identifiers.org")
          .setProviderDescription("Identifiers.org platform, blah, blah, blah, blah, blah... and a lot of things to get this validated")
          .setProviderCode("idorg")
          .setProviderHomeUrl("http://identifiers.org")
          .setProviderLocation("GB")
          .setInstitutionName("EMBL-EBI")
          .setInstitutionHomeUrl("https://www.ebi.ac.uk")
          .setInstitutionDescription("The European Bioinformatics Institute, blah, blah, blah...")
          .setInstitutionLocation("GB")
          .setRequestedPrefix("mynewprefix")
          .setProviderUrlPattern("https://www.httpbin.org/status/{$id}")
          .setSampleId("200")
          .setIdRegexPattern("\\d+")
          .setSupportingReferences(new String[]{"ref1", "ref2"})
          .setAdditionalInformation("Additional information about this unit test")
          .setRequester(new Requester()
                              .setEmail("rjuacaba@ebi.ac.uk")
                              .setName("renatocjn@gmail.com"));

    private <T> void checkResultOk(ServiceResponse<T> response, String context) {
        // Just for debugging purposes, serialized response into the logs
        ObjectMapper mapper = new ObjectMapper();
        try {
            log.info("Test '{}', " +
                    "response from the service:\n{}", context, mapper.writeValueAsString(response));
        } catch (JsonProcessingException e) {
            // Ignore
        }
        assertThat(String.format("Response from service is OK (%s)", context),
                response.getHttpStatus() == HttpStatus.OK,
                is(true));
    }

    @Test
    public void testPrefixRegistration() {
        var response = ApiServicesFactory.getRegistryService("localhost", "8180")
                        .setProtocolSchemeToHttp()
                        .requestPrefixRegistration(payload);
        checkResultOk(response, "Prefix Registration Request");
    }

    @Test
    public void testNamespaceNameValidation() {
        var response = ApiServicesFactory.getRegistryService("localhost", "8180")
                        .setProtocolSchemeToHttp()
                        .validateName(payload);
        checkResultOk(response, "Validation Request - Name");
    }

    @Test
    public void testNamespaceDescriptionValidation() {
        var response =
                ApiServicesFactory.getRegistryService("localhost", "8180")
                        .setProtocolSchemeToHttp()
                        .validateDescription(payload);
        checkResultOk(response, "Validation Request - Description");
    }

    @Test
    public void testValidateProviderHomeUrl() {
        var response =
                ApiServicesFactory.getRegistryService("localhost", "8180")
                        .setProtocolSchemeToHttp()
                        .validateProviderHomeUrl(payload);
        checkResultOk(response, "Validation Request - Provider home URL");
    }

    @Test
    public void testValidateProviderName() {
        var response =
                ApiServicesFactory.getRegistryService("localhost", "8180")
                        .setProtocolSchemeToHttp()
                        .validateProviderName(payload);
        checkResultOk(response, "Validation Request - Provider name");
    }

    @Test
    public void testValidateProviderDescription() {
        var response =
                ApiServicesFactory.getRegistryService("localhost", "8180")
                        .setProtocolSchemeToHttp()
                        .validateProviderDescription(payload);
        checkResultOk(response, "Validation Request - Provider description");
    }

    @Test
    public void testValidateProviderLocation() {
        var response =
                ApiServicesFactory.getRegistryService("localhost", "8180")
                        .setProtocolSchemeToHttp()
                        .validateProviderLocation(payload);
        checkResultOk(response, "Validation Request - Provider location");
    }

    @Test
    public void testValidateProviderCode() {
        var response =
                ApiServicesFactory.getRegistryService("localhost", "8180")
                        .setProtocolSchemeToHttp()
                        .validateProviderCode(payload);
        checkResultOk(response, "Validation Request - Provider code");
    }

    @Test
    public void testValidateInstitutionName() {
        var response =
                ApiServicesFactory.getRegistryService("localhost", "8180")
                        .setProtocolSchemeToHttp()
                        .validateInstitutionName(payload);
        checkResultOk(response, "Validation Request - Institution name");
    }

    @Test
    public void testValidateInstitutionHomeUrl() {
        var response =
                ApiServicesFactory.getRegistryService("localhost", "8180")
                        .setProtocolSchemeToHttp()
                        .validateInstitutionHomeUrl(payload);
        checkResultOk(response, "Validation Request - Institution home URL");
    }

    @Test
    public void testValidateInstitutionDescription() {
        var response =
                ApiServicesFactory.getRegistryService("localhost", "8180")
                        .setProtocolSchemeToHttp()
                        .validateInstitutionDescription(payload);
        checkResultOk(response, "Validation Request - Institution description");
    }

    @Test
    public void testValidateInstitutionLocation() {
        var response =
                ApiServicesFactory.getRegistryService("localhost", "8180")
                        .setProtocolSchemeToHttp()
                        .validateInstitutionLocation(payload);
        checkResultOk(response, "Validation Request - Institution location");
    }

    @Test
    public void testValidateRequestedPrefix() {
        var response =
                ApiServicesFactory.getRegistryService("localhost", "8180")
                        .setProtocolSchemeToHttp()
                        .validateRequestedPrefix(payload);
        checkResultOk(response, "Validation Request - Requested prefix");
    }

    @Test
    public void testValidateProviderUrlPattern() {
        var response =
                ApiServicesFactory.getRegistryService("localhost", "8180")
                        .setProtocolSchemeToHttp()
                        .validateProviderUrlPattern(payload);
        checkResultOk(response, "Validation Request - Provider URL Pattern");
    }

    @Test
    public void testValidateSampleId() {
        var response =
                ApiServicesFactory.getRegistryService("localhost", "8180")
                        .setProtocolSchemeToHttp()
                        .validateSampleId(payload);
        checkResultOk(response, "Validation Request - Sample ID");
    }

    @Test
    public void testValidateIdRegexPattern() {
        var response =
                ApiServicesFactory.getRegistryService("localhost", "8180")
                        .setProtocolSchemeToHttp()
                        .validateIdRegexPattern(payload);
        checkResultOk(response, "Validation Request - ID Regex Pattern");
    }

    @Test
    public void testValidateReferences() {
        var response =
                ApiServicesFactory.getRegistryService("localhost", "8180")
                        .setProtocolSchemeToHttp()
                        .validateReferences(payload);
        checkResultOk(response, "Validation Request - References");
    }

    @Test
    public void testValidateAdditionalInformation() {
        var response =
                ApiServicesFactory.getRegistryService("localhost", "8180")
                        .setProtocolSchemeToHttp()
                        .validateAdditionalInformation(payload);
        checkResultOk(response, "Validation Request - Additional information");
    }

    @Test
    public void testValidateRequesterName() {
        var response =
                ApiServicesFactory.getRegistryService("localhost", "8180")
                        .setProtocolSchemeToHttp()
                        .validateRequesterName(payload);
        checkResultOk(response, "Validation Request - Requester name");
    }

    @Test
    public void testValidateRequesterEmail() {
        var response =
                ApiServicesFactory.getRegistryService("localhost", "8180")
                        .setProtocolSchemeToHttp()
                        .validateRequesterEmail(payload);
        checkResultOk(response, "Validation Request - Requester e-mail");
    }

    @Test
    public void testValidateRequester() {
        var response =
                ApiServicesFactory.getRegistryService("localhost", "8180")
                        .setProtocolSchemeToHttp()
                        .validateRequester(payload);
        checkResultOk(response, "Validation Request - Requester");
    }
}