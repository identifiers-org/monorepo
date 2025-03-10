package org.identifiers.cloud.hq.validatorregistry.curation;

import org.identifiers.cloud.commons.messages.models.CurationWarningNotification;
import org.identifiers.cloud.commons.messages.models.Namespace;
import org.identifiers.cloud.commons.messages.models.Resource;
import org.identifiers.cloud.hq.validatorregistry.registryhelpers.CurationWarningNotificationPoster;
import org.identifiers.cloud.hq.validatorregistry.registryhelpers.ResolutionDatasetFetcher;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@Disabled("This is meant to be debug code")
@SpringBootTest
class DebugCurationEngineTest {
    @Autowired
    private ResolutionDatasetFetcher fetcher;
    @Autowired
    private CurationEngine curationEngine;
    @SpyBean
    private CurationWarningNotificationPoster poster;
    @Captor
    private ArgumentCaptor<List<CurationWarningNotification>> notifications;

    @Test
    void testEngine() throws ExecutionException, InterruptedException {
        curationEngine.setNamespacesEnabled(false);
        curationEngine.setResourcesEnabled(false);
        curationEngine.setInstitutionsEnabled(false);

        curationEngine.execute();

        verify(poster).post(notifications.capture());

        assertNotNull(notifications.getValue());
    }

    @ParameterizedTest
    @ValueSource(longs = {})
    void debugInstitution(long institutionId) {
        var institution = fetcher.fetch().stream()
                .map(Namespace::getResources)
                .flatMap(Collection::stream)
                .map(Resource::getInstitution)
                .filter(i -> i.getId() == institutionId)
                .findFirst();
        assertTrue(institution.isPresent());
        var notifications = curationEngine.validate(institution.get()).toList();
        assertNotNull(notifications);
    }



}