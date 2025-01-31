package org.identifiers.cloud.ws.resolver.services;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.commons.messages.responses.ServiceResponse;
import org.identifiers.cloud.commons.messages.responses.resolver.ResponseResolvePayload;
import org.identifiers.cloud.commons.compactidparsing.ParsedCompactIdentifier;
import org.identifiers.cloud.commons.messages.models.ResolvedResource;
import org.matomo.java.tracking.MatomoRequest;
import org.matomo.java.tracking.MatomoRequest.MatomoRequestBuilder;
import org.matomo.java.tracking.MatomoTracker;
import org.matomo.java.tracking.parameters.AcceptLanguage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Slf4j
@Component
@RequiredArgsConstructor
public class MatomoTrackingService {
    @Value("${org.identifiers.matomo.authToken}")
    String authToken;

    @Value("${org.identifiers.matomo.enabled}")
    boolean isEnabled;

    @NonNull final ExecutorService matomoTrackerExecutor;
    @NonNull final MatomoTracker idorgMatomoTracker;

    record MatomoTrackingInfo(
        String url, String xforwardedFor, String remoteAddr,
        String ua, String lang, String refe,
        List<ResolvedResource> resolvedResources,
        ParsedCompactIdentifier parsedCid,
        boolean wasResolutionSuccessful
    ) {}

    private static MatomoTrackingInfo getMatomoInfoFrom(
            final HttpServletRequest request,
            final ServiceResponse<ResponseResolvePayload> result
    ) {
        return new MatomoTrackingInfo (
            request.getRequestURL().toString(),
            request.getHeader("X-FORWARDED-FOR"),
            request.getRemoteAddr(),
            request.getHeader("user-agent"),
            request.getHeader("Accept-Language"),
            request.getHeader("Referer"),
            result.getPayload().getResolvedResources(),
            result.getPayload().getParsedCompactIdentifier(),
            result.getHttpStatus().is2xxSuccessful());
    }

    public CompletableFuture<Void> handleCidResolution(final HttpServletRequest request, final ServiceResponse<ResponseResolvePayload> result) {
        if (!isEnabled) {
            log.debug("Matomo is disabled");
        } else if (request.getHeader("DNT") != null &&
                request.getHeader("DNT").contains("1")) {
            log.debug("Skipping matomo notification - DoNotTrack");
        } else {
            return doHandleCidResolution(getMatomoInfoFrom(request, result));
        }
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture<Void> doHandleCidResolution(MatomoTrackingInfo info) {
        MatomoRequestBuilder mreq = MatomoRequest.request();
        mreq.siteId(1);

        mreq.actionUrl(info.url);

        List<MatomoRequest> requests = new ArrayList<>(2);

        setHttpHeadersOnRequest(info, mreq);
        mreq.actionName("Resolution/Query");
        mreq.newVisit(true);
        requests.add(0, mreq.build());

        if (info.wasResolutionSuccessful) {
            setResolutionResultOnRequest(info, mreq);
            mreq.actionName("Resolution/Redirect");
            mreq.newVisit(false);
            requests.add(1, mreq.build());
        }

        return CompletableFuture.runAsync(
                () -> idorgMatomoTracker.sendBulkRequest(requests),
                matomoTrackerExecutor);
    }

    private void setHttpHeadersOnRequest(final MatomoTrackingInfo info, MatomoRequestBuilder mreq) {
        if (authToken != null && !authToken.isEmpty()) {
            mreq.authToken(authToken);
            String remoteIP = info.xforwardedFor;
            if (remoteIP == null || remoteIP.isEmpty()) {
                remoteIP = info.remoteAddr;
            } else { // Make sure to only take client Ip from forwarded for header and discard proxies
                int firstCommaIdx = remoteIP.indexOf(",");
                if (firstCommaIdx != -1) {
                    remoteIP = remoteIP.substring(0, firstCommaIdx).strip();
                }
            }
            mreq.visitorIp(remoteIP);
        } else {
            log.debug("No auth token => no ip collection");
        }
        mreq.headerUserAgent(info.ua);
        mreq.headerAcceptLanguage(AcceptLanguage.fromHeader(info.lang));
        mreq.referrerUrl(info.refe);
    }

    Comparator<ResolvedResource> resolvedResourceComparator =
            Comparator.comparingInt(o -> o.getRecommendation().getRecommendationIndex());

    private void setResolutionResultOnRequest(MatomoTrackingInfo info, MatomoRequestBuilder mreq) {
        ResolvedResource maxResolvedResource = info.resolvedResources.stream()
                .max(resolvedResourceComparator).orElse(null);
        if (maxResolvedResource != null) {
            mreq.outlinkUrl(maxResolvedResource.getCompactIdentifierResolvedUrl());
        }

        Map<Long, Object> customData = new HashMap<>();
        if (info.parsedCid != null) {
            customData.put(6L, info.parsedCid.getNamespace() == null ? "" : info.parsedCid.getNamespace());
            customData.put(7L, info.parsedCid.getProviderCode() == null ? "" : info.parsedCid.getProviderCode());
        }
        customData.put(8L, info.resolvedResources.size());
        if (maxResolvedResource != null) {
            customData.put(9L, maxResolvedResource.getInstitution().getName());
            customData.put(10L, maxResolvedResource.isOfficial());
        }
        mreq.dimensions(customData);
    }
}

