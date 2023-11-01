package org.identifiers.cloud.hq.ws.registry.models.validators;

import org.identifiers.cloud.hq.ws.registry.MockedRepositoriesTestConfiguration;
import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefixPayload;
import org.junit.Test;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.function.Function;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(JUnit4.class)
public class PrefixRegistrationRequestValidatorAdditionalInformationTest {
    PrefixRegistrationRequestValidatorAdditionalInformation validator =
            new PrefixRegistrationRequestValidatorAdditionalInformation();

    Function<String, ServiceRequestRegisterPrefixPayload> fnc =
            (p) -> new ServiceRequestRegisterPrefixPayload().setAdditionalInformation(p);

    @Test
    public void testBlankAdditionalInformation() {
        assertTrue(validator.validate(fnc.apply("")));
    }

    @Test
    public void testNullAdditionalInformation() {
        assertTrue(validator.validate(fnc.apply(null)));
    }

    @Test
    public void testValidAdditionalInformation() {
        assertTrue(validator.validate(fnc.apply("Additional information")));
    }
}