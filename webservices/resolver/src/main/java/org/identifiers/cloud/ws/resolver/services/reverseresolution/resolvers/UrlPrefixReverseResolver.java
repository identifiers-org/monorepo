package org.identifiers.cloud.ws.resolver.services.reverseresolution.resolvers;


import com.google.common.base.Suppliers;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.trie.PatriciaTrie;
import org.apache.commons.lang3.StringUtils;
import org.identifiers.cloud.ws.resolver.services.reverseresolution.RrResourceData;
import org.identifiers.cloud.ws.resolver.services.reverseresolution.RrDataSupplier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.concurrent.TimeUnit.SECONDS;

@Slf4j
@Service
public class UrlPrefixReverseResolver {
    private final Supplier<PrefixTree> reverseResolutionData;
    private final RrDataSupplier dataProvider;


    public UrlPrefixReverseResolver(RrDataSupplier dataProvider,
                                    @Value("${org.identifiers.cloud.ws.resolver.reverse-resolution.prefix.tree-cache-duration}")
                             Duration cacheDuration) {
        this.dataProvider = dataProvider;

        long cacheDurationSeconds = cacheDuration.toSeconds();

        assert cacheDurationSeconds > 0 : "Cache duration for prefix RR must be greater than zero";

        this.reverseResolutionData = Suppliers.memoizeWithExpiration(
                this::generatePrefixTree, cacheDurationSeconds, SECONDS
        );
    }


    public Optional<RrResourceData> resolve(String url, String accession) {
        if (StringUtils.isAnyBlank(url, accession)) return Optional.empty();
        var accessionIdx = url.indexOf(accession);
        if (accessionIdx == -1) return Optional.empty();

        String urlPrefix = url.substring(0, accessionIdx);
        var prefixTree = reverseResolutionData.get();
        var matches = prefixTree.getOrDefault(urlPrefix, null);
        if (CollectionUtils.isEmpty(matches)) return Optional.empty();

        for (var match : matches) {
            var luiPatternTester = Pattern.compile(match.luiPattern())
                    .asMatchPredicate();
            if (luiPatternTester.test(accession)) {
                return Optional.of(match);
            }
        }
        return Optional.of(matches.get(0)); // Send one just in case
    }



    private static String getUrlPatternPrefix(RrResourceData data) {
        var urlPattern = data.urlPattern();
        var idTemplateVarIdx = urlPattern.indexOf("{$id}");
        assert idTemplateVarIdx != -1 : String.format("%s is invalid for prefix %s", urlPattern, data.prefix());

        return urlPattern.substring(0, idTemplateVarIdx);
    }

    private PrefixTree generatePrefixTree() {
        log.debug("Generating prefix tree");
        var dataByUrlPrefix = dataProvider.get().stream().collect(
                Collectors.groupingBy(UrlPrefixReverseResolver::getUrlPatternPrefix)
        );
        return new PrefixTree(dataByUrlPrefix);
    }



    // This private class is just to avoid repeating the large PatriciaTrie<...> type in the code
    private static class PrefixTree extends PatriciaTrie<List<RrResourceData>> {
        public PrefixTree(Map<? extends String, ? extends List<RrResourceData>> map) {
            super(map);
        }
    }
}
