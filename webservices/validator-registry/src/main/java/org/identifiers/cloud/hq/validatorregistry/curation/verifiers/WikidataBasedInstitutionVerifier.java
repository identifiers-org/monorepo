package org.identifiers.cloud.hq.validatorregistry.curation.verifiers;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.similarity.JaroWinklerSimilarity;
import org.identifiers.cloud.commons.messages.models.Institution;
import org.identifiers.cloud.commons.messages.models.CurationWarningNotification;
import org.identifiers.cloud.hq.validatorregistry.helpers.StatusHelper;
import org.identifiers.cloud.hq.validatorregistry.helpers.TargetEntityHelper;
import org.identifiers.cloud.hq.validatorregistry.helpers.WikiDataHelper;
import org.identifiers.cloud.hq.validatorregistry.helpers.WikiDataHelper.WikiDataOrganizationDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * This class verifies institutions in the registry with their page on wikidata.
 */
@Slf4j
@Service
@ConditionalOnProperty(value = "org.identifiers.cloud.verifiers.wikidata.enabled")
public class WikidataBasedInstitutionVerifier extends RegistryEntityVerifier<Institution> {
    private static final String NOTIFICATION_TYPE = "wikidata-institution-diff";
    private static final JaroWinklerSimilarity similarityMeasurer = new JaroWinklerSimilarity();

    @Value("${org.identifiers.cloud.verifiers.wikidata.max-distance-for-match}")
    private double maxDistanceForMatch;

    private final WikiDataHelper wikiDataHelper;

    public WikidataBasedInstitutionVerifier(StatusHelper statusHelper,
                                            WikiDataHelper wikiDataHelper) {
        super(statusHelper);
        this.wikiDataHelper = wikiDataHelper;
    }


    @Override
    public Optional<CurationWarningNotification> doValidate(Institution institution) {
        try {
            // This is a limiter due to wikidata's query limits
            // https://www.mediawiki.org/wiki/Wikidata_Query_Service/User_Manual#Query_limits
            Thread.currentThread().wait(400,0);
        } catch (InterruptedException e) {
            log.debug("Wikidata rate limiter interrupted");
        }
        log.debug("Checking wikidata for institution {}", institution.getName());

        if (StringUtils.isNotBlank(institution.getRorId())) {
            var details = wikiDataHelper.getOrganizationDetailsFromRorId(institution.getRorId());
            if (details.isPresent()) {
                log.debug("Found wikidata match for {} based on ROR ID", institution.getName());
                return validateFromMatch(institution, details.get());
            }
        }
        return bestEffortValidation(institution);
    }

    /**
     * This compares an institution and its match on wikidata to produce a
     * @param institution institution to compare to data in wikidata
     * @param wikidataEntry match for institution in wikidata
     * @return notification on the differences between institution and wikidata entry
     */
    Optional<CurationWarningNotification> validateFromMatch(Institution institution,
                                                            WikiDataOrganizationDetails wikidataEntry) {
        HashMap<String, String> differentAttributes = new HashMap<>();

        if (!equalsNormalizedUrls(institution.getHomeUrl(), wikidataEntry.getHomeUrl())) {
            // This comparison ignores protocol and trailing slash
            differentAttributes.put("expected-home-url", wikidataEntry.getHomeUrl());
        }
        if (!StringUtils.contains(institution.getName(), wikidataEntry.getName())) {
            // Contains is used here because the name in the registry normally
            // contains other information, such as city and country
            differentAttributes.put("expected-name", wikidataEntry.getName());
        }
        if (!StringUtils.endsWith(institution.getRorId(), wikidataEntry.getRorId())) {
            // "endsWith" is used here because wikidata doesn't store the full
            // ror URI, only the alphanumerical component at the end
            differentAttributes.put("expected-ror-id", "https://ror.org/"+wikidataEntry.getRorId());
        }
        if (!StringUtils.equals(institution.getLocation().getCountryName(), wikidataEntry.getLocName()) ||
            !StringUtils.equals(institution.getLocation().getCountryCode(), wikidataEntry.getLocCode())) {
            differentAttributes.put("expected-country", wikidataEntry.getLocName());
        }

        if(differentAttributes.isEmpty()) {
            return Optional.empty();
        } else {
            String globalId = String.format("%s:%s", NOTIFICATION_TYPE, institution.getId());
            String targetType = TargetEntityHelper.getEntityTypeOf(institution);
            differentAttributes.put("wikidata-url", wikidataEntry.getQid());
            var notification = CurationWarningNotification.builder()
                    .globalId(globalId)
                    .targetType(targetType)
                    .targetId(institution.getId())
                    .type(NOTIFICATION_TYPE)
                    .details(differentAttributes)
                    .build();
            return Optional.of(notification);
        }
    }

    /**
     * Tries different ways to find a match for the institution on wikidata and perform curation.
     * Unless it finds a good match for the institution, it won't perform any verification to avoid false positives.
     * <ol>
     *     <li>It will first use text search to find possible matches</li>
     *     <li>Then try to match based on URL</li>
     *     <li>If not found, text similarity is used to find closes match</li>
     * </ol>
     * @param institution to try to match and produce notification
     * @return Possible notification for that institution
     */
    Optional<CurationWarningNotification> bestEffortValidation(Institution institution) {
        // Filter institution name string for organization name as it may contain country and other names
        var orgNames = wikiDataHelper.findOrganizationNamesUsingNlp(institution.getName());

        var possibleWikidataIdMatches = Stream.of(orgNames)
                .map(wikiDataHelper::searchForWikiDataIds)
                .flatMap(Collection::stream)
                .toList();
        var possibleWikidataMatches = wikiDataHelper.getOrganizationDetailsFromQids(possibleWikidataIdMatches)
                .stream().filter(WikidataBasedInstitutionVerifier::isUsefulWikidataInfo).toList();

        if (possibleWikidataMatches.isEmpty()) {
            return Optional.empty();
        }

        String currentHomeUrl = institution.getHomeUrl();
        var oneWithSameHomeUrl = possibleWikidataMatches.stream()
                .filter(d -> currentHomeUrl.equals(d.getHomeUrl()))
                .findFirst();
        if (oneWithSameHomeUrl.isPresent()) {
            log.debug("Found wikidata match for {} based on URL", institution.getName());
            return validateFromMatch(institution, oneWithSameHomeUrl.get());
        }

        var distances = possibleWikidataMatches.stream().collect(
                Collectors.toMap(Function.identity(),m -> measureDistance(institution, m))
        );
        var closestMatch = possibleWikidataMatches.stream().min(Comparator.comparingDouble(distances::get));

        if (closestMatch.isPresent()) {
            var match = closestMatch.get();
            if (distances.get(match) <= maxDistanceForMatch) {
                log.debug("Found wikidata match for {} based on similarity", institution.getName());
                return validateFromMatch(institution, match);
            }
        }

        return Optional.empty();
    }

    double measureDistance(Institution institution, WikiDataOrganizationDetails wikidataEntry) {
        //This assumes that similarityMeasurer returns a value from 0 to 1 in proportion to how similar the params are
        //  But distance is a concept that behaves in the opposite way, so the complement is returned.

        double acc = 0.0;

        acc += StringUtils.isAnyBlank(institution.getName(), wikidataEntry.getName()) ? 0.0 :
                similarityMeasurer.apply(institution.getName(), wikidataEntry.getName());
        acc += StringUtils.isAnyBlank(institution.getHomeUrl(), wikidataEntry.getHomeUrl()) ? 0.0 :
                similarityMeasurer.apply(institution.getHomeUrl(), wikidataEntry.getHomeUrl());
        acc += StringUtils.isAnyBlank(institution.getLocation().getCountryCode(), wikidataEntry.getLocCode()) ? 0.0 :
                similarityMeasurer.apply(institution.getLocation().getCountryCode(), wikidataEntry.getLocCode());
        acc += StringUtils.isAnyBlank(institution.getLocation().getCountryName(), wikidataEntry.getLocName()) ? 0.0 :
                similarityMeasurer.apply(institution.getLocation().getCountryName(), wikidataEntry.getLocName());

        return 4.0-acc;
    }

    private boolean equalsNormalizedUrls(String registryUrl, String wikidataUrl) {
        return StringUtils.equals(
                normalizeUrl(registryUrl),
                normalizeUrl(wikidataUrl)
        );
    }

    /**
     * Removes protocol, initial www component and trailing slash character
     * @param url value to normalize
     * @return normalized URL
     */
    private static String normalizeUrl(String url) {
        if (url == null) return null;

        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }
        if (url.startsWith("http://")) {
            url = url.replaceFirst("^http://", "");
        } else if (url.startsWith("https://")) {
            url = url.replaceFirst("^https://", "");
        }
        if (url.startsWith("www.")) {
            url = url.replaceFirst("www.", "");
        }
        return url;
    }

    private static boolean isUsefulWikidataInfo(WikiDataOrganizationDetails details) {
        return StringUtils.isNoneBlank(details.getHomeUrl(), details.getName(), details.getLocCode());
    }
}
