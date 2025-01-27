package org.identifiers.cloud.ws.metadata.api.controllers;

import org.identifiers.cloud.ws.metadata.api.responses.ServiceResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Controller
@RequestMapping("/")
public class RootController {
    final ServiceResponse<?> defaultRootResponse = ServiceResponse.ofError(
            BAD_REQUEST,
            "Please find the documentation on how to use this service at https://docs.identifiers.org/pages/metadata_service"
    );

    @ResponseStatus(BAD_REQUEST)
    ServiceResponse<?> rootMessage() {
        return defaultRootResponse;
    }
}
