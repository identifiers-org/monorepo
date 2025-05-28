package org.identifiers.cloud.ws.resolver.services.reverseresolution;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.identifiers.cloud.commons.messages.requests.resolver.ReverseResolutionRequestPayload;
import org.identifiers.cloud.commons.messages.responses.resolver.ReverseResolutionMatch;
import org.identifiers.cloud.ws.resolver.TestRedisServer;
import org.identifiers.cloud.ws.resolver.data.repositories.NamespaceRespository;
import org.identifiers.cloud.ws.resolver.periodictasks.models.HqServiceResponseGetResolverDataset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.util.ResourceUtils;

import java.io.IOException;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(
        classes = {TestRedisServer.class},
        properties = {
                "logging.level.org.identifiers.cloud.ws.resolver=DEBUG"
        }
)
class ReverseResolutionServiceTest {
    @Autowired
    ReverseResolutionService service;

    @MockBean
    NamespaceRespository namespaceRespository;

    @BeforeEach
    void setUp() throws IOException {
        var namespaceFile = ResourceUtils.getFile("classpath:testResolutionDataset.json");
        var objectMapper = new ObjectMapper();
        objectMapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);

        var namespaces = objectMapper.readValue(
                namespaceFile,
                HqServiceResponseGetResolverDataset.class)
            .getPayload().getNamespaces();

        when(namespaceRespository.findAll()).thenReturn(namespaces::iterator);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "https://www.ncbi.nlm.nih.gov/datasets/genome/GCF_000001405,refseq.gcf",
            "http://www.kegg.jp/entry/K00001,kegg.orthology",
            "https://www.ensembl.org/id/ENSG00000139618,ensembl",
            "https://www.ncbi.nlm.nih.gov/gene/100010,ncbigene",
            "https://pubmed.ncbi.nlm.nih.gov/16333295/,pubmed",
            "https://reactome.org/content/detail/R-HSA-201451,reactome",
            "https://www.ensembl.org/id/ENG00000139618,ensembl", // Bad Lui
            "https://pubmed.ncbi.nlm.nih.gov/abc/,pubmed", // Bad Lui
            "https://reactome.org/content/detail/R-HSA201451,reactome", // Bad Lui
            "https://google.com/?q=12345,", // Bad URL
            "https://http.cat/status/200,", // Bad URL
            "https://www.youtube.com/watch?v=dQw4w9WgXcQ&t=3s," // Bad URL
    })
    void resolveByPrefixGuessAccession(String url, String expectedPrefix) {
        var payload = new ReverseResolutionRequestPayload(url, null, false);
        var answer = service.resolveBasedOnPrefix(payload);

        if (expectedPrefix != null) {
            assertTrue(answer.isPresent());
            assertEquals(expectedPrefix, answer.get().getPrefix());
            if (answer.get().isLuiPatternMatch()) {
                assertNotNull(answer.get().getPossibleIdorgCurie());
                assertNotNull(answer.get().getPossibleIdorgUrl());
            } else {
                assertNull(answer.get().getPossibleIdorgCurie());
                assertNull(answer.get().getPossibleIdorgUrl());
            }
        } else {
            assertTrue(answer.isEmpty());
        }
    }

    @ParameterizedTest
    @CsvSource(value = {
            "https://www.ensembl.org/Homo_sapiens/Gene/Summary?g=ENSG00000139618,ensembl",
            "https://www.uniprot.org/uniprotkb/P0DP23/entry,uniprot",
            "http://www.uniprot.org/uniprot/Q5BJF6-3,uniprot"
    })
    void resolveBySimilarityGuessAccession(String url, String expectedPrefix) {
        var payload = new ReverseResolutionRequestPayload(url, null, false);
        var answer = service.resolveBasedOnSimilarity(payload);

        assertFalse(answer.isEmpty());
        boolean isExpectedPrefixAmongResults = answer
                .stream()
                .map(ReverseResolutionMatch::getPrefix)
                .anyMatch(expectedPrefix::equals);
        assertTrue(isExpectedPrefixAmongResults);
    }
}