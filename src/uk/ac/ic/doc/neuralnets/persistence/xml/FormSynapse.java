package uk.ac.ic.doc.neuralnets.persistence.xml;

import uk.ac.ic.doc.neuralnets.graph.neural.Neurone;
import uk.ac.ic.doc.neuralnets.graph.neural.Synapse;

/**
 * 
 * @author Stephen
 *
 */
public class FormSynapse extends PartialObject {

	// Storage for start and end neurones.
	private Neurone start = null;
	private Neurone end = null;

	/**
	 * FormSyanpse is a partial object, all partial object must have an id to
	 * which they can be referenced. This must be set at construction.
	 * 
	 * @param id
	 *            The old objects id.
	 */
	public FormSynapse(int id) {
		super(id);
	}

	/**
	 * Set the start neurone of the synapse.
	 * 
	 * @param start
	 *            Synapse to be the start.
	 */
	public void setStart(Neurone start) {
		this.start = start;
	}

	/**
	 * Set the end neurone of the syanpse.
	 * 
	 * @param end
	 *            Synapse to be the end.
	 */
	public void setEnd(Neurone end) {
		this.end = end;
	}

	/**
	 * Creates the synapse by invoking constructObject, to generate the create
	 * object type and set any persistable parameters, sets start and end
	 * neurones and returns completed synapse.
	 * 
	 * @return Synapse Created Synapse.
	 */
	public Synapse formSynapse() {
		Synapse syn = (Synapse) constructObject();

		syn.setStart(start);
		syn.setTo(end);

		return syn;
	}

}
