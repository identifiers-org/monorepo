package org.identifiers.cloud.ws.sparql.services;

import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;

import java.util.Optional;

public abstract class VirtualStatementResolver {
    protected final ValueFactory valueFactory;
    private final IRI active;
    private final IRI obsolete;

    protected VirtualStatementResolver() {
        this.valueFactory = SimpleValueFactory.getInstance();
        active = valueFactory.createIRI("id:active");
        obsolete = valueFactory.createIRI("id:obsolete");
    }

    /**
     * Check if statements should be generated for triple given the context
     * @param subj first element of rdf triple query, may be null
     * @param pred second element of rdf triple query
     * @param obj third element of rdf triple query, may be null
     * @param contexts graph context, usually the URI for the named graph being queried
     * @return true if statements may be generated for this triple query
     */
    public abstract boolean mayGenerateStatements(Resource subj, IRI pred, Value obj, Resource... contexts);

    public CloseableIteration<Statement> getStatements(Resource subj, Value obj, Resource... contexts) {
        if (obj instanceof Resource resource) {
            return doGetStatements(subj, resource, contexts);
        } else {
            return doGetStatements(subj, contexts);
        }
    }

    /**
     * Generate statements for query in formats
     * <ul>
     *     <li>[Subject] [Predicate] [Rdf Literal]</li>
     *     <li>[Subject] [Predicate] [?variable]</li>
     * </ul>
     * @param subj first element of rdf triple query, not null
     * @param contexts graph context, usually the URI for the named graph being queried
     * @return iteratable
     */
    abstract CloseableIteration<Statement> doGetStatements(final Resource subj, Resource... contexts);

    /**
     * Generate statements for query in formats
     * <ul>
     *     <li>[Subject] [Predicate] [Rdf Resource]</li>
     *     <li>[?variable] [Predicate] [Rdf Resource]</li>
     * </ul>
     * @param subj first element of rdf triple query, not null
     * @param contexts graph context, usually the URI for the named graph being queried
     * @return iteratable
     */
    abstract CloseableIteration<Statement> doGetStatements(final Resource subj, final Resource obj,
                                                           Resource... contexts);

    protected final Optional<Boolean> getActiveFlag(Resource... contexts) {
        for (Resource context : contexts) {
            if (active.equals(context)) {
                return Optional.of(true);
            }
            if (obsolete.equals(context)) {
                return Optional.of(false);
            }
        }
        return Optional.empty();
    }
}
