package org.identifiers.cloud.hq.ws.registry.models;

import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.NonNull;
import org.identifiers.cloud.hq.ws.registry.data.models.PrefixRegistrationRequest;
import org.identifiers.cloud.hq.ws.registry.data.models.PrefixRegistrationSession;
import org.identifiers.cloud.hq.ws.registry.data.models.ResourceRegistrationRequest;
import org.identifiers.cloud.hq.ws.registry.data.models.ResourceRegistrationSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RegistrationSessionActionEmailNotifierSMTPTest {
    public static final String REQUESTER_NAME = "Developer";
    public static final String REQUESTER_EMAIL = "dev@email.com";
    public static final String PREFIX = "uniprot";
    public static final String DESCRIPTION = "This is the description!";
    public static final String NAME = "The provider!";

    @Autowired
    List<PrefixRegistrationSessionActionEmailNotifier> prefixNotifiers;
    @Autowired
    List<ResourceRegistrationSessionActionEmailNotifier> resourceNotifiers;

    @Value("${spring.mail.username}")
    String username;
    @Value("${spring.mail.password}")
    String password;

    @RegisterExtension
    static GreenMailExtension mailServer = new GreenMailExtension(ServerSetupTest.SMTP);


    @BeforeEach
    void setUserPassword() {
        mailServer.setUser(username, password);
    }


    @Test
    void testNamespaceNotifiersPerformAction() {
        var session = getPrefixRegistrationSession();
        prefixNotifiers.forEach(notifier -> {
            notifier.performAction(session);
            assertNumberOfMessagesAndResetMockedServer();
        });
    }

    @Test
    void testResourceNotifiersPerformAction() {
        var session = getResourceRegistrationSession();
        resourceNotifiers.forEach(notifier -> {
            notifier.performAction(session);
            assertNumberOfMessagesAndResetMockedServer();
        });
    }


    private void assertNumberOfMessagesAndResetMockedServer() {
        // One message for the requester and one for idorg mailing list
        var messages = mailServer.getReceivedMessages();
        assertEquals(2, messages.length);

        // One actual email request
        long numberOfDistinctMessageIds = Arrays.stream(messages)
                .map(this::getMessageId)
                .unordered().distinct().count();
        assertEquals(1L, numberOfDistinctMessageIds);

        mailServer.reset();
        setUserPassword();
    }

    private String getMessageId(MimeMessage message) {
        try {
            return message.getMessageID();
        } catch (MessagingException e) {
            fail(e.getMessage());
        }
        fail();
        return null;
    }


    // Test data
    @NonNull
    private static ResourceRegistrationSession getResourceRegistrationSession() {
        var request = new ResourceRegistrationRequest();
        request.setId(30);
        request.setProviderName(NAME);
        request.setNamespacePrefix(PREFIX);
        request.setProviderDescription(DESCRIPTION);
        request.setRequesterName(REQUESTER_NAME);
        request.setRequesterEmail(REQUESTER_EMAIL);

        var session = new ResourceRegistrationSession();
        session.setId(10);
        session.setClosed(false);
        session.setCreated(new Date());
        session.setResourceRegistrationRequest(request);
        return session;
    }

    @NonNull
    private static PrefixRegistrationSession getPrefixRegistrationSession() {
        var request = new PrefixRegistrationRequest();
        request.setId(30);
        request.setName(NAME);
        request.setRequestedPrefix(PREFIX);
        request.setDescription(DESCRIPTION);
        request.setRequesterName(REQUESTER_NAME);
        request.setRequesterEmail(REQUESTER_EMAIL);

        var session = new PrefixRegistrationSession();
        session.setId(10);
        session.setClosed(false);
        session.setCreated(new Date());
        session.setPrefixRegistrationRequest(request);
        return session;
    }
}