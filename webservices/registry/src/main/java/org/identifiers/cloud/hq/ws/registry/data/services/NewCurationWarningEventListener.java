package org.identifiers.cloud.hq.ws.registry.data.services;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.hq.ws.registry.data.models.curationwarnings.CurationWarningEvent;

@Slf4j
public class NewCurationWarningEventListener {
    @PrePersist
    private void onCreate(CurationWarningEvent event) {
        boolean isEventForOpenWarning = event.getType() != CurationWarningEvent.Type.SOLVED;
        event.getCurationWarning().setOpen(isEventForOpenWarning);
    }

    @PreRemove
    private void onDestroy(CurationWarningEvent event) {
        var curationWarning = event.getCurationWarning();
        var newLatest = curationWarning.getLatestEvent();
        boolean isPreviousEventForOpenWarning = newLatest.getType() == null ||
                event.getType() != CurationWarningEvent.Type.SOLVED;
        curationWarning.setOpen(isPreviousEventForOpenWarning);
    }
}
