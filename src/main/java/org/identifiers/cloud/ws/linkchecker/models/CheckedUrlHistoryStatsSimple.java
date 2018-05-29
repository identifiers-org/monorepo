package org.identifiers.cloud.ws.linkchecker.models;

import org.identifiers.cloud.ws.linkchecker.data.models.LinkCheckResult;

import java.io.Serializable;
import java.util.List;

/**
 * Project: link-checker
 * Package: org.identifiers.cloud.ws.linkchecker.data.models
 * Timestamp: 2018-05-22 14:47
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
public class CheckedUrlHistoryStatsSimple implements CheckedUrlHistoryStats, Serializable {
    // Number of events where the checked URL was considered to be up
    private int nUpEvents = 0;
    // Number of events where the checked URL was considered to be down
    private int nDownEvents = 0;

    @Override
    public void init(List<LinkCheckResult> linkCheckResults) throws CheckedUrlHistoryStatsException {
        if ((nUpEvents + nDownEvents) == 0) {
            nUpEvents += linkCheckResults.parallelStream().filter(LinkCheckResult::isUrlAssessmentOk).count();
            nDownEvents = linkCheckResults.size() - nUpEvents;
        } else {
            throw new CheckedUrlHistoryStatsException("CANNOT INITIALIZE stats for alredy initialized stats");
        }
    }

    @Override
    public void update(LinkCheckResult linkCheckResult) {
        if (linkCheckResult.isUrlAssessmentOk()) {
            nUpEvents++;
        } else {
            nDownEvents++;
        }
    }

    @Override
    public double getUpPercenetage() {
        return (nUpEvents * 100.0) / (nDownEvents + nUpEvents);
    }
}
