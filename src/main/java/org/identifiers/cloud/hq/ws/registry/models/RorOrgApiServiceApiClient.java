package org.identifiers.cloud.hq.ws.registry.models;

import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.hq.ws.registry.models.rororg.Organization;
import org.springframework.stereotype.Component;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.models
 * Timestamp: 2019-08-15 01:24
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Component
@Slf4j
public class RorOrgApiServiceApiClient implements RorOrgApiService {
    // Re-try configuration
    public static final int WS_REQUEST_RETRY_MAX_ATTEMPTS = 12;
    public static final int WS_REQUEST_RETRY_BACK_OFF_PERIOD = 1500; // 1.5 seconds
    
    // TODO
    @Override
    public Organization getOrganizationDetails(String rorId) throws RorOrgApiServiceException {
        return null;
    }
}
