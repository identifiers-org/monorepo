package org.identifiers.satellite.frontend.satellitewebspa.api.controllers;

//@Controller
@Deprecated
public class ErrorController {

//    @RequestMapping(value = "/error")
    public String handleError() {
        return "index.html";
    }
}
