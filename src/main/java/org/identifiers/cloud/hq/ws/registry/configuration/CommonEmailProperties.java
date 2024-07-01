package org.identifiers.cloud.hq.ws.registry.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class CommonEmailProperties {
    @Value("${org.identifiers.cloud.hq.ws.registry.notifiers.from}")
    private String emailSender;
    @Value("${org.identifiers.cloud.hq.ws.registry.notifiers.replyto}")
    private String emailReplyTo;
    @Value("${org.identifiers.cloud.hq.ws.registry.notifiers.email.support}")
    private String emailAddressSupport;

    // Placeholders
    @Value("${org.identifiers.cloud.hq.ws.registry.notifiers.placeholder.prefix}")
    private String placeholderPrefix;
    @Value("${org.identifiers.cloud.hq.ws.registry.notifiers.placeholder.requestername}")
    private String placeholderRequesterName;
    @Value("${org.identifiers.cloud.hq.ws.registry.notifiers.placeholder.requesteremail}")
    private String placeholderRequesterEmail;
    @Value("${org.identifiers.cloud.hq.ws.registry.notifiers.placeholder.prefixname}")
    private String placeholderPrefixName;
    @Value("${org.identifiers.cloud.hq.ws.registry.notifiers.placeholder.prefixdescription}")
    private String placeholderPrefixDescription;
    @Value("${org.identifiers.cloud.hq.ws.registry.notifiers.placeholder.sessionid}")
    private String placeholderSessionId;
    @Value("${org.identifiers.cloud.hq.ws.registry.notifiers.placeholder.donotuse}")
    private String placeholderDoNotUse;
    @Value("${org.identifiers.cloud.hq.ws.registry.notifiers.placeholder.resource.name}")
    private String placeholderResourceName;
    @Value("${org.identifiers.cloud.hq.ws.registry.notifiers.placeholder.resource.description}")
    private String placeholderResourceDescription;
    @Value("${org.identifiers.cloud.hq.ws.registry.notifiers.placeholder.email.support}")
    private String placeholderEmailSupport;
}
