package org.identifiers.cloud.ws.linkchecker.data.models;

import java.util.List;

/**
 * Project: link-checker
 * Package: org.identifiers.cloud.ws.linkchecker.data.models
 * Timestamp: 2018-05-22 14:47
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
public class CheckedUrlHistoryStatsSimple implements CheckedUrlHistoryStats {
    @Override
    public void init(List<CheckedUrl> checkedUrls) {

    }

    @Override
    public void update(CheckedUrl checkedUrl) {

    }

    @Override
    public float getUpPercenetage() {
        return 0;
    }
}
