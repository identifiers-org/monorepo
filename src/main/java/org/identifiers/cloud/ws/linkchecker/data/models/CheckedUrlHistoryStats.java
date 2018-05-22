package org.identifiers.cloud.ws.linkchecker.data.models;

import java.util.List;

/**
 * Project: link-checker
 * Package: org.identifiers.cloud.ws.linkchecker.data.models
 * Timestamp: 2018-05-22 13:48
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
public interface CheckedUrlHistoryStats {
    /**
     * Initialize the history with a given list of checked URLs events
     * @param checkedUrls list of checked URLs events
     */
    void init(List<CheckedUrl> checkedUrls);

    /**
     * Update the history with a given checked URL event
     * @param checkedUrl checked URL event
     */
    void update(CheckedUrl checkedUrl);

    /**
     * Get the percentage of events where the checked URL was considered to be up and running
     * @return up and running percentage
     */
    float getUpPercenetage();
}
