package org.identifiers.cloud.ws.resolver.services.reverseresolution;

import org.identifiers.cloud.ws.resolver.data.models.Namespace;
import org.identifiers.cloud.ws.resolver.data.models.Resource;

import java.util.List;
import java.util.Objects;

public record RrResourceData(
      String prefix,
      String urlPattern,
      boolean namespaceEmbeddedInLui,
      String luiPattern
) {
   public static List<RrResourceData> from(Namespace namespace) {
      return namespace.isDeprecated() ? List.of() :
            namespace.getResources().stream()
                     .map(r -> from(namespace, r))
                     .filter(Objects::nonNull)
                     .toList();
   }
   private static RrResourceData from(Namespace namespace, Resource resource) {
      return resource.isDeprecated() ? null : new RrResourceData(
         namespace.getPrefix(),
         Utils.normalizeUrl(resource.getUrlPattern()),
         namespace.isNamespaceEmbeddedInLui(),
         namespace.getPattern()
      );
   }
}
