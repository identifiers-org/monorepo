package org.identifiers.cloud.hq.ws.registry.data.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import javax.persistence.Entity;
import java.math.BigInteger;

/**
 * Project: registry
 * Package: org.identifiers.cloud.hq.ws.registry.data.models
 * Timestamp: 2018-10-11 12:08
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 *
 * Data model for namespace synonyms
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
@Entity
// TODO - refactoring to relational
public class NamespaceSynonym {
    @Id private String synonym;
    private BigInteger namespaceFk;
    @Transient private Namespace namespace;

    public String getSynonym() {
        return synonym;
    }

    public NamespaceSynonym setSynonym(String synonym) {
        this.synonym = synonym;
        return this;
    }

    public BigInteger getNamespaceFk() {
        return namespaceFk;
    }

    public NamespaceSynonym setNamespaceFk(BigInteger namespaceFk) {
        this.namespaceFk = namespaceFk;
        return this;
    }

    public Namespace getNamespace() {
        return namespace;
    }

    public NamespaceSynonym setNamespace(Namespace namespace) {
        this.namespace = namespace;
        return this;
    }
}
