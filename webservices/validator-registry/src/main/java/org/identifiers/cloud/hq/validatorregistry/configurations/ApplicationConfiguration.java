package org.identifiers.cloud.hq.validatorregistry.configurations;

import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.TokenizerModel;
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;
import org.identifiers.cloud.commons.urlchecking.HttpClientHelper;
import org.identifiers.cloud.commons.urlchecking.UrlChecker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Configuration
public class ApplicationConfiguration {
    @Bean
    RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.defaultHeader(ACCEPT, APPLICATION_JSON_VALUE).build();
    }

    @Bean
    UrlChecker urlChecker() throws IOException {
        var sslFactory = HttpClientHelper.getBaseSSLFactoryBuilder(true).build();
        var httpClient = HttpClientHelper.getBaseHttpClientBuilder(sslFactory).build();
        return new UrlChecker(httpClient);
    }

    @Bean
    TokenNameFinderModel tokenNameFinderModel(@Value("classpath:en-ner-organization.bin")
                                              Resource tokenNameFinderModelBin) throws IOException {
        return new TokenNameFinderModel(tokenNameFinderModelBin.getInputStream());
    }

    @Bean
    TokenizerModel tokenizerModel(@Value("classpath:en-token.bin")
                                  Resource tokenizerModelBin) throws IOException{
        return new TokenizerModel(tokenizerModelBin.getInputStream());
    }

    @Bean
    SPARQLRepository wikidataSparqlRepository(@Value("${org.identifiers.cloud.wikidata.sparql-endpoint}")
                                              String wikidataSparqlEndpoint) {
        return new SPARQLRepository(wikidataSparqlEndpoint);
    }
}
