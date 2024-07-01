package org.identifiers.cloud.hq.ws.registry.models;

import lombok.NonNull;
import org.identifiers.cloud.hq.ws.registry.data.models.PrefixRegistrationRequest;
import org.identifiers.cloud.hq.ws.registry.data.models.PrefixRegistrationSession;
import org.identifiers.cloud.hq.ws.registry.data.models.ResourceRegistrationRequest;
import org.identifiers.cloud.hq.ws.registry.data.models.ResourceRegistrationSession;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RegistrationSessionActionEmailNotifierTest {
    public static final String REQUESTER_NAME = "Developer";
    public static final String REQUESTER_EMAIL = "dev@email.com";
    public static final String PREFIX = "uniprot";
    public static final String DESCRIPTION = "This is the description!";
    public static final String NAME = "The provider!";

    @Autowired
    List<PrefixRegistrationSessionActionEmailNotifier> prefixNotifiers;
    @Autowired
    List<ResourceRegistrationSessionActionEmailNotifier> resourceNotifiers;


    @Test
    void bodiesAreCorrectForResourceNotifiers() {
        var session = getResourceRegistrationSession();

        resourceNotifiers.forEach(notifier -> {
            var body = notifier.parseEmailBody(session);
            assertNoPlaceholderLeft(body);
            assertBodyContainsData(body);
        });
    }

    @Test
    void bodiesAreCorrectForNamespaceNotifiers() {
        var session = getPrefixRegistrationSession();

        prefixNotifiers.forEach(notifier -> {
            var body = notifier.parseEmailBody(session);
            assertNoPlaceholderLeft(body);
            assertBodyContainsData(body);
        });
    }


    @Test
    void subjectsAreCorrectForPrefixNotifiers() {
        var session = getPrefixRegistrationSession();

        prefixNotifiers.forEach(notifier -> {
            var subject = notifier.parseEmailSubject(session);
            assertNoPlaceholderLeft(subject);
            assertSubjectContainsData(subject);
        });
    }

    @Test
    void subjectsAreCorrectForResourceNotifiers() {
        var session = getResourceRegistrationSession();

        resourceNotifiers.forEach(notifier -> {
            var subject = notifier.parseEmailSubject(session);
            assertNoPlaceholderLeft(subject);
            assertSubjectContainsData(subject);
        });
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




    // Helpers
    private static void assertNoPlaceholderLeft(String text) {
        assertFalse(text.contains("PLACEHOLDER"), "Placeholder not replaced in template");
    }

    private static void assertBodyContainsData(String body) {
        assertTrue(body.contains(REQUESTER_NAME));
        assertTrue(body.contains(PREFIX));
        assertTrue(body.contains(DESCRIPTION));
        assertTrue(body.contains(NAME));
    }

    private static void assertSubjectContainsData(String subject) {
        assertTrue(subject.contains(PREFIX));
    }
}