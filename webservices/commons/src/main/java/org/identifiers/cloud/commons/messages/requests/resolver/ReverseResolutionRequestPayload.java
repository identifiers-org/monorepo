package org.identifiers.cloud.commons.messages.requests.resolver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class ReverseResolutionRequestPayload {
    private String url;
    private String accession = null;
    private boolean forceUrls = false;
}
