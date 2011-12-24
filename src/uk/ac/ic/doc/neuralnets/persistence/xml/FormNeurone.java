package uk.ac.ic.doc.neuralnets.persistence.xml;

import uk.ac.ic.doc.neuralnets.graph.neural.Neurone;

/**
 * 
 * @author Stephen
 *
 */
public class FormNeurone extends PartialObject {

	/**
	 * FormNeurone is a partial object, all partial object must have an id to
	 * which they can be referenced. This must be set at construction.
	 * 
	 * @param id
	 *            The old objects id.
	 */
	public FormNeurone(int id) {
		super(id);
	}

	/**
	 * Creates the neurone by invoking the constructObject method and populating
	 * any parameters. Returns the completed object.
	 * 
	 * @return Neurone Complete neurone
	 */
	public Neurone getNeurone() {
		return (Neurone) constructObject();
	}

}
