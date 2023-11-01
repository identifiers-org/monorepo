package org.identifiers.cloud.hq.ws.registry.models.validators;

import junit.framework.TestCase;
import org.identifiers.cloud.hq.ws.registry.MockedRepositoriesTestConfiguration;
import org.identifiers.cloud.hq.ws.registry.api.requests.ServiceRequestRegisterPrefixPayload;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.function.Function;

@SpringBootTest(classes = MockedRepositoriesTestConfiguration.class)
@RunWith(SpringRunner.class)
public class PrefixRegistrationRequestValidatorRequestedPrefixTest extends TestCase {

    @Autowired
    @Qualifier("PrefixRegistrationRequestValidatorRequestedPrefix")
    PrefixRegistrationRequestValidator prefixValidator;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    Function<String, ServiceRequestRegisterPrefixPayload> fnc =
            (p) -> new ServiceRequestRegisterPrefixPayload().setRequestedPrefix(p);

    @Test
    public void testPrefixValidatorBlankString() {
        exceptionRule.expect(PrefixRegistrationRequestValidatorException.class);

        prefixValidator.validate(fnc.apply(""));
    }

    @Test
    public void testPrefixValidatorUppercaseString() {
        exceptionRule.expect(PrefixRegistrationRequestValidatorException.class);
        prefixValidator.validate(fnc.apply("UPPERCASE_PREFIX"));
    }


    @Test
    public void testPrefixValidatorNullString() {
        exceptionRule.expect(PrefixRegistrationRequestValidatorException.class);
        prefixValidator.validate(fnc.apply(null));
    }


    @Test
    public void testPrefixValidatorNewValidPrefix() {
        assertTrue(prefixValidator.validate(fnc.apply("new_prefix")));
    }


    @Test
    public void testPrefixValidatorExistingNamespace() {
        exceptionRule.expect(PrefixRegistrationRequestValidatorException.class);
        prefixValidator.validate(fnc.apply("namespace1"));
    }

}