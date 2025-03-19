package org.identifiers.cloud.hq.ws.registry.data.models.curationwarnings;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.identifiers.cloud.hq.ws.registry.data.services.NewCurationWarningEventListener;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@EntityListeners(value = {AuditingEntityListener.class, NewCurationWarningEventListener.class})
public class CurationWarningEvent {
    @Id
    @GeneratedValue
    private Long id;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    protected Date created;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;

    public enum Type {
        CREATED,
        SOLVED,
        REOPENED
    }

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private CurationWarning curationWarning;
}
