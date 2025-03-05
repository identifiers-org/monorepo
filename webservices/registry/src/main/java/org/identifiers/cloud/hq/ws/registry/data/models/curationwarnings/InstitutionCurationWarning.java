package org.identifiers.cloud.hq.ws.registry.data.models.curationwarnings;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.identifiers.cloud.hq.ws.registry.data.models.Institution;
import org.springframework.data.rest.core.annotation.RestResource;

@Entity @Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@DiscriminatorValue("institution")
public class InstitutionCurationWarning extends CurationWarning {
    @ManyToOne
    @RestResource(path = "target", rel = "target")
    private Institution institution;
}
