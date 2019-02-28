package org.identifiers.cloud.hq.ws.miridcontroller.api.controllers;

import org.identifiers.cloud.hq.ws.miridcontroller.api.models.MirIdApiModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
    public @ResponseBody
    ResponseEntity<?> mintId() {
        return model.mintId();
    }
    // TODO
}
