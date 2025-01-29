package org.identifiers.cloud.commons.compactidparsing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class NamespaceInfo {
   boolean namespaceEmbeddedInLui;
   String  prefix;
   boolean renderDeprecatedLanding;
   boolean deprecated;
   Date    deprecationDate;
}
