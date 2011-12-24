package uk.ac.ic.doc.neuralnets.persistence.xml;

import java.util.Hashtable;
import java.util.Stack;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import uk.ac.ic.doc.neuralnets.graph.neural.NeuralNetwork;
import uk.ac.ic.doc.neuralnets.graph.neural.Neurone;

/**
 * 
 * @author Stephen
 * 
 */
public class XMLLoad extends DefaultHandler {

	// Reference to the logger.
	static Logger log = Logger.getLogger(XMLLoad.class);

	// Reference for network that will be returned (root).
	private NeuralNetwork nnet = null;

	// Stack of currently building partial objects.
	private Stack<PartialObject> pObjects = null;

	// Store of completed networks and neurones referenced by their OLD ID, for
	// linking.
	private Hashtable<Integer, NeuralNetwork> networks = null;
	private Hashtable<Integer, Neurone> neurones = null;

	// Key is network old id, value is parent network old id.
	private Hashtable<Integer, Integer> subNetworks = null;

	public XMLLoad() {
		super();
	}

	/**
	 * Get the currently parsed network.
	 * 
	 * @return NeuralNetwork
	 */
	public NeuralNetwork getNeuralNetwork() {
		return nnet;
	}

	/**
	 * Method called on starting to parse a document. All ADT storage is
	 * initialized, also the root network which contains the first initial
	 * networks is created and its stored with an id of 0.
	 */
	public void startDocument() {
		// Initialize ADT storage.
		pObjects = new Stack<PartialObject>();
		networks = new Hashtable<Integer, NeuralNetwork>();
		neurones = new Hashtable<Integer, Neurone>();
		subNetworks = new Hashtable<Integer, Integer>();

		nnet = new NeuralNetwork();
		networks.put(0, nnet);
	}

	/**
	 * Method called on finish of parsing a document, it inserts networks into
	 * their parent networks, by traversing the subNetworks map and adding each
	 * child network to its parent.
	 */
	public void endDocument() {
		// Connect up subnetworks.
		for (int cnet : subNetworks.keySet()) {
			networks.get(subNetworks.get(cnet)).addNode(networks.get(cnet));
		}
	}

	/**
	 * Method called on a new element being discovered. Method created partial
	 * objects or adds properties to the most recent partial object depending on
	 * the name of the tag. When creating a partial object any initial
	 * properties are added at that time.
	 */
	public void startElement(String uri, String name, String qName,
			Attributes atts) {
		if (qName.equals("population")) {
			// Create a new Partial network.
			pObjects.push(new FormNetwork(Integer.parseInt(atts
					.getValue("name"))));

		} else if (qName.equals("instance")) {
			// Create a new Partial neurone.
			pObjects
					.push(new FormNeurone(Integer.parseInt(atts.getValue("id"))));

		} else if (qName.equals("projection")) {
			// Create a new Partial Edge group and add source and target
			// destinations.
			FormProjections fPorj = new FormProjections(-1);
			fPorj.setStart(networks.get(Integer.parseInt(atts
					.getValue("source"))));
			fPorj.setEnd(networks
					.get(Integer.parseInt(atts.getValue("target"))));
			pObjects.push(fPorj);

		} else if (qName.equals("connection")) {
			// Create a new Partial Synapse and add source and target
			// destinations.
			FormSynapse fSyn = new FormSynapse(Integer.parseInt(atts
					.getValue("id")));
			fSyn.setStart(neurones.get(Integer.parseInt(atts
					.getValue("pre_cell_id"))));
			fSyn.setEnd(neurones.get(Integer.parseInt(atts
					.getValue("post_cell_id"))));
			pObjects.push(fSyn);

			// Anything else is a property, make sure that it can be added to
			// something.
		} else if (!pObjects.isEmpty()) {
			if (qName.equals("meta:property")) {
				// Add the key and value to the most recent partial object.
				pObjects.peek().addProperty(atts.getValue(0), atts.getValue(1));

			} else if (qName.equals("location")) {
				// Add the location xyz to the most recent partial object.
				pObjects.peek().addProperty(atts.getLocalName(0),
						atts.getValue(0));
				pObjects.peek().addProperty(atts.getLocalName(1),
						atts.getValue(1));
				pObjects.peek().addProperty(atts.getLocalName(2),
						atts.getValue(2));

			} else if (qName.equals("properties")) {
				// Add the key and value to the most recent partial object.
				pObjects.peek().addProperty(atts.getLocalName(0),
						atts.getValue(0));

			}
		}
	}

	/**
	 * On finishing a tag, if it is a partial object then form that object and
	 * pop the partial off the top of the stack, else do nothing.
	 */
	public void endElement(String uri, String name, String qName) {
		if (qName.equals("population")) {

			// Get the partial object.
			FormNetwork fNet = (FormNetwork) pObjects.pop();
			// Create the network as all information has been gathered.
			NeuralNetwork completedNetwork = fNet.getNetwork();
			// Place the network in a map, referenced by its OLD ID.
			networks.put(fNet.getID(), completedNetwork);
			// Add references for parent networks.
			subNetworks.put(fNet.getID(), fNet.getParentNetworkID());

		} else if (qName.equals("instance")) {

			// Get the old ID of the neurone.
			int oldID = ((FormNeurone) pObjects.peek()).getID();
			// Create the neurone as all information has been gathered.
			Neurone completedNeurone = ((FormNeurone) pObjects.pop())
					.getNeurone();
			// Add the neurone to the current network which contains it.
			((FormNetwork) pObjects.peek()).addNeurone(completedNeurone);
			// Place the neurone in a map, referenced by its OLD ID.
			neurones.put(oldID, completedNeurone);

		} else if (qName.equals("projection")) {

			// No need to create object, just pop off the top.
			pObjects.pop();

		} else if (qName.equals("connection")) {

			// Get the partial object.
			FormSynapse fSyn = ((FormSynapse) pObjects.pop());
			// Create the synapse and add it to the projection list.
			((FormProjections) pObjects.peek()).addProjection(fSyn
					.formSynapse());

		}
	}

	/**
	 * Do nothing on CDATA, neuroML does not use CDATA.
	 */
	public void characters(char ch[], int start, int length) {
		// Do nothing on CDATA.
	}

}
