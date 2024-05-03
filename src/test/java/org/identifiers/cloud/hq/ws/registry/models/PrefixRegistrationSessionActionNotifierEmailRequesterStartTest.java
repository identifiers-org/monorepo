package org.identifiers.cloud.hq.ws.registry.models;

import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.identifiers.cloud.hq.ws.registry.data.models.PrefixRegistrationRequest;
import org.identifiers.cloud.hq.ws.registry.data.models.PrefixRegistrationSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PrefixRegistrationSessionActionNotifierEmailRequesterStartTest {

    @Autowired
    PrefixRegistrationSessionActionNotifierEmailRequesterStart requester;

    @RegisterExtension
    static GreenMailExtension mailServer = new GreenMailExtension(ServerSetupTest.SMTP);

    @BeforeEach
    void setUserPassword(@Value("${spring.mail.username}")
                         String username,
                         @Value("${spring.mail.password}")
                         String password) {
        mailServer.setUser(username, password);
    }

    @Test
    void testPerformAction() {
        var request = new PrefixRegistrationRequest();
        request.setRequesterEmail("someemail@gmail.com");
        request.setRequestedPrefix("prefix");
        request.setDescription("description");
        request.setName("name");
        request.setRequesterName("Requester");

        var session = new PrefixRegistrationSession();
        session.setPrefixRegistrationRequest(request);

        requester.performAction(session);

        // One message for the requester and one for idorg mailing list
        var messages = mailServer.getReceivedMessages();
        assertEquals(2, messages.length);

        // One actual email request
        long numberOfDistinctMessageIds = Arrays.stream(messages)
                .map(this::getMessageId)
                .unordered().distinct().count();
        assertEquals(1L, numberOfDistinctMessageIds);
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
}