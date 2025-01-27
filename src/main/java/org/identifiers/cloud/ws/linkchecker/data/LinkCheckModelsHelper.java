package org.identifiers.cloud.ws.linkchecker.data;

import org.identifiers.cloud.ws.linkchecker.data.models.LinkCheckRequest;
import org.identifiers.cloud.ws.linkchecker.data.models.LinkCheckResult;
import org.identifiers.cloud.ws.linkchecker.strategies.LinkCheckerReport;

/**
 * Project: link-checker
 * Package: org.identifiers.cloud.ws.linkchecker.data
 * Timestamp: 2018-05-29 11:27
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This class implements diverse helpers, specially for model transformation, to keep model decoupling.
 */
public class LinkCheckModelsHelper {
    public static LinkCheckResult getResultFromReport(LinkCheckerReport report, LinkCheckRequest request) {
        return new LinkCheckResult()
                //.setTimestamp(report.getTimestamp())
                .setHttpStatus(report.getHttpStatus())
                .setUrl(report.getUrl())
                .setUrlAssessmentOk(report.isUrlAssessmentOk())
                .setProviderId(request.getProviderId())
                .setResourceId(request.getResourceId())
                .setRequestTimestamp(request.getTimestamp());
    }
}
