package org.identifiers.cloud.hq.validatorregistry.curation.verifiers;

import org.identifiers.cloud.commons.messages.models.Resource;
import org.identifiers.cloud.commons.messages.responses.ServiceResponse;
import org.identifiers.cloud.commons.messages.responses.linkchecker.ServiceResponseResourceAvailabilityPayload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class AvailabilityVerifierTest {
    AvailabilityVerifier availabilityVerifier;

    @BeforeEach
    void setUpMocks() {
        var mockAvailability = new ServiceResponseResourceAvailabilityPayload();
        mockAvailability.add(new ServiceResponseResourceAvailabilityPayload.Item(10, 0.0f));
        mockAvailability.add(new ServiceResponseResourceAvailabilityPayload.Item(15, 100.0f));

        var serviceResponse = ServiceResponse.of(mockAvailability);
        var responseEntity = ResponseEntity.ok(serviceResponse);

        RestTemplate restTemplate = mock();
        doReturn(responseEntity).when(restTemplate).exchange(
                        any(), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)
        );

        availabilityVerifier = new AvailabilityVerifier(restTemplate, mock(),
                "http://google.com", 25);
    }

    @Test
    void verify() {
        availabilityVerifier.preValidateTask();

        var resource = new Resource()
                .setId(10)
                .setSampleId("Rick Astley")
                .setResourceHomeUrl("http://google.com")
                .setUrlPattern("http://google.com?q={$id}");

        var notification = availabilityVerifier.doValidate(resource);
        assertNotNull(notification);
        assertTrue(notification.isPresent());
        assertEquals(10, notification.get().getTargetId());

        resource.setId(15);
        notification = availabilityVerifier.doValidate(resource);
        assertNotNull(notification);
        assertFalse(notification.isPresent());

        resource.setId(20);
        notification = availabilityVerifier.doValidate(resource);
        assertNotNull(notification);
        assertFalse(notification.isPresent());
    }

}