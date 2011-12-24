package uk.ac.ic.doc.neuralnets.persistence.xml;

import uk.ac.ic.doc.neuralnets.graph.neural.NetworkBridge;
import uk.ac.ic.doc.neuralnets.graph.neural.NeuralNetwork;
import uk.ac.ic.doc.neuralnets.graph.neural.Synapse;

/**
 * 
 * @author Stephen
 *
 */
public class FormProjections extends PartialObject {

	// Reference to the start and end networks.
	private NeuralNetwork start = null;
	private NeuralNetwork end = null;

	// Reference to network bridge if required.
	private NetworkBridge nb = null;

	/**
	 * FormProjections is a partial object, all partial object must have an id
	 * to which they can be referenced. This must be set at construction.
	 * 
	 * @param id
	 *            The old objects id.
	 */
	public FormProjections(int id) {
		super(id);
	}

	/**
	 * Sets the start network for the projections.
	 * 
	 * @param start
	 *            The start neural network.
	 */
	public void setStart(NeuralNetwork start) {
		this.start = start;
	}

	/**
	 * Sets the end network for the projections.
	 * 
	 * @param end
	 *            The end neural network.
	 */
	public void setEnd(NeuralNetwork end) {
		this.end = end;
	}

	/**
	 * Adds a complete Synapse to the set of synapses, either added to the
	 * NetworkBridge if that is what has been created or to the neural network
	 * immediate if the start and end networks are the same.
	 * 
	 * @param s
	 *            The synapse to be added to the set.
	 */
	public void addProjection(Synapse s) {
		// Projections internal to a single network.
		if (start.getID() == end.getID()) {
			start.addEdge(s);
		}

		// Projections form a network bridge.
		else {
			if (nb == null) {
				nb = new NetworkBridge(start, end);

				start.connect(nb);
				end.connect(nb);
			}

			nb.connect(s);
		}
	}

}
