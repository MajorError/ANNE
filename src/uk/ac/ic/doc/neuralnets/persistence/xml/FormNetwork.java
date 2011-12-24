package uk.ac.ic.doc.neuralnets.persistence.xml;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import uk.ac.ic.doc.neuralnets.graph.Node;
import uk.ac.ic.doc.neuralnets.graph.neural.NeuralNetwork;
import uk.ac.ic.doc.neuralnets.graph.neural.Neurone;

/**
 * 
 * @author Stephen
 *
 */
public class FormNetwork extends PartialObject {

	// Stores the set of neurones which have already been created, ready for
	// insertion into the completed Neural network.
	private Set<Node<?>> neurones = new HashSet<Node<?>>();

	// The parent network old id. Used for connecting up subnetworks.
	private int parentID = 0;

	/**
	 * FormNetwork is a partial object, all partial object must have an id to
	 * which they can be referenced. This must be set at construction.
	 * 
	 * @param id The old objects id.
	 */
	public FormNetwork(int id) {
		super(id);
	}

	/**
	 * Creates the neural network and inserts any stored neurones into the
	 * network, networks have no parameters so there are no properties to be
	 * added.
	 * 
	 * @return the new complete NeuralNetwork.
	 */
	public NeuralNetwork getNetwork() {
		// Create the new network.
		NeuralNetwork network = new NeuralNetwork();
		// Add all of the neurones to the network.
		network.addAllNodes((Collection<Node<?>>) neurones);
		// Return the new network.
		return network;
	}

	/**
	 * Adds a completed neurone to the set, to be inserted into the network when
	 * it is formed. Also sets the parent network id from the neurones 'z'
	 * value.
	 * 
	 * @param neurone Completed neurone to be added to the network.
	 */
	public void addNeurone(Neurone neurone) {
		neurones.add(neurone);
		parentID = neurone.getZ();
	}

	/**
	 * Returns the current parent network id of this network to be formed.
	 * 
	 * @return int Current Parent Network ID.
	 */
	public int getParentNetworkID() {
		return parentID;
	}

}
