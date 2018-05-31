package org.identifiers.cloud.ws.linkchecker.models;

import org.identifiers.cloud.ws.linkchecker.data.models.LinkCheckResult;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Project: link-checker
 * Package: org.identifiers.cloud.ws.linkchecker.data.models
 * Timestamp: 2018-05-22 15:48
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * This is a local cache for the history tracking information on a link. Base class for the different tracked entities.
 */
public abstract class HistoryTracker implements Serializable {
    // Home URL for this provider within the context of a namespace or prefix
    private String url;
    // When the tracking was queued / added to the link checker (UTC)
    private Timestamp created;

    public String getUrl() {
        return url;
    }

    public HistoryTracker setUrl(String url) {
        this.url = url;
        return this;
    }

    public Timestamp getCreated() {
        return created;
    }

    public HistoryTracker setCreated(Timestamp created) {
        this.created = created;
        return this;
    }

    public List<CheckedUrlHistoryStats> getHistoryStats() {
        return Arrays.stream(HistoryStats.values()).map(HistoryStats::getHistoryStats).collect(Collectors.toList());
    }

    public void addLinkCheckResult(LinkCheckResult linkCheckResult) {
        // Update the history stats
        Arrays.stream(HistoryStats.values())
                .forEach(historyStats -> historyStats.getHistoryStats().update(linkCheckResult));
    }

    public void initHistoryStats(List<LinkCheckResult> linkCheckResults) {
        Arrays.stream(HistoryStats.values())
                .forEach(historyStats -> historyStats.getHistoryStats().init(linkCheckResults));
    }

    public enum HistoryStats implements Serializable {
        SIMPLE(new CheckedUrlHistoryStatsSimple(), "Simple UP/DOWN history tracking", "simple");

        private CheckedUrlHistoryStats historyStats;
        private String key;
        private String description;

        HistoryStats(CheckedUrlHistoryStats historyStats, String description, String key) {
            this.historyStats = historyStats;
            this.description = description;
        }

        public CheckedUrlHistoryStats getHistoryStats() {
            return historyStats;
        }

        public HistoryStats setHistoryStats(CheckedUrlHistoryStats historyStats) {
            this.historyStats = historyStats;
            return this;
        }

        public String getDescription() {
            return description;
        }

        public HistoryStats setDescription(String description) {
            this.description = description;
            return this;
        }
    }
}
