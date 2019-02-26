package org.identifiers.cloud.hq.ws.miridcontroller.api.controllers;

import org.identifiers.cloud.hq.ws.miridcontroller.api.models.MirIdApiModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
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
    
    // TODO
}
