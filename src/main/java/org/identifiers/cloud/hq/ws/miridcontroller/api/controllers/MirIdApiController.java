package org.identifiers.cloud.hq.ws.miridcontroller.api.controllers;

import org.identifiers.cloud.hq.ws.miridcontroller.api.models.MirIdApiModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Project: mirid-controller
 * Package: org.identifiers.cloud.hq.ws.miridcontroller.api.controllers
 * Timestamp: 2019-02-26 11:17
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@RestController
@RequestMapping("mirIdApi")
public class MirIdApiController {
    @Autowired
    private MirIdApiModel model;

    @GetMapping("/mintId")
    public @ResponseBody ResponseEntity<?> mintId() {
        return model.mintId();
    }

    @GetMapping("/keepAlive/{mirId}")
    public @ResponseBody ResponseEntity<?> keepAlive(@PathVariable String mirId) {
        return model.keepAlive(mirId);
    }

    @GetMapping("/keepAlive/{mirId}")
    public @ResponseBody ResponseEntity<?> loadId(@PathVariable String mirId) {
        return model.loadId(mirId);
    }

    @GetMapping("/keepAlive/{mirId}")
    public @ResponseBody ResponseEntity<?> returnId(@PathVariable String mirId) {
        return model.returnId(mirId);
    }
}
