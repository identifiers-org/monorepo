package org.identifiers.cloud.ws.resolver.models;

import org.identifiers.cloud.ws.resolver.data.models.Namespace;
import org.identifiers.cloud.ws.resolver.data.repositories.NamespaceRespository;
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
public class CompactIdParsingHelperTest {

    @Autowired
    CompactIdParsingHelper helper;

    @MockBean
    NamespaceRespository namespaceRespository;

    @Before
    public void setupNamespaceRepository() {
        Namespace namespace = new Namespace()
                .setPrefix("existing_namespace")
                .setPattern("\\d+").setDeprecated(false)
                .setNamespaceEmbeddedInLui(false);
        Namespace luinamespace = new Namespace()
                .setPrefix("lui_namespace")
                .setPattern("\\d+").setDeprecated(false)
                .setNamespaceEmbeddedInLui(true);
        namespace.setResources(Collections.emptyList());

        //Order is important here
        doReturn(null).when(namespaceRespository).findByPrefix(anyString());
        doReturn(namespace).when(namespaceRespository).findByPrefix("existing_namespace");
        doReturn(luinamespace).when(namespaceRespository).findByPrefix("lui_namespace");
    }

    @Test
    public void parseCompactIdRequests() {
        testRawRequest("Namespace only",
                "existing_namespace",
                null, "existing_namespace", null);

        testRawRequest("Namespace w/lui",
                "existing_namespace:123",
                null, "existing_namespace", "123");
        testRawRequest("Namespace w/ invalid lui",
                "existing_namespace:abc",
                null, "existing_namespace", "abc");
        testRawRequest("Namespace w/lui using slash",
                "existing_namespace/123",
                null, "existing_namespace", "123");

        testRawRequest("Namespace w/lui and provider code",
                "provider/existing_namespace:123",
                "provider", "existing_namespace", "123");

        //FIXME: The parsed result below should be the same as the previous
        testRawRequest("Namespace w/lui and provider code using slash",
                "provider/existing_namespace/123",
                "provider", null, null);
    }

    @Test
    public void parseCompactIdRequestsWithNonExistingNamespace() {
        // FIXME: The helper is inconsistent when the namespace doesn't exist
        testRawRequest("Non existing namespace only",
                "non_existing_namespace",
                null, null, null);

        testRawRequest("Non existing namespace w/lui",
                "non_existing_namespace:123",
                null, "non_existing_namespace", "123");
        testRawRequest("Non existing namespace w/lui using slash",
                "non_existing_namespace/123",
                "non_existing_namespace", null, null);

        testRawRequest("Non existing namespace w/lui using slash",
                "provider/non_existing_namespace:123",
                "provider", null, null);
        testRawRequest("Non existing namespace w/lui using slash",
                "provider/non_existing_namespace/123",
                "provider", null, null);
    }

    @Test
    public void parseCompactIdRequestsWithLuiNamespace() {
        testRawRequest("Namespace w/lui",
                "lui_namespace:123",
                null, "lui_namespace", "lui_namespace:123");
        testRawRequest("Namespace w/lui and provider code",
                "provider/lui_namespace:123",
                "provider", "lui_namespace", "lui_namespace:123");

        //FIXME: The below is inconsistent
        testRawRequest("Namespace w/lui and provider code",
                "provider/lui_namespace/123",
                "provider", null, null);
    }

    void testRawRequest(String msg, String rawRequest, String providerCode, String namespace, String localId) {
        ParsedCompactIdentifier pci = helper.parseCompactIdRequest(rawRequest);
        assertEquals(msg, rawRequest, pci.getRawRequest());
        assertEquals(msg, providerCode, pci.getProviderCode());
        assertEquals(msg, namespace, pci.getNamespace());
        assertEquals(msg, localId, pci.getLocalId());
    }
}