package org.identifiers.cloud.hq.ws.registry.data.models.curationwarnings;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Entity
public class CurationWarningDetail {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String label;

    @Column(nullable = false)
    private String value;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false)
    private CurationWarning curationWarning;
}
