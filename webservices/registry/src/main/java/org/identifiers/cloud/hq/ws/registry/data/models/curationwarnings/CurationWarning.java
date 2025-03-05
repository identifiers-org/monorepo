package org.identifiers.cloud.hq.ws.registry.data.models.curationwarnings;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Getter @Setter
@Accessors(chain = true)
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="target_type", discriminatorType = DiscriminatorType.STRING, columnDefinition = "not null")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
public class CurationWarning {
    @Id
    @GeneratedValue
    @Column(nullable = false, updatable = false)
    protected long id;

    @Column(nullable = false, unique = true, updatable = false)
    protected String globalId;

    @Column(nullable = false, updatable = false)
    protected String type;

    // The 'open' attribute is dependent on the last event registered and is updated by an entity listener
    //  I don't like this solution, but I couldn't find a way to write a jpa query to list open warnings
    //  and I couldn't use a native query because JpaRepository pagination doesn't work with them.
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(nullable = false)
    protected boolean open;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    protected Date created;

    @LastModifiedDate
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    protected Date modified;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(nullable = false)
    protected Date lastNotification;

    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "curationWarning", orphanRemoval = true)
    protected Set<CurationWarningDetail> details;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "curationWarning", orphanRemoval = true)
    @OrderBy("created DESC")
    protected List<CurationWarningEvent> events;

    public CurationWarningEvent getLatestEvent() {
        // This currently relies on the @OrderBy on the events association
        var it = events.iterator();
        return it.hasNext() ? it.next() : null;
    }

    public Map<String, String> getMoreDetails() {
        // This getter makes the ObjectMapper add this to the Spring Data Rest response
        //  It is a bit better on JSON than the Set of curation warning details
        if (details == null) {
            return Map.of();
        } else {
            return details.stream().collect(Collectors.toMap(
                    CurationWarningDetail::getLabel,
                    CurationWarningDetail::getValue
            ));
        }
    }
}
