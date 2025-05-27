package org.identifiers.cloud.ws.resolver.services.reverseresolution;

import org.apache.commons.text.similarity.LevenshteinDistance;

import java.util.regex.Pattern;

public class Utils {
   static final LevenshteinDistance levenshteinDistance = LevenshteinDistance.getDefaultInstance();


   public static float getResolvedUrlSimilarity(RrResourceData match, String queryUrl, String queryAccession) {
      var expectedUrl = match.urlPattern().replace("{$id}", queryAccession);
      int similarity = Integer.max(0, levenshteinDistance.apply(expectedUrl, queryUrl));
      float largerUrlLength = Integer.max(expectedUrl.length(), queryUrl.length());
      float score = 100.0f - 100.0f * similarity / largerUrlLength;

      // Bound to a percentage since the levenshtein distance can be larger than the length of strings
      score = Float.max(0, score);
      score = Float.min(100, score);
      return score;
   }

   public static boolean doesAccessionMatch(RrResourceData match, String accession) {
      if (accession != null && !accession.isBlank()) {
         var tester = Pattern.compile(match.luiPattern()).asMatchPredicate();
         return tester.test(accession);
      } else {
         return false;
      }
   }


   public static String normalizeUrl(String url) {
      return url.replaceFirst("^https?://(www\\.)?", "")
                .replaceFirst("/$", "");
   }

   public static String getIdorgUrl(RrResourceData match, String accession) {
      return "http://identifiers.org/" + getIdorgCurie(match, accession);
   }

   public static String getIdorgCurie(RrResourceData match, String accession) {
      return match.namespaceEmbeddedInLui() ? accession :
              String.format("%s:%s", match.prefix(), accession);
   }
}
