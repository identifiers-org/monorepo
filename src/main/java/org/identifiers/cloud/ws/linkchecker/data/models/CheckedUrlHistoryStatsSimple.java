package org.identifiers.cloud.ws.linkchecker.data.models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger logger = LoggerFactory.getLogger(CheckedUrlHistoryStatsSimple.class);
    // Number of events where the checked URL was considered to be up
    private int nUpEvents = 0;
    // Number of events where the checked URL was considered to be down
    private int nDownEvents = 0;

    @Override
    public void init(List<LinkCheckResult> linkCheckResults) throws CheckedUrlHistoryStatsException {
        if ((nUpEvents + nDownEvents) == 0) {
            nUpEvents += linkCheckResults.parallelStream().filter(linkCheckResult -> linkCheckResult.getHttpStatus() == 200).count();
            nDownEvents = linkCheckResults.size() - nUpEvents;
        } else {
            throw new CheckedUrlHistoryStatsException("CANNOT INITIALIZE stats for alredy initialized stats");
        }
    }

    @Override
    public void update(LinkCheckResult linkCheckResult) {
        if (linkCheckResult.getHttpStatus() == 200) {
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
