package org.identifiers.cloud.commons.compactidparsing;

public interface NamespaceInfoSource {
   NamespaceInfo findByPrefix(String prefix);
   boolean existsByPrefix(String prefix);
}
