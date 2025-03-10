package org.identifiers.cloud.hq.validatorregistry.configurations;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
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
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.USER_AGENT;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@EnableScheduling
@Configuration
public class ApplicationConfiguration {
    @Bean
    ExecutorService executorService() {
        var threadFactory = new ThreadFactoryBuilder().setNameFormat("validation-%d").build();
        return Executors.newFixedThreadPool(30, threadFactory);
    }

    @Bean
    RestTemplate restTemplate(
            RestTemplateBuilder restTemplateBuilder,
            @Value("${app.version}") String appVersion,
            @Value("${java.version}") String javaVersion,
            @Value("${app.contact}") String appContact
    ) {
        var idorgAgentStr = String.format(
                "IdorgRegistryValidator/%s (%s) Java-http-client/%s",
                javaVersion, appContact, appVersion);
        return restTemplateBuilder
                .defaultHeader(ACCEPT, APPLICATION_JSON_VALUE)
                .defaultHeader(USER_AGENT, idorgAgentStr)
                .build();
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
