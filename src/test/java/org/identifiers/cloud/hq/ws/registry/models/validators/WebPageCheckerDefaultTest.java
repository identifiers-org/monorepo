package org.identifiers.cloud.hq.ws.registry.models.validators;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.net.HttpURLConnection;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WebPageCheckerDefaultTest {
    @Spy
    WebPageCheckerDefault checker;
    @Mock
    HttpURLConnection connection;


    @Test
    void testHttpsRewrite() throws IOException {
        doReturn(connection).when(checker).getConnection(any());
        doReturn(HttpStatus.MOVED_PERMANENTLY.value()).when(connection).getResponseCode();
        doReturn("https://google.com").when(connection).getHeaderField("Location");
        String url = "http://google.com";
        assertThrows(WebPageCheckerException.class, () -> checker.checkWebPageUrl(url));
    }

    @Test
    void testValid2xxResponse() throws IOException {
        for (int r=200; r<=206; r++) {
            doReturn(connection).when(checker).getConnection(any());
            doReturn(r).when(connection).getResponseCode();
            String url = "https://google.com";

            assertTrue(checker.checkWebPageUrl(url), "A valid connection failed");
        }
    }

    @Test
    void testInvalid3xxResponse() throws IOException {
        for (int r=300; r<=303; r++) {
            doReturn(connection).when(checker).getConnection(any());
            doReturn(r).when(connection).getResponseCode();
            String url = "https://google.com";

            assertTrue(checker.checkWebPageUrl(url));
        }
    }

    @Test
    void testInvalid4xxResponse() throws IOException {
        for (int r=400; r<=417; r++) {
            doReturn(connection).when(checker).getConnection(any());
            doReturn(r).when(connection).getResponseCode();
            String url = "https://google.com";
            assertThrows(WebPageCheckerException.class, () -> checker.checkWebPageUrl(url));
        }
    }

    @Test
    void testInvalid5xxResponse() throws IOException {
        for (int r=500; r<=505; r++) {
            doReturn(connection).when(checker).getConnection(any());
            doReturn(r).when(connection).getResponseCode();
            String url = "https://google.com";
            assertThrows(WebPageCheckerException.class, () -> checker.checkWebPageUrl(url));
        }
    }
}