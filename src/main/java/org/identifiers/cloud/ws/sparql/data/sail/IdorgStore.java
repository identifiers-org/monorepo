package org.identifiers.cloud.ws.sparql.data.sail;

import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.sail.SailConnection;
import org.eclipse.rdf4j.sail.SailException;
import org.eclipse.rdf4j.sail.helpers.AbstractSail;
import org.identifiers.cloud.ws.sparql.services.SameAsResolver;

public class IdorgStore extends AbstractSail {
	private ValueFactory vf;

	private final SameAsResolver sameAsResolver;

	public IdorgStore(SameAsResolver sameAsResolver) {
	    super();
	    this.sameAsResolver = sameAsResolver;
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
		return new IdorgConnection(getValueFactory(), sameAsResolver, this);
	}

	public void setValueFactory(ValueFactory vf) {
		this.vf = vf;
	}

}
