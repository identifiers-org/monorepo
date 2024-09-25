package org.identifiers.cloud.hq.ws.registry.api.models;

import org.identifiers.cloud.hq.ws.registry.api.data.models.Requester;
import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefixPayload;
import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefixValidate;
import org.identifiers.cloud.hq.ws.registry.configuration.ValidatorsConfiguration;
import org.identifiers.cloud.hq.ws.registry.data.models.Namespace;
import org.identifiers.cloud.hq.ws.registry.data.services.NamespaceService;
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

import java.util.Arrays;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(properties = {
        "org.identifiers.cloud.hq.ws.registry.validation.mindescriptionlength=10"
})
@ContextConfiguration(classes = {
        PrefixRegistrationRequestValidationApiModelTest.RestTemplateBuilderProvider.class,
        ValidatorsConfiguration.class,
        PrefixRegistrationRequestValidationApiModel.class,
})
class PrefixRegistrationRequestValidationApiModelTest {
    @MockBean NamespaceService namespaceService;
    @Autowired PrefixRegistrationRequestValidationApiModel model;

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



    @Test
    void validateName() {
        var payload = new ServiceRequestRegisterPrefixPayload();
        var request = new ServiceRequestRegisterPrefixValidate();
        request.setPayload(payload);

        payload.setName("Renato");
        var answer = model.validateName(request);
        assertEquals(OK, answer.getHttpStatus());

        var invalidNames = new String[] {"   ", "", null};
        for (var name : invalidNames) {
            payload.setName(name);
            answer = model.validateName(request);
            assertEquals(BAD_REQUEST, answer.getHttpStatus(), "'" + name + "' should be invalid");
            assertFalse(isBlank(answer.getErrorMessage()));
        }
    }

    @Test
    void validateDescription() {
        var payload = new ServiceRequestRegisterPrefixPayload();
        var request = new ServiceRequestRegisterPrefixValidate();
        request.setPayload(payload);

        payload.setDescription("VALID DESCRIPTION");
        var answer = model.validateDescription(request);
        assertEquals(OK, answer.getHttpStatus());

        var invalidValue = new String[] {"too short", "    too    short     ", "", null};
        for (var value : invalidValue) {
            payload.setDescription(value);
            answer = model.validateDescription(request);
            assertEquals(BAD_REQUEST, answer.getHttpStatus(), "'" + value + "' should be invalid");
            assertFalse(isBlank(answer.getErrorMessage()));
        }
    }

    @Test
    void validateRequestedPrefix() {
        doReturn(null, new Namespace())
                .when(namespaceService).getNamespaceByPrefix(anyString());

        var payload = new ServiceRequestRegisterPrefixPayload();
        var request = new ServiceRequestRegisterPrefixValidate();
        request.setPayload(payload);

        payload.setRequestedPrefix("new.prefix");
        var answer = model.validateRequestedPrefix(request);
        assertEquals(OK, answer.getHttpStatus());

        var invalidValue = new String[] {"UPPERCASED_NAMESPACE", "CamelCasedNamespace", "existing_prefix", "", "   ", "", null};
        for (var value : invalidValue) {
            payload.setRequestedPrefix(value);
            answer = model.validateRequestedPrefix(request);
            assertEquals(BAD_REQUEST, answer.getHttpStatus(), "'" + value + "' should be invalid");
            assertFalse(isBlank(answer.getErrorMessage()));
        }
    }



    @Test
    void validateProviderCode() {
        doThrow(new RuntimeException("This should not be run for namespace requests"))
                .when(namespaceService)
                .getNamespaceByPrefix(anyString());

        var payload = new ServiceRequestRegisterPrefixPayload();
        var request = new ServiceRequestRegisterPrefixValidate();
        request.setPayload(payload);

        payload.setProviderCode("pcode");
        var answer = model.validateProviderCode(request);
        assertEquals(OK, answer.getHttpStatus());

        var invalidValue = new String[] {"   ", "", null};
        for (var value : invalidValue) {
            payload.setProviderCode(value);
            answer = model.validateProviderCode(request);
            assertEquals(BAD_REQUEST, answer.getHttpStatus(), "'" + value + "' should be invalid");
            assertFalse(isBlank(answer.getErrorMessage()));
        }
    }

    @Test
    void validateProviderUrlPattern() {
        var payload = new ServiceRequestRegisterPrefixPayload();
        var request = new ServiceRequestRegisterPrefixValidate();
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
        var payload = new ServiceRequestRegisterPrefixPayload();
        var request = new ServiceRequestRegisterPrefixValidate();
        request.setPayload(payload);

        payload.setSampleId("welp");
        var answer = model.validateSampleId(request);
        assertEquals(OK, answer.getHttpStatus());

        payload.setIdRegexPattern("[a-z]+");
        answer = model.validateSampleId(request);
        assertEquals(OK, answer.getHttpStatus());
        payload.setIdRegexPattern(null);

        payload.setProviderUrlPattern("https://valid.com/?q={$id}");
        answer = model.validateSampleId(request);
        assertEquals(OK, answer.getHttpStatus());
        payload.setProviderUrlPattern(null);

        var invalidValue = new String[] {"   ", "", null};
        for (var value : invalidValue) {
            payload.setSampleId(value);
            answer = model.validateSampleId(request);
            assertEquals(BAD_REQUEST, answer.getHttpStatus(), "'" + value + "' should be invalid");
            assertFalse(isBlank(answer.getErrorMessage()));
        }

        payload.setSampleId("welp");

        payload.setProviderUrlPattern("https://invalid.com/?q={$id}");
        answer = model.validateSampleId(request);
        assertEquals(BAD_REQUEST, answer.getHttpStatus());
        payload.setProviderUrlPattern(null);

        payload.setIdRegexPattern("[A-Z]+");
        answer = model.validateSampleId(request);
        assertEquals(BAD_REQUEST, answer.getHttpStatus());
    }

    @Test
    void validateIdRegexPattern() {
        var payload = new ServiceRequestRegisterPrefixPayload();
        var request = new ServiceRequestRegisterPrefixValidate();
        request.setPayload(payload);

        payload.setIdRegexPattern("[a-z]+");
        var answer = model.validateIdRegexPattern(request);
        assertEquals(OK, answer.getHttpStatus());

        payload.setSampleId("welp");
        answer = model.validateIdRegexPattern(request);
        assertEquals(OK, answer.getHttpStatus());

        payload.setSampleId("WELP");
        answer = model.validateIdRegexPattern(request);
        assertEquals(BAD_REQUEST, answer.getHttpStatus());

        payload.setSampleId(null);
        var invalidValue = new String[] {"]1nv4l1d syn74x[", "   ", "", null};
        for (var value : invalidValue) {
            payload.setIdRegexPattern(value);
            answer = model.validateIdRegexPattern(request);
            assertEquals(BAD_REQUEST, answer.getHttpStatus(), "'" + value + "' should be invalid");
            assertFalse(isBlank(answer.getErrorMessage()));
        }
    }



    @Test
    void validateProviderName() {
        var payload = new ServiceRequestRegisterPrefixPayload();
        var request = new ServiceRequestRegisterPrefixValidate();
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
        var payload = new ServiceRequestRegisterPrefixPayload();
        var request = new ServiceRequestRegisterPrefixValidate();
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
        var payload = new ServiceRequestRegisterPrefixPayload();
        var request = new ServiceRequestRegisterPrefixValidate();
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
        var payload = new ServiceRequestRegisterPrefixPayload();
        var request = new ServiceRequestRegisterPrefixValidate();
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
        var payload = new ServiceRequestRegisterPrefixPayload();
        var request = new ServiceRequestRegisterPrefixValidate();
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
        var payload = new ServiceRequestRegisterPrefixPayload();
        var request = new ServiceRequestRegisterPrefixValidate();
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
        var payload = new ServiceRequestRegisterPrefixPayload();
        var request = new ServiceRequestRegisterPrefixValidate();
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
        var payload = new ServiceRequestRegisterPrefixPayload();
        var request = new ServiceRequestRegisterPrefixValidate();
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
    void validateReferences() {
        var payload = new ServiceRequestRegisterPrefixPayload();
        var request = new ServiceRequestRegisterPrefixValidate();
        request.setPayload(payload);

        var values = new String[][]{ {"welp", "   ", "", null}, {}, null };
        for (var value : values) {
            payload.setSupportingReferences(value);
            var answer = model.validateReferences(request);
            assertEquals(OK, answer.getHttpStatus(), "'" + Arrays.toString(value) + "' should be valid");
            assertTrue(isBlank(answer.getErrorMessage()));
        }
    }

    @Test
    void validateAdditionalInformation() {
        var payload = new ServiceRequestRegisterPrefixPayload();
        var request = new ServiceRequestRegisterPrefixValidate();
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
        var payload = new ServiceRequestRegisterPrefixPayload();
        var request = new ServiceRequestRegisterPrefixValidate();
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
        var payload = new ServiceRequestRegisterPrefixPayload();
        var request = new ServiceRequestRegisterPrefixValidate();
        request.setPayload(payload);
        var requester = new Requester();
        payload.setRequester(requester);;

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
        var payload = new ServiceRequestRegisterPrefixPayload();
        var request = new ServiceRequestRegisterPrefixValidate();
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
        var payload = new ServiceRequestRegisterPrefixPayload();
        var request = new ServiceRequestRegisterPrefixValidate();
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
        var payload = new ServiceRequestRegisterPrefixPayload();
        var request = new ServiceRequestRegisterPrefixValidate();
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