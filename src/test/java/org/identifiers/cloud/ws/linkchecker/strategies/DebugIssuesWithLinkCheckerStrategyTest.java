package org.identifiers.cloud.ws.linkchecker.strategies;

import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.ws.linkchecker.TestRedisServer;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@Disabled("This is meant to be used by devs to debug problematic URLs, for continuous test, because http traffic is not mocked")
@Execution(ExecutionMode.CONCURRENT)
@SpringBootTest(
        properties = {
                "logging.level.org.identifiers.cloud.ws.linkchecker.strategies=DEBUG",
                "org.identifiers.cloud.ws.linkchecker.daemon.websiteswithtrustedcerts="
        },
        classes = { TestRedisServer.class }
)
class DebugIssuesWithLinkCheckerStrategyTest {
    @Autowired
    LinkCheckerStrategy linkCheckerStrategy;

    static final List<String> UNPROTECTED_URLS = List.of(
            "ftp://ftp.embl-heidelberg.de/pub/databases/protein_extras/hssp/102l.hssp.bz2",
            "ftp://ftp.cmbi.ru.nl/pub/molbio/data/hssp/102l.hssp.bz2"
//            "https://getentry.ddbj.nig.ac.jp/getentry?database=ddbj&accession_number=X58356",
//            "http://www.ddbj.nig.ac.jp/"
//            "https://www.uniprot.org/",
//            "http://purl.uniprot.org/uniprot/P0DP23",
//            "https://omim.org/",
//            "https://omim.org/entry/603903",
//            "http://mirror.omim.org/",
//            "http://mirror.omim.org/entry/603903",
//            "http://www.proteopedia.org/",
//            "http://proteopedia.org/wiki/index.php/2gc4",
//            "https://pdbj.org/",
//            "https://pdbj.org/mine/summary/2gc4",
//            "http://flybase.org/captcha/reports/FBgn0011293",
//            "http://flybase.org/",
//            "https://www.uniprot.org/uniparc/",
//            "https://www.uniprot.org/uniparc/UPI000000000A/entry",
//            "http://arabidopsis.org/index.jsp",
//            "http://arabidopsis.org/servlets/TairObject?accession=AASequence:1009107926"
    );

    static final List<String> PROTECTED_URLS = List.of();

    static Stream<Arguments> getLinksToCheck() {
        return Stream.concat(
                UNPROTECTED_URLS.stream().map(s -> Arguments.of(s, false)),
                PROTECTED_URLS.stream().map(s -> Arguments.of(s, true))
        );
    }

    @ParameterizedTest(name="{0} (Accept401or403: {1})")
    @MethodSource("getLinksToCheck")
    void checkStrategyForUrl(String urlString, boolean accept401or403) throws MalformedURLException {
        var url = new URL(urlString);
        var report = linkCheckerStrategy.check(url, accept401or403);
        log.info("URL check report: {}", report);
        assertTrue(report.isUrlAssessmentOk(), "Assessment should be OK");
        assertTrue(HttpStatus.valueOf(report.getHttpStatus()).is2xxSuccessful(), "Status should be OK");
    }
}