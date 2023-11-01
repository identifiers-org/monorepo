package org.identifiers.cloud.hq.ws.registry.models.validators;


import junit.framework.TestCase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.net.HttpURLConnection;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class WebPageCheckerDefaultTest extends TestCase {
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();
    @Spy
    WebPageCheckerDefault checker;
    @Mock
    HttpURLConnection connection;


    @Test
    public void testHttpsRewrite() throws IOException {
        exceptionRule.expect(WebPageCheckerException.class);
//        exceptionRule.expectMessage("It seems that a https rewrite is in place. Use https instead of http.");

        doReturn(connection).when(checker).getConnection(any());
        doReturn(HttpStatus.MOVED_PERMANENTLY.value()).when(connection).getResponseCode();
        doReturn("https://google.com").when(connection).getHeaderField("Location");
        String url = "http://google.com";

        checker.checkWebPageUrl(url);
    }

    @Test
    public void testValid2xxResponse() throws IOException {
        for (int r=200; r<=206; r++) {
            doReturn(connection).when(checker).getConnection(any());
            doReturn(r).when(connection).getResponseCode();
            String url = "https://google.com";

            assertTrue("A valid connection failed", checker.checkWebPageUrl(url));
        }
    }

    @Test
    public void testInvalid3xxResponse() throws IOException {
        for (int r=300; r<=303; r++) {
            try {
                doReturn(connection).when(checker).getConnection(any());
                doReturn(r).when(connection).getResponseCode();
                String url = "https://google.com";

                checker.checkWebPageUrl(url);
            } catch (RuntimeException e) {
                assertThat(e, instanceOf(WebPageCheckerException.class));
            }
        }
    }

    @Test
    public void testInvalid4xxResponse() throws IOException {
        for (int r=400; r<=417; r++) {
            try {
                doReturn(connection).when(checker).getConnection(any());
                doReturn(r).when(connection).getResponseCode();
                String url = "https://google.com";

                checker.checkWebPageUrl(url);
            } catch (RuntimeException e) {
                assertThat(e, instanceOf(WebPageCheckerException.class));
            }
        }
    }

    @Test
    public void testInvalid5xxResponse() throws IOException {
        for (int r=500; r<=505; r++) {
            try {
                doReturn(connection).when(checker).getConnection(any());
                doReturn(r).when(connection).getResponseCode();
                String url = "https://google.com";

                checker.checkWebPageUrl(url);
            } catch (RuntimeException e) {
                assertThat(e, instanceOf(WebPageCheckerException.class));
            }
        }
    }
}