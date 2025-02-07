package org.identifiers.cloud.commons.urlchecking;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class HttpClientHelperTest {

    @ParameterizedTest(name = "useCurlCerts = {0}")
    @ValueSource(booleans = {true, false})
    void checkClientCreation(boolean useCurlCerts) throws IOException {
        var sslFactory = HttpClientHelper.getBaseSSLFactoryBuilder(useCurlCerts).build();
        var client = HttpClientHelper.getBaseHttpClientBuilder(sslFactory).build();
        assertNotNull(client);
    }
}