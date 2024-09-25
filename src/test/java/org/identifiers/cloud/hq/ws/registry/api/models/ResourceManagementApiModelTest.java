package org.identifiers.cloud.hq.ws.registry.api.models;

import org.identifiers.cloud.hq.ws.registry.api.data.models.Requester;
import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterResourcePayload;
import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterResourceValidate;
import org.identifiers.cloud.hq.ws.registry.configuration.ValidatorsConfiguration;
import org.identifiers.cloud.hq.ws.registry.data.models.Namespace;
import org.identifiers.cloud.hq.ws.registry.data.repositories.ResourceRegistrationSessionRepository;
import org.identifiers.cloud.hq.ws.registry.data.services.NamespaceService;
import org.identifiers.cloud.hq.ws.registry.models.ResourceLifecycleManagementService;
import org.identifiers.cloud.hq.ws.registry.models.ResourceRegistrationRequestManagementService;
import org.identifiers.cloud.hq.ws.registry.models.helpers.AuthHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(properties = {
        "org.identifiers.cloud.hq.ws.registry.validation.mindescriptionlength=10"
})
@ContextConfiguration(classes = {
        ResourceManagementApiModelTest.RestTemplateBuilderProvider.class,
        ValidatorsConfiguration.class,
        ResourceManagementApiModel.class,
})
class ResourceManagementApiModelTest {
    @MockBean NamespaceService namespaceService;
    @MockBean AuthHelper authHelper;
    @MockBean ResourceRegistrationSessionRepository resourceRegistrationSessionRepository;
    @MockBean ResourceRegistrationRequestManagementService resourceRegistrationRequestManagementService;
    @MockBean ResourceLifecycleManagementService resourceLifecycleManagementService;
    @Autowired ResourceManagementApiModel model;


    @TestConfiguration
    public static class RestTemplateBuilderProvider {
        // Needed for the ValidatorsConfiguration dependencies
        @Bean @Primary
        public RestTemplateBuilder builder() {
            RestTemplateBuilder builder = mock();
            RestTemplate restTemplate = mock();
            var responseEntity = new ResponseEntity<Void>(OK);
            var badResponseEntity = new ResponseEntity<Void>(BAD_REQUEST);

            doReturn(responseEntity)
                    .when(restTemplate)
                    .exchange(contains("valid.com"), eq(GET), eq(null), eq(Void.class));

            doReturn(badResponseEntity)
                    .when(restTemplate)
                    .exchange(contains("invalid.com"), eq(GET), eq(null), eq(Void.class));

            doReturn(restTemplate).when(builder).build();

            return builder;
        }
    }

    @AfterEach
    void verifyMocksAreNotCalled() {
        verifyNoMoreInteractions(authHelper, resourceRegistrationSessionRepository,
                resourceRegistrationRequestManagementService, resourceLifecycleManagementService);
    }

    @Test
    void validateNamespacePrefix() {
        doReturn(new Namespace().setPrefix("existing_prefix"), (Namespace) null)
                .when(namespaceService).getNamespaceByPrefix(anyString());

        var payload = new ServiceRequestRegisterResourcePayload();
        var request = new ServiceRequestRegisterResourceValidate();
        request.setPayload(payload);

        payload.setNamespacePrefix("existing_prefix");
        var answer = model.validateNamespacePrefix(request);
        assertEquals(OK, answer.getHttpStatus());

        var invalidValue = new String[] {"UPPERCASED_NAMESPACE", "CamelCasedNamespace", "new.prefix", "", "   ", null};
        for (var value : invalidValue) {
            payload.setNamespacePrefix(value);
            answer = model.validateNamespacePrefix(request);
            assertEquals(BAD_REQUEST, answer.getHttpStatus(), "'" + value + "' should be invalid");
            assertFalse(isBlank(answer.getErrorMessage()));
        }
    }



    @Test
    void validateProviderCode() {
        doReturn(null, new Namespace())
                .when(namespaceService)
                .getNamespaceByPrefix(anyString());
        doReturn(false, true)
                .when(namespaceService)
                .checkIfNamespaceExistsByPrefix(anyString());

        var payload = new ServiceRequestRegisterResourcePayload();
        var request = new ServiceRequestRegisterResourceValidate();
        request.setPayload(payload);

        payload.setProviderCode("pcode");
        var answer = model.validateProviderCode(request);
        assertEquals(OK, answer.getHttpStatus());

        var invalidValue = new String[] {"existing_namespace", "   ", "", null};
        for (var value : invalidValue) {
            payload.setProviderCode(value);
            answer = model.validateProviderCode(request);
            assertEquals(BAD_REQUEST, answer.getHttpStatus(), "'" + value + "' should be invalid: " + answer.getErrorMessage());
            assertFalse(isBlank(answer.getErrorMessage()));
        }
    }

    @Test
    void validateProviderUrlPattern() {
        var payload = new ServiceRequestRegisterResourcePayload();
        var request = new ServiceRequestRegisterResourceValidate();
        request.setPayload(payload);

        payload.setProviderUrlPattern("https://valid.com/?q={$id}");
        var answer = model.validateProviderUrlPattern(request);
        assertEquals(OK, answer.getHttpStatus());

        payload.setProviderUrlPattern("https://valid.com/?q={$id}");
        payload.setSampleId("welp");
        answer = model.validateProviderUrlPattern(request);
        assertEquals(OK, answer.getHttpStatus());

        var invalidValue = new String[] {
                "ftp://valid.com?q={$id}", "valid.com?q={$id}",
                "https://valid.com", "https://invalid.com?q={$id}",
                "   ", "", null};
        for (var value : invalidValue) {
            payload.setProviderUrlPattern(value);
            payload.setSampleId("welp");
            answer = model.validateProviderUrlPattern(request);
            assertEquals(BAD_REQUEST, answer.getHttpStatus(), "'" + value + "' should be invalid");
            assertFalse(isBlank(answer.getErrorMessage()));
        }
    }

    @Test
    void validateSampleId() {
        doReturn(new Namespace().setPattern("[a-z]+"))
                .when(namespaceService)
                .getNamespaceByPrefix(anyString());

        var payload = new ServiceRequestRegisterResourcePayload();
        var request = new ServiceRequestRegisterResourceValidate();
        request.setPayload(payload);

        payload.setSampleId("welp");
        var answer = model.validateSampleId(request);
        assertEquals(OK, answer.getHttpStatus());

        payload.setNamespacePrefix("existing_prefix");
        answer = model.validateSampleId(request);
        assertEquals(OK, answer.getHttpStatus());

        payload.setProviderUrlPattern("https://valid.com/?q={$id}");
        answer = model.validateSampleId(request);
        assertEquals(OK, answer.getHttpStatus());

        payload.setProviderUrlPattern("https://invalid.com/?q={$id}");
        answer = model.validateSampleId(request);
        assertEquals(BAD_REQUEST, answer.getHttpStatus());

        payload.setProviderUrlPattern(null);
        var invalidValue = new String[] {"BAD_ID", "   ", "", null};
        for (var value : invalidValue) {
            payload.setSampleId(value);
            answer = model.validateSampleId(request);
            assertEquals(BAD_REQUEST, answer.getHttpStatus(), "'" + value + "' should be invalid");
            assertFalse(isBlank(answer.getErrorMessage()));
        }
    }



    @Test
    void validateProviderName() {
        var payload = new ServiceRequestRegisterResourcePayload();
        var request = new ServiceRequestRegisterResourceValidate();
        request.setPayload(payload);

        payload.setProviderName("Renato");
        var answer = model.validateProviderName(request);
        assertEquals(OK, answer.getHttpStatus());

        var invalidNames = new String[] {"   ", "", null};
        for (var name : invalidNames) {
            payload.setProviderName(name);
            answer = model.validateProviderName(request);
            assertEquals(BAD_REQUEST, answer.getHttpStatus(), "'" + name + "' should be invalid");
            assertFalse(isBlank(answer.getErrorMessage()));
        }
    }

    @Test
    void validateProviderDescription() {
        var payload = new ServiceRequestRegisterResourcePayload();
        var request = new ServiceRequestRegisterResourceValidate();
        request.setPayload(payload);

        payload.setProviderDescription("VALID DESCRIPTION");
        var answer = model.validateProviderDescription(request);
        assertEquals(OK, answer.getHttpStatus());

        var invalidValue = new String[] {"too short", "    too    short     ", "", null};
        for (var value : invalidValue) {
            payload.setProviderDescription(value);
            answer = model.validateProviderDescription(request);
            assertEquals(BAD_REQUEST, answer.getHttpStatus(), "'" + value + "' should be invalid");
            assertFalse(isBlank(answer.getErrorMessage()));
        }
    }

    @Test
    void validateProviderHomeUrl() {
        var payload = new ServiceRequestRegisterResourcePayload();
        var request = new ServiceRequestRegisterResourceValidate();
        request.setPayload(payload);

        payload.setProviderHomeUrl("https://valid.com");
        var answer = model.validateProviderHomeUrl(request);
        assertEquals(OK, answer.getHttpStatus());

        var invalidUrls = new String[] {"ftp://ftpUrl.com", "notUrl", "noProtocol.com", "https://invalid.com", "", null};
        for (var url : invalidUrls) {
            payload.setProviderHomeUrl(url);
            answer = model.validateProviderHomeUrl(request);
            assertEquals(BAD_REQUEST, answer.getHttpStatus(), "'" + url + "' should be invalid");
            assertFalse(isBlank(answer.getErrorMessage()));
        }
    }

    @Test
    void validateProviderLocation() {
        var payload = new ServiceRequestRegisterResourcePayload();
        var request = new ServiceRequestRegisterResourceValidate();
        request.setPayload(payload);


        for (var code : countryCodeExamples) {
            payload.setProviderLocation(code);
            var answer = model.validateProviderLocation(request);
            assertEquals(OK, answer.getHttpStatus());
        }

        var invalidNames = new String[] {"   ", "", null, "AFG", "CAN"};
        for (var name : invalidNames) {
            payload.setProviderLocation(name);
            var answer = model.validateProviderLocation(request);
            assertEquals(BAD_REQUEST, answer.getHttpStatus(), "'" + name + "' should be invalid");
            assertFalse(isBlank(answer.getErrorMessage()));
        }
    }



    @Test
    void validateInstitutionName() {
        var payload = new ServiceRequestRegisterResourcePayload();
        var request = new ServiceRequestRegisterResourceValidate();
        request.setPayload(payload);

        payload.setInstitutionName("VALID NAME");
        var answer = model.validateInstitutionName(request);
        assertEquals(OK, answer.getHttpStatus());

        var invalidValue = new String[] {"     ", "", null};
        for (var value : invalidValue) {
            payload.setInstitutionName(value);
            answer = model.validateInstitutionName(request);
            assertEquals(BAD_REQUEST, answer.getHttpStatus(), "'" + value + "' should be invalid");
            assertFalse(isBlank(answer.getErrorMessage()));
        }
    }

    @Test
    void validateInstitutionDescription() {
        var payload = new ServiceRequestRegisterResourcePayload();
        var request = new ServiceRequestRegisterResourceValidate();
        request.setPayload(payload);

        payload.setInstitutionDescription("VALID DESCRIPTION");
        var answer = model.validateInstitutionDescription(request);
        assertEquals(OK, answer.getHttpStatus());

        var invalidValue = new String[] {"too short", "    too    short     ", "", null};
        for (var value : invalidValue) {
            payload.setInstitutionDescription(value);
            answer = model.validateInstitutionDescription(request);
            assertEquals(BAD_REQUEST, answer.getHttpStatus(), "'" + value + "' should be invalid");
            assertFalse(isBlank(answer.getErrorMessage()));
        }
    }

    @Test
    void validateInstitutionHomeUrl() {
        var payload = new ServiceRequestRegisterResourcePayload();
        var request = new ServiceRequestRegisterResourceValidate();
        request.setPayload(payload);

        payload.setInstitutionHomeUrl("https://valid.com");
        var answer = model.validateInstitutionHomeUrl(request);
        assertEquals(OK, answer.getHttpStatus());

        var invalidUrls = new String[] {"ftp://ftpUrl.com", "notUrl", "noProtocol.com", "https://invalid.com", "", "   ", null};
        for (var url : invalidUrls) {
            payload.setInstitutionHomeUrl(url);
            answer = model.validateInstitutionHomeUrl(request);
            assertEquals(BAD_REQUEST, answer.getHttpStatus(), "'" + url + "' should be invalid");
            assertFalse(isBlank(answer.getErrorMessage()));
        }
    }

    @Test
    void validateInstitutionLocation() {
        var payload = new ServiceRequestRegisterResourcePayload();
        var request = new ServiceRequestRegisterResourceValidate();
        request.setPayload(payload);


        for (var code : countryCodeExamples) {
            payload.setInstitutionLocation(code);
            var answer = model.validateInstitutionLocation(request);
            assertEquals(OK, answer.getHttpStatus());
        }

        var invalidNames = new String[] {"   ", "", null, "AFG", "CAN"};
        for (var name : invalidNames) {
            payload.setInstitutionLocation(name);
            var answer = model.validateInstitutionLocation(request);
            assertEquals(BAD_REQUEST, answer.getHttpStatus(), "'" + name + "' should be invalid");
            assertFalse(isBlank(answer.getErrorMessage()));
        }
    }



    @Test
    void validateAdditionalInformation() {
        var payload = new ServiceRequestRegisterResourcePayload();
        var request = new ServiceRequestRegisterResourceValidate();
        request.setPayload(payload);

        var values = new String[]{ "welp", "   ", "", null };
        for (var value : values) {
            payload.setAdditionalInformation(value);
            var answer = model.validateAdditionalInformation(request);
            assertEquals(OK, answer.getHttpStatus(), "'" + value + "' should be valid");
            assertTrue(isBlank(answer.getErrorMessage()));
        }
    }



    @Test
    void validateRequester() {
        var payload = new ServiceRequestRegisterResourcePayload();
        var request = new ServiceRequestRegisterResourceValidate();
        request.setPayload(payload);
        var requester = new Requester();
        payload.setRequester(requester);

        var answer = model.validateRequester(request);
        assertEquals(BAD_REQUEST, answer.getHttpStatus());
        assertFalse(isBlank(answer.getErrorMessage()));

        requester.setName("Renato");
        answer = model.validateRequester(request);
        assertEquals(BAD_REQUEST, answer.getHttpStatus());
        assertFalse(isBlank(answer.getErrorMessage()));

        requester.setEmail("renato@google.com");
        answer = model.validateRequester(request);
        assertEquals(OK, answer.getHttpStatus());
        assertTrue(isBlank(answer.getErrorMessage()));

        requester.setEmail("bad_email.com");
        answer = model.validateRequester(request);
        assertEquals(BAD_REQUEST, answer.getHttpStatus());
        assertFalse(isBlank(answer.getErrorMessage()));
    }

    @Test
    void validateRequesterName() {
        var payload = new ServiceRequestRegisterResourcePayload();
        var request = new ServiceRequestRegisterResourceValidate();
        request.setPayload(payload);
        var requester = new Requester();
        payload.setRequester(requester);

        requester.setName("Renato");
        var answer = model.validateRequesterName(request);
        assertEquals(OK, answer.getHttpStatus());

        var invalidNames = new String[] {"   ", "", null};
        for (var name : invalidNames) {
            requester.setName(name);
            answer = model.validateRequesterName(request);
            assertEquals(BAD_REQUEST, answer.getHttpStatus(), "'" + name + "' should be invalid");
            assertFalse(isBlank(answer.getErrorMessage()));
        }
    }

    @Test
    void validateRequesterEmail() {
        var payload = new ServiceRequestRegisterResourcePayload();
        var request = new ServiceRequestRegisterResourceValidate();
        request.setPayload(payload);
        var requester = new Requester();
        payload.setRequester(requester);

        requester.setEmail("valid@email.com");
        var answer = model.validateRequesterEmail(request);
        assertEquals(OK, answer.getHttpStatus());

        var invalidNames = new String[] {"invalid_email.com", "   ", "", null};
        for (var name : invalidNames) {
            requester.setEmail(name);
            answer = model.validateRequesterEmail(request);
            assertEquals(BAD_REQUEST, answer.getHttpStatus(), "'" + name + "' should be invalid");
            assertFalse(isBlank(answer.getErrorMessage()));
        }
    }



    @Test
    void validateAuthHelpDescription() {
        var payload = new ServiceRequestRegisterResourcePayload();
        var request = new ServiceRequestRegisterResourceValidate();
        request.setPayload(payload);

        payload.setProtectedUrls(true);
        payload.setAuthHelpDescription("VALID DESCRIPTION");
        var answer = model.validateAuthHelpDescription(request);
        assertEquals(OK, answer.getHttpStatus());

        payload.setProtectedUrls(false);
        payload.setAuthHelpDescription(null);
        answer = model.validateAuthHelpDescription(request);
        assertEquals(OK, answer.getHttpStatus());

        payload.setProtectedUrls(true);
        var invalidNames = new String[] {"too short", "   too short   ", "   ", "", null};
        for (var name : invalidNames) {
            payload.setAuthHelpDescription(name);
            answer = model.validateAuthHelpDescription(request);
            assertEquals(BAD_REQUEST, answer.getHttpStatus(), "'" + name + "' should be invalid");
            assertFalse(isBlank(answer.getErrorMessage()));
        }
    }

    @Test
    void validateAuthHelpUrl() {
        var payload = new ServiceRequestRegisterResourcePayload();
        var request = new ServiceRequestRegisterResourceValidate();
        request.setPayload(payload);

        payload.setProtectedUrls(true);
        payload.setAuthHelpUrl("https://valid.com");
        var answer = model.validateAuthHelpUrl(request);
        assertEquals(OK, answer.getHttpStatus());

        payload.setProtectedUrls(false);
        payload.setAuthHelpUrl(null);
        answer = model.validateAuthHelpUrl(request);
        assertEquals(OK, answer.getHttpStatus());

        payload.setProtectedUrls(true);
        var invalidUrls = new String[] {"ftp://ftpUrl.com", "notUrl", "noProtocol.com", "https://invalid.com", "", "   ", null};
        for (var url : invalidUrls) {
            payload.setAuthHelpUrl(url);
            answer = model.validateAuthHelpUrl(request);
            assertEquals(BAD_REQUEST, answer.getHttpStatus(), "'" + url + "' should be invalid");
            assertFalse(isBlank(answer.getErrorMessage()));
        }
    }




    private final List<String> countryCodeExamples = List.of(
            "AD", "AE", "AF", "AG", "AI", "AL", "AM", "AO", "AQ", "AR", "AS", "AT", "AU", "AW", "AX",
            "AZ", "BA", "BB", "BD", "BE", "BF", "BG", "BH", "BI", "BJ", "BL", "BM", "BN", "BO", "BQ", "BR", "BS",
            "BT", "BV", "BW", "BY", "BZ", "CA", "CC", "CD", "CF", "CG", "CH", "CI", "CK", "CL", "CM", "CN", "CO",
            "CR", "CU", "CV", "CW", "CX", "CY", "CZ", "DE", "DJ", "DK", "DM", "DO", "DZ", "EC", "EE", "EG", "EH",
            "ER", "ES", "ET", "FI", "FJ", "FK", "FM", "FO", "FR", "GA", "GB", "GD", "GE", "GF", "GG", "GH", "GI",
            "GL", "GM", "GN", "GP", "GQ", "GR", "GS", "GT", "GU", "GW", "GY", "HK", "HM", "HN", "HR", "HT", "HU",
            "ID", "IE", "IL", "IM", "IN", "IO", "IQ", "IR", "IS", "IT", "JE", "JM", "JO", "JP", "KE", "KG", "KH",
            "KI", "KM", "KN", "KP", "KR", "KW", "KY", "KZ", "LA", "LB", "LC", "LI", "LK", "LR", "LS", "LT", "LU",
            "LV", "LY", "MA", "MC", "MD", "ME", "MF", "MG", "MH", "MK", "ML", "MM", "MN", "MO", "MP", "MQ", "MR",
            "MS", "MT", "MU", "MV", "MW", "MX", "MY", "MZ", "NA", "NC", "NE", "NF", "NG", "NI", "NL", "NO", "NP",
            "NR", "NU", "NZ", "OM", "PA", "PE", "PF", "PG", "PH", "PK", "PL", "PM", "PN", "PR", "PS", "PT", "PW",
            "PY", "QA", "RE", "RO", "RS", "RU", "RW", "SA", "SB", "SC", "SD", "SE", "SG", "SH", "SI", "SJ", "SK",
            "SL", "SM", "SN", "SO", "SR", "SS", "ST", "SV", "SX", "SY", "SZ", "TC", "TD", "TF", "TG", "TH", "TJ",
            "TK", "TL", "TM", "TN", "TO", "TR", "TT", "TV", "TW", "TZ", "UA", "UG", "UM", "US", "UY", "UZ", "VA",
            "VC", "VE", "VG", "VI", "VN", "VU", "WF", "WS", "YE", "YT", "ZA", "ZM", "ZW");
}