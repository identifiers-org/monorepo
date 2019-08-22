package org.identifiers.cloud.hq.ws.registry.api.models;

import org.identifiers.cloud.hq.ws.registry.models.InstitutionLifecycleManagementService;
import org.identifiers.cloud.hq.ws.registry.models.InstitutionLifecycleManagementServiceException;
import org.identifiers.cloud.hq.ws.registry.models.InstitutionLifecycleMangementOperationReport;
import org.identifiers.cloud.hq.ws.registry.models.helpers.AuthHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.api.models
 * Timestamp: 2019-08-22 11:17
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Component
public class InstitutionManagementApiModel {
    // Lifecycle manager service
    @Autowired
    private InstitutionLifecycleManagementService institutionLifecycleManagementServiceSimpleStrategy;

    // Auth Helper
    @Autowired
    private AuthHelper authHelper;

    public ResponseEntity<?> deleteById(long id) {
        try {
            String actor = authHelper.getCurrentUsername();
            InstitutionLifecycleMangementOperationReport report =
                    institutionLifecycleManagementServiceSimpleStrategy.deleteById(id, actor, "no additional information");
            if (report.isSuccess()) {
                return new ResponseEntity<>(report.getAdditionalInformation(), HttpStatus.OK);
            } else {
                if (!report.isInstitutionFound()) {
                    return new ResponseEntity<>(report.getErrorMessage(), HttpStatus.NOT_FOUND);
                }
                if (!report.getResources().isEmpty()) {
                    return new ResponseEntity<>(report.getErrorMessage(), HttpStatus.BAD_REQUEST);
                }
            }
        } catch (InstitutionLifecycleManagementServiceException e) {
            String errorMessage = String.format("Delete request for institution with ID '%d' COULD NOT BE COMPLETED due to the following error '%s'", id, e.getMessage());
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Something unknown happened", HttpStatus.I_AM_A_TEAPOT);
    }
}
