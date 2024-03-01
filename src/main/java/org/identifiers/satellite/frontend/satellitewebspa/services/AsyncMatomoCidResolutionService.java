package org.identifiers.satellite.frontend.satellitewebspa.services;

import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.libapi.models.resolver.ResponseResolvePayload;
import org.identifiers.cloud.libapi.models.ServiceResponse;
import org.identifiers.cloud.libapi.models.resolver.ParsedCompactIdentifier;
import org.identifiers.cloud.libapi.models.resolver.ResolvedResource;
import org.matomo.java.tracking.MatomoRequest;
import org.matomo.java.tracking.MatomoRequest.MatomoRequestBuilder;
import org.matomo.java.tracking.MatomoTracker;
import org.matomo.java.tracking.parameters.AcceptLanguage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import java.util.*;

@Slf4j
@Component
public class AsyncMatomoCidResolutionService {
    @Value("${org.identifiers.matomo.authToken}")
    String authToken;

    @Value("${org.identifiers.matomo.enabled}")
    public boolean isEnabled;

    final MatomoTracker idorgMatomoTracker;
    public AsyncMatomoCidResolutionService(MatomoTracker idorgMatomoTracker) {
        this.idorgMatomoTracker = idorgMatomoTracker;
    }

    private record MatomoRequestImportantInfo (
        String url, String xforwarded_for, String remote_addr,
        String ua, String lang, String refe,
        List<ResolvedResource> resolvedResources,
        ParsedCompactIdentifier parsed_cid,
        boolean wasResolutionSuccessfull
    ) {}

    static MatomoRequestImportantInfo matomoInfoOf(final HttpServletRequest request, final ServiceResponse<ResponseResolvePayload> result) {
        ResponseResolvePayload resolveResult = result.getPayload();
        return new MatomoRequestImportantInfo(
                request.getRequestURL().toString().replaceFirst("/resolutionApi/", ""),
                request.getHeader("X-FORWARDED-FOR"),
                request.getRemoteAddr(),
                request.getHeader("user-agent"),
                request.getHeader("Accept-Language"),
                request.getHeader("Referer"),
                resolveResult.getResolvedResources(),
                resolveResult.getParsedCompactIdentifier(),
                result.getHttpStatus().is2xxSuccessful()
        );
    }

    public void asyncHandleCidResolution(final HttpServletRequest request, final ServiceResponse<ResponseResolvePayload> result) {
        if (!isEnabled) {
            log.debug("Matomo is disabled");
        } else if (request.getHeader("DNT") != null &&
                request.getHeader("DNT").contains("1")) {
            log.debug("Skipping matomo notification - DoNotTrack");
        } else {
            doHandleCidResolution(matomoInfoOf(request, result));
        }
    }

    void doHandleCidResolution(MatomoRequestImportantInfo info) {
        log.debug("Info: {}", info);
        MatomoRequestBuilder mreq = MatomoRequest.request();
        mreq.siteId(1);

        mreq.actionUrl(info.url);
        mreq.trackBotRequests(true);
        List<MatomoRequest> requests = new ArrayList<>(2);

        setHttpHeadersOnRequest(info, mreq);
        mreq.actionName("Resolution/Query");
        mreq.newVisit(true);
        requests.add(0, mreq.build());

        if (info.wasResolutionSuccessfull) {
            setResolutionResultOnRequest(info, mreq);
            mreq.actionName("Resolution/Redirect");
            mreq.newVisit(false);
            requests.add(1, mreq.build());
        }

        idorgMatomoTracker.sendBulkRequestAsync(requests);
    }

    private void setHttpHeadersOnRequest(final MatomoRequestImportantInfo info, MatomoRequestBuilder mreq) {
        if (StringUtils.isNotBlank(authToken) && authToken.length() == 32) {
            mreq.authToken(authToken);
            String remoteIP = info.xforwarded_for;
            if (remoteIP == null || remoteIP.isEmpty()) {
                remoteIP = info.remote_addr;
            } else { // Make sure to only take client Ip from forwarded for header and discard proxies
                int firstCommaIdx = remoteIP.indexOf(",");
                if (firstCommaIdx != -1) {
                    remoteIP = remoteIP.substring(0, firstCommaIdx);
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

    private void setResolutionResultOnRequest(MatomoRequestImportantInfo info, MatomoRequestBuilder mreq) {
        ResolvedResource maxResolvedResource = info.resolvedResources.stream()
                .max(resolvedResourceComparator).orElse(null);
        if (maxResolvedResource != null) {
            mreq.outlinkUrl(maxResolvedResource.getCompactIdentifierResolvedUrl());
        }

        Map<Long, Object> customData = new HashMap<>();
        if (info.parsed_cid != null) {
            customData.put(6L, info.parsed_cid.getNamespace() == null ? "" : info.parsed_cid.getNamespace());
            customData.put(7L, info.parsed_cid.getProviderCode() == null ? "" : info.parsed_cid.getProviderCode());
        }
        customData.put(8L, info.resolvedResources.size());
        if (maxResolvedResource != null) {
            customData.put(9L, maxResolvedResource.getInstitution().getName());
            customData.put(10L, maxResolvedResource.isOfficial());
        }
        mreq.dimensions(customData);
    }
}

