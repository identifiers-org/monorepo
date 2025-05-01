package org.identifiers.cloud.ws.sparql.data.sail;

import lombok.Getter;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.sail.Sail;
import org.eclipse.rdf4j.sail.SailConnection;
import org.eclipse.rdf4j.sail.SailException;
import org.eclipse.rdf4j.sail.StackableSail;
import org.eclipse.rdf4j.sail.helpers.AbstractSail;
import org.identifiers.cloud.ws.sparql.services.VirtualStatementResolver;

import java.util.Collection;

public class IdorgStore extends AbstractSail implements StackableSail {
	private ValueFactory vf;
	@Getter	private AbstractSail baseSail;
	private final Collection<VirtualStatementResolver> virtualStatementResolvers;

	public IdorgStore(Collection<VirtualStatementResolver> virtualStatementResolvers) {
	    super();
        this.virtualStatementResolvers = virtualStatementResolvers;
        this.vf = SimpleValueFactory.getInstance();
	}

	@Override
	public boolean isWritable() throws SailException {
		return false;
	}

	@Override
	public ValueFactory getValueFactory() {
		return vf;
	}

	@Override
	protected void shutDownInternal() throws SailException {

	}

	@Override
	protected SailConnection getConnectionInternal() throws SailException {
		return new IdorgConnection(vf, baseSail, virtualStatementResolvers);
	}

	public void setValueFactory(ValueFactory vf) {
		this.vf = vf;
	}

	@Override
	public void setBaseSail(Sail sail) {
		this.baseSail = (AbstractSail) sail;
	}
}
