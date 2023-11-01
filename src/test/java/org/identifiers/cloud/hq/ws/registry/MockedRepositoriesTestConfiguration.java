package org.identifiers.cloud.hq.ws.registry;

import org.identifiers.cloud.hq.ws.registry.data.models.Namespace;
import org.identifiers.cloud.hq.ws.registry.data.repositories.NamespaceRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@TestConfiguration
public class MockedRepositoriesTestConfiguration {
    @Bean
    @Primary
    NamespaceRepository getMockedNamespaceRespository() {
        NamespaceRepository mockedNsRepo = mock(NamespaceRepository.class);
        Namespace ns = new Namespace().setPrefix("namespace1");
        doReturn(null).when(mockedNsRepo).findByPrefix(anyString());
        doReturn(ns).when(mockedNsRepo).findByPrefix("namespace1");

        return mockedNsRepo;
    }
}
