package org.identifiers.cloud.ws.sparql.data.sail;

import java.util.*;
import java.util.stream.Stream;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.common.iteration.CloseableIteratorIteration;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.query.algebra.evaluation.TripleSource;
import org.identifiers.cloud.ws.sparql.services.VirtualStatementResolver;

@Slf4j
@RequiredArgsConstructor
public class IdorgTripleSource implements TripleSource {
	private final ValueFactory valueFactory;
	private final Collection<VirtualStatementResolver> virtualStatementResolvers;
	private final TripleSource other;

	@Override
	public CloseableIteration<Statement> getStatements(Resource subj, IRI pred, Value obj,
													   Resource... contexts) throws QueryEvaluationException {
		log.debug("getStatements for {} {} {}", subj, pred, obj);

		var otherStatements = other.getStatements(subj, pred, obj, contexts).stream();
		var generatedStatements = virtualStatementResolvers.stream()
				.filter(r -> r.mayGenerateStatements(subj, pred, obj, contexts))
				.flatMap(r -> r.getStatements(subj, obj, contexts).stream());

		return new CloseableIteratorIteration<>(
				Stream.concat(generatedStatements, otherStatements).iterator()
		);
	}

	@Override
	public ValueFactory getValueFactory() {
		return valueFactory;
	}
}
