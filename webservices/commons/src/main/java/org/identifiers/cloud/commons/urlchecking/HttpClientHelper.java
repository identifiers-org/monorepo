package org.identifiers.cloud.commons.urlchecking;

import nl.altindag.ssl.SSLFactory;
import nl.altindag.ssl.pem.util.PemUtils;
import nl.altindag.ssl.util.CertificateUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URL;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

/**
 * A helper to set up the SSL context and the HTTPClient used for link validation.
 * Builders are returned in case further customization is required by the service using it
 */
public class HttpClientHelper {

    // See https://curl.se/ca/cacert.pem
    private static final String curlTrustedCaCertUrl = "https://curl.se/ca/cacert.pem";

    public static SSLFactory.Builder getBaseSSLFactoryBuilder(boolean useCurlCerts,
                                                              String... websitesWithTrustedCerts) throws IOException {
        var sslFactoryBuilder = SSLFactory.builder()
                .withDefaultTrustMaterial()
                .withSystemTrustMaterial();

        // This is a mean to add extra websites
        if (Stream.of(websitesWithTrustedCerts).anyMatch(StringUtils::hasText)) {
            var urlCertificates = CertificateUtils.getCertificatesFromExternalSources(websitesWithTrustedCerts);
            urlCertificates.values().forEach(sslFactoryBuilder::withTrustMaterial);
        }

        if (useCurlCerts) {
            try (var certInputStream = new URL(curlTrustedCaCertUrl).openStream()) {
                var trustManager = PemUtils.loadTrustMaterial(certInputStream);
                sslFactoryBuilder.withTrustMaterial(trustManager);
            }
        }

        return sslFactoryBuilder;
    }

    public static HttpClient.Builder getBaseHttpClientBuilder(SSLFactory sslFactory) {
        return HttpClient.newBuilder()
                .sslContext(sslFactory.getSslContext())
                .sslParameters(sslFactory.getSslParameters())
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .connectTimeout(Duration.ofMinutes(2))
                .executor(Executors.newCachedThreadPool())
                .version(HttpClient.Version.HTTP_1_1);
    }
}
