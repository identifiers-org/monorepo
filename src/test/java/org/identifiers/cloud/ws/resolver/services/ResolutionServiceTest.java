package org.identifiers.cloud.ws.resolver.services;

import org.identifiers.cloud.ws.resolver.data.models.Location;
import org.identifiers.cloud.ws.resolver.data.models.Namespace;
import org.identifiers.cloud.ws.resolver.data.models.Resource;
import org.identifiers.cloud.ws.resolver.data.repositories.NamespaceRespository;
import org.identifiers.cloud.ws.resolver.models.CompactIdParsingHelper;
import org.identifiers.cloud.ws.resolver.models.ParsedCompactIdentifier;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ResolutionServiceTest {
    @Autowired ResolutionService resolutionService;
    @Autowired CompactIdParsingHelper helper;

    @MockBean
    NamespaceRespository namespaceRespository;



    @Before
    public void setupNamespaceRepository() {
        Namespace namespace = new Namespace()
                .setPrefix("existing_namespace")
                .setPattern("\\d+").setDeprecated(false);
        Resource resource = new Resource()
                .setId(1).setName("valid resource").setDeprecated(false)
                .setUrlPattern("https://mockedurl.com?q={$id}")
                .setLocation(new Location().setCountryCode("GB").setCountryName("UK"));
        namespace.setResources(Collections.singletonList(resource));

        //Order is important here
        doReturn(null).when(namespaceRespository).findByPrefix(anyString());
        doReturn(namespace).when(namespaceRespository).findByPrefix("existing_namespace");
    }

    @Test
    public void testResolveExistingNamespace() {
        ParsedCompactIdentifier pci = helper.parseCompactIdRequest("existing_namespace");
        ResolutionServiceResult result = resolutionService.resolve(pci);
        assertTrue("Finds valid namespace", result.isResolved());
        assertEquals("Only idorg is found as resource for a namespace",
                1, result.getResolvedResources().size());
        assertNull("Resolved namespace should be null",
                result.getResolvedResources().get(0).getNamespacePrefix());
    }

    @Test
    public void testResolveNonExistingNamespace() {
        ParsedCompactIdentifier pci = helper.parseCompactIdRequest("non_namespace");
        ResolutionServiceResult result = resolutionService.resolve(pci);
        assertFalse("Finds valid namespace", result.isResolved());
        assertEquals("Only idorg is found as resource for a namespace",
                0, result.getResolvedResources().size());
    }

    @Test
    public void testResolveValidCID() {
        ParsedCompactIdentifier pci = helper.parseCompactIdRequest("existing_namespace:123");
        ResolutionServiceResult result = resolutionService.resolve(pci);
        assertTrue("Finds valid namespace", result.isResolved());
        assertEquals("only one resource is found for it",1, result.getResolvedResources().size());
        String resolvedURL = result.getResolvedResources().get(0).getCompactIdentifierResolvedUrl();
        assertTrue("Resolved resource is mocked", resolvedURL.startsWith("https://mockedurl.com"));
        assertFalse("Resolved URL should not contain template variable", resolvedURL.contains("{$id}"));
    }

    @Test
    public void testResolveNonValidCID() {
        ParsedCompactIdentifier pci = helper.parseCompactIdRequest("existing_namespace:abc");
        ResolutionServiceResult result = resolutionService.resolve(pci);
        assertFalse("Should not resolve invalid local id", result.isResolved());
        assertTrue("Empty resource for invalid local id", result.getResolvedResources().isEmpty());
    }
}