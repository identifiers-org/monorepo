package org.identifiers.cloud.hq.ws.registry.api.models;

import org.apache.commons.lang3.StringUtils;
import org.identifiers.cloud.hq.ws.registry.api.data.models.Namespace;
import org.identifiers.cloud.hq.ws.registry.api.data.models.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ResolutionApiModelTest {
    @Test
    void testStripPrefixFromUrlsInplace() {
        String prefix = "mir";


        Namespace namespace = new Namespace();
        namespace.setPrefix(prefix);

        Resource resource1 = new Resource();
        resource1.setUrlPattern("https://provider1.com?q=mir:{$id}");
        Resource resource2 = new Resource();
        resource2.setUrlPattern("https://provider2.com?q={$id}");
        Resource resource3 = new Resource();
        resource3.setUrlPattern("https://provider3.com?q=MIR:{$id}");
        namespace.setResources(List.of(resource1, resource2, resource3));


        ResolutionApiModel.rewriteForEmbeddedPrefixes(namespace);


        for (Resource resource : namespace.getResources()) {
            String pattern = resource.getUrlPattern();
            var queryParams = UriComponentsBuilder
                    .fromHttpUrl(pattern).build().getQueryParams();
            String value = queryParams.getFirst("q");

            String errMessage = String.format("Prefix %s not stripped from pattern %s", prefix, pattern);
            assertFalse(StringUtils.containsIgnoreCase(value, prefix), errMessage);

            assertEquals("{$id}", value);
        }
    }
}