package org.identifiers.cloud.ws.sparql.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.identifiers.cloud.commons.messages.responses.ServiceResponse;
import org.identifiers.cloud.commons.messages.responses.registry.ResolverDatasetPayload;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.util.ResourceUtils;

import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RdfsMemberResolverTest {
    static final RdfsMemberResolver RESOLVER = new RdfsMemberResolver();
    static final ValueFactory vf = SimpleValueFactory.getInstance();

    @BeforeAll
    static void setup() throws IOException {
        var testResolutionDatasetFile = ResourceUtils.getFile("classpath:resolutionDataset.json");
        try (var fis = new FileInputStream(testResolutionDatasetFile)) {
            ObjectMapper objectMapper = new ObjectMapper();
            var typeRef = new TypeReference<ServiceResponse<ResolverDatasetPayload>>() {};
            var testResolutionDataset = objectMapper.readValue(fis, typeRef);

            RESOLVER.receive(testResolutionDataset.getPayload());
        }
    }

    @ParameterizedTest
    @CsvSource(value = {
            "http://identifiers.org/uniprot:P12345,,true",
            ",http://identifiers.org/uniprot:P12345,true",
            "https://identifiers.org/uniprot:P12345,,true",
            ",https://identifiers.org/uniprot:P12345,true",
            "http://identifiers.org/uniprot:P12345,http://identifiers.org/uniprot:P12345,false",
            ",,false",
            "http://google.com?q=rickroll,,false",
            ",http://google.com?q=rickroll,false",
    })
    void mayGenerateStatements(String subject, String obj, boolean expected) {
        var r1 = subject == null ? null : vf.createIRI(subject);
        var r2 = obj == null ? null : vf.createIRI(obj);
        var out = RESOLVER.mayGenerateStatements(r1, RDFS.MEMBER, r2);
        assertEquals(expected, out);
    }

    @ParameterizedTest(name = "{0}")
    @CsvSource(value = {
            "http://identifiers.org/uniprot:P12345,true",
            "http://identifiers.org/uniprot/P12345,true",
            "https://identifiers.org/uniprot:P12345,true",
            "https://identifiers.org/uniprot/P12345,true",
            "http://identifiers.org/uniprot,true",
            "https://identifiers.org/uniprot,true",
            "ftp://ftp.domain.com,false",
            "http://google.com,false",
            "http://docs.identifiers.org,false"

    })
    void isIdOrgUri(String iriStr, boolean expectedComparison) {
        Resource res = vf.createIRI(iriStr);
        assertEquals(expectedComparison, RESOLVER.isIdOrgUri(res));

        Value value = vf.createIRI(iriStr);
        assertEquals(expectedComparison, RESOLVER.isIdOrgUri(value));
    }

    @ParameterizedTest(name = "{0}")
    @CsvSource(value = {
            "http://identifiers.org/uniprot:P12345,uniprot",
            "http://identifiers.org/uniprot/P12345,uniprot",
            "https://identifiers.org/uniprot:P12345,uniprot",
            "https://identifiers.org/uniprot/P12345,uniprot",
            "http://identifiers.org/uniprot,",
            "https://identifiers.org/uniprot,",
            "https://identifiers.org/uniprot:,",
            "ftp://ftp.domain.com,",
            "http://google.com,",
            "http://docs.identifiers.org,"
    })
    void getNamespaceOfResourceIfExists(String iriStr, String expectedPrefix) {
        var res = vf.createIRI(iriStr);
        var out = RESOLVER.getNamespaceOfResourceIfExists(res);

        if (expectedPrefix != null) {
            assertTrue(out.isPresent());
            assertTrue(out.get().toString().endsWith(expectedPrefix));
        } else {
            assertTrue(out.isEmpty());
        }
    }
}