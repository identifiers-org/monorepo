package org.identifiers.cloud.hq.ws.registry.data.models.curationwarnings;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.identifiers.cloud.hq.ws.registry.data.models.Namespace;
import org.identifiers.cloud.hq.ws.registry.data.models.Resource;
import org.springframework.data.rest.core.annotation.RestResource;

@Entity @Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@DiscriminatorValue("resource")
public class ResourceCurationWarning extends CurationWarning {
    @ManyToOne
    @RestResource(path = "target", rel = "target")
    private Resource resource;
}
