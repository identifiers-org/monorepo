package org.identifiers.cloud.ws.resolver.services.reverseresolution.resolvers;

import com.google.common.base.Suppliers;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.identifiers.cloud.ws.resolver.services.reverseresolution.RrResourceData;
import org.identifiers.cloud.ws.resolver.services.reverseresolution.RrDataSupplier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collection;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Service
public class SimilarityReverseResolver {
   private final Supplier<Collection<RrResourceData>> reverseResolutionData;
   private final int maxResults;
   public SimilarityReverseResolver(RrDataSupplier dataSupplier,
                                    @Value("${org.identifiers.cloud.ws.resolver.reverse-resolution.similarity.list-cache-duration}")
                                    Duration cacheDuration,
                                    @Value("${org.identifiers.cloud.ws.resolver.reverse-resolution.similarity.max-results}")
                                    int maxResults) {
      assert maxResults > 0 : "Max results for similarity RR must be greater than zero";

      long cacheDurationInSeconds = cacheDuration.getSeconds();
      assert cacheDurationInSeconds > 0 : "Cache duration for similarity RR must be greater than zero";

      this.reverseResolutionData = Suppliers.memoizeWithExpiration(
              dataSupplier::get, cacheDurationInSeconds, TimeUnit.SECONDS
      );
      this.maxResults = maxResults;
   }

   public Stream<RrResourceData> resolve(String url, String accession) {
      Function<RrResourceData, Integer> distanceToQuery =
              s -> LevenshteinDistance.getDefaultInstance()
                      .apply(url, s.urlPattern().replace("{$id}", accession));

      return reverseResolutionData.get().stream()
              .sorted(Comparator.comparing(distanceToQuery))
              .limit(maxResults);
   }
}
