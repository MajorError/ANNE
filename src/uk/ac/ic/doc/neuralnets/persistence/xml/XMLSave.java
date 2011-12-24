package uk.ac.ic.doc.neuralnets.persistence.xml;

import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.apache.log4j.Logger;

import uk.ac.ic.doc.neuralnets.graph.Edge;
import uk.ac.ic.doc.neuralnets.graph.Node;
import uk.ac.ic.doc.neuralnets.graph.neural.NetworkBridge;
import uk.ac.ic.doc.neuralnets.graph.neural.NeuralNetwork;
import uk.ac.ic.doc.neuralnets.graph.neural.Neurone;
import uk.ac.ic.doc.neuralnets.graph.neural.Synapse;
import uk.ac.ic.doc.neuralnets.persistence.xml.LoadXMLService;
import uk.ac.ic.doc.neuralnets.persistence.MethodSelector;
import uk.ac.ic.doc.neuralnets.persistence.PersistenceAssistance;

/**
 * 
 * @author Stephen
 * 
 */
public class XMLSave {

	// Reference to logger.
	static Logger log = Logger.getLogger(LoadXMLService.class);

	// Reference to the network being stored.
	private NeuralNetwork network;

	// Storage for networks, network bridges to be exported.
	private Set<NeuralNetwork> networks = null;
	private Set<NetworkBridge> networkBridges = null;

	// Storage for parent/child network references.
	private HashMap<Integer, Integer> subNetworks = null;

	// The current amount of indent on each line.
	private String indent = "";

	// The method selector for persisting any object fields.
	private MethodSelector ms = null;

	// The writer to output the xml to.
	private BufferedWriter out = null;
    
    // Some Persistence Assistance 
    private PersistenceAssistance pa = PersistenceAssistance.newInstance();
    private int done = 0;

	/**
	 * Create object and initalise with the network to be output and the write
	 * to output the xml to.
	 * 
	 * @param network
	 *            Network to be exported
	 * @param out
	 *            Destination for generated xml
	 */
	public XMLSave(NeuralNetwork network, BufferedWriter out) {
		ms = new MethodSelector();
        pa.setSaveTarget( network );

		this.out = out;
		this.network = network;
	}

	/**
	 * Generates the xml of the network and outputs it to the file.
	 * 
	 * @throws IOException
	 *             On encountering a problem.
	 */
	public void generateXML() throws IOException {
		// Reset the indent.
		indent = "";

		// Search the network for all networks and network bridges.
		searchNetwork();

		// Output xml document preamble.
		addLine("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		addLine("<networkml xmlns=\"http://morphml.org/networkml/schema\"");
		addLine("\txmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
		addLine("\txmlns:meta=\"http://morphml.org/metadata/schema\"");
		addLine("\txsi:schemaLocation=\"http://morphml.org/networkml/schema/Schemata/v1.7.3/Level3/NetworkML_v1.7.3.xsd\"");
		addLineI("lengthUnits=\"micron\">");

		addBlankLine();
		addLine("<meta:notes>Produced by ANNE: the Artifical Neural Network Editor</meta:notes>");
		addBlankLine();

		// Output all of the networks and their neurones.
		addLineI("<populations>");
		for (NeuralNetwork n : networks) {
			ouputNeurones(n);
		}
		addLineD("</populations>");

		addBlankLine();

		// Output all of the synapses.
		addLineI("<projections units=\"Physiological Units\">");
		for (NeuralNetwork n : networks) {
			outputSynapses(n);
		}
		for (NetworkBridge nB : networkBridges) {
			outputNetworkBridges(nB);
		}
		addLineD("</projections>");

		// Finish the xml document.
		addBlankLine();
		addLineD("</networkml>");
	}

	/**
	 * Outputs the network bridge but only if the network bridge contains more
	 * than zero synapses.
	 * 
	 * @param nb
	 *            The network bridge to output.
	 * @throws IOException
	 *             thrown on encountering a problem.
	 */
	private void outputNetworkBridges(NetworkBridge nb) throws IOException {
		// Only output on more than zero synapses.
		if (nb.getBundle().size() > 0) {
			addLineI("<projection name=\"NetworkBridge-Synapses\" source=\""
					+ nb.getStart().getID() + "\" target=\""
					+ nb.getEnd().getID() + "\">");
			addLine("<synapse_props synapse_type=\""
					+ Synapse.class.getCanonicalName() + "\" />");
			addLineI("<connections size=\"" + nb.getBundle().size() + "\">");
			for (Edge<?, ?> s : nb.getBundle()) {
				outputSynapse((Synapse) s);
			}
			addLineD("</connections>");
			addLineD("</projection>");
		}
	}

	/**
	 * Counts the number of networks in a collection of nodes.
	 * 
	 * @param nodes
	 *            To count networks in.
	 * @return int Number of networks found.
	 */
	private int countNetworks(Collection<Node<?>> nodes) {
		int count = 0;

		for (Node<?> node : nodes) {
			if (node instanceof NeuralNetwork) {
				count++;
			}
		}

		return count;
	}

	/**
	 * Counts the number of bridges in a collection of edges.
	 * 
	 * @param edges
	 *            To count bridges in.
	 * @return int Number of bridges found.
	 */
	private int countBridges(Collection<Edge<?, ?>> edges) {
		int count = 0;

		for (Edge<?, ?> e : edges) {
			if (e instanceof NetworkBridge) {
				count++;
			}
		}

		return count;
	}

	/**
	 * Outputs the synapses of a neural network, but only there is more than one
	 * synapse to output.
	 * 
	 * @param n
	 *            The neural network.
	 * @throws IOException
	 */
	private void outputSynapses(NeuralNetwork n) throws IOException {
		int size = n.getEdges().size() - countBridges(n.getEdges());

		if (size > 0) {
			addLineI("<projection name=\"Network-Synapses\" source=\""
					+ n.getID() + "\" target=\"" + n.getID() + "\">");
			addLine("<synapse_props synapse_type=\""
					+ Synapse.class.getCanonicalName() + "\" />");
			addLineI("<connections size=\"" + size + "\">");
			for (Edge<?, ?> e : n.getEdges()) {
				if (e instanceof Synapse)
					outputSynapse((Synapse) e);
			}
			addLineD("</connections>");
			addLineD("</projection>");
		}
	}

	/**
	 * Outputs a single synapse in xml format.
	 * 
	 * @param s
	 *            Synapse to output.
	 * @throws IOException
	 *             thrown on encountering a problem.
	 */
	private void outputSynapse(Synapse s) throws IOException {
		try {
			addLineI("<connection id=\"" + s.getID() + "\" pre_cell_id=\""
					+ s.getStart().getID() + "\" post_cell_id=\""
					+ s.getEnd().getID() + "\">");

			Set<Field> persistable = ms.getPersistableMethodsAndFields(s
					.getClass());
			addLineI("<properties weight=\"" + s.getWeight() + "\">");

			addLineI("<meta:properties>");
			for (Field f : persistable) {
				addLine("<meta:property tag=\"" + f.getName() + "\" value=\""
						+ f.get(s) + "\" />");
			}
			addLine("<meta:property tag=\"instance_type\" value=\""
					+ s.getClass().getCanonicalName() + "\" />");

			addLineD("</meta:properties>");

			addLineD("</properties>");

			addLineD("</connection>");
            
            pa.done( ++done );
		} catch (IllegalArgumentException e) {
			log.error("Cannot get argument!", e);
		} catch (IllegalAccessException e) {
			log.error("Cannot access value!", e);
		}
	}

	/**
	 * Outputs the neurones for a single network, but only if there is more than
	 * one.
	 * 
	 * @param n
	 *            Network who's neurones are to be output.
	 * @throws IOException
	 *             thrown when encountering a problem.
	 */
	private void ouputNeurones(NeuralNetwork n) throws IOException {
		int bundleSize = n.getNodes().size();
		int countNN = countNetworks(n.getNodes());
		int size = bundleSize - countNN;

		if (bundleSize > 0 && bundleSize - countNN > 0) {
			addLineI("<population name=\"" + n.getID() + "\">");
			addLineI("<instances size=\"" + size + "\">");
			for (Node<?> node : n.getNodes()) {
				if (node instanceof Neurone) {
					if (network.getID() == subNetworks.get(n.getID())) {
						outputNeurone((Neurone) node, 0);
					} else {
						outputNeurone((Neurone) node, subNetworks
								.get(n.getID()));
					}
				}
			}
			addLineD("</instances>");
			addLineD("</population>");
		}
	}

	/**
	 * Output the neurone input, z is the neurone height and also the parent
	 * network's id.
	 * 
	 * @param node
	 *            Neurone to output.
	 * @param z
	 *            Height and parent network id
	 * @throws IOException
	 *             thrown on encountering a problem.
	 */
	private void outputNeurone(Neurone node, int z) throws IOException {
		try {
			addLineI("<instance id=\"" + node.getID() + "\">");

			addLineI("<meta:properties>");
			Set<Field> persistable = ms.getPersistableMethodsAndFields(node
					.getClass());

			addLine("<meta:property tag=\"instance_type\" value=\""
					+ node.getClass().getCanonicalName() + "\" />");

			for (Field f : persistable) {
				addLine("<meta:property tag=\"" + f.getName() + "\" value=\""
						+ f.get(node) + "\" />");
			}

			addLineD("</meta:properties>");

			addLine("<location x=\"" + node.getX() + "\" y=\"" + node.getY()
					+ "\" z=\"" + z + "\" />");
			addLineD("</instance>");
            
            pa.done( ++done );
		} catch (IllegalArgumentException e) {
			log.error("Cannot get argument!", e);
		} catch (IllegalAccessException e) {
			log.error("Cannot access value!", e);
		}
	}

	/*
	 * Does breadth-first search on the initial network across network bridges
	 * to discover all of the networks to output.
	 */
	/**
	 * Does breadth-first search on the initial network across all the network
	 * bridges, nodes and edges to discover all of the networks and network
	 * bridges that need to be output. First will zero all ADT storage incase
	 * the network has changed and to prevent duplicates. Also keeps track of
	 * networks parent networks.
	 */
	private void searchNetwork() {
		// Create storage for networks and network bridges.
		networks = new HashSet<NeuralNetwork>();
		networkBridges = new HashSet<NetworkBridge>();
		subNetworks = new HashMap<Integer, Integer>();

		// List to store networks to later traverse.
		LinkedList<NeuralNetwork> traverse = new LinkedList<NeuralNetwork>();

		// Add the first network to look at.
		traverse.add(network);
		subNetworks.put(network.getID(), -1);

		// While there are networks to search, search!
		while (traverse.size() > 0) {
			NeuralNetwork next = traverse.poll();

			// If the result already contains the network then it has already
			// been search.
			if (!networks.contains(next)) {
				// Otherwise add it to the result.
				networks.add(next);

				// For each node in the network, if it is a NeuralNetwork, add
				// it to the traverse.
				for (Node<?> node : next.getNodes()) {
					if (node instanceof NeuralNetwork) {
						traverse.add((NeuralNetwork) node);
						subNetworks.put(((NeuralNetwork) node).getID(), next
								.getID());
					}
				}

				// For each edge in the network, if it is a NetworkBridge, add
				// it to the tempBridges.
				for (Edge<?, ?> edge : next.getEdges()) {
					if (edge instanceof NetworkBridge
							&& !networkBridges.contains((NetworkBridge) edge)) {
						networkBridges.add((NetworkBridge) edge);

						traverse.add(((NetworkBridge) edge).getStart());
						traverse.add(((NetworkBridge) edge).getEnd());
					}
				}

				for (NetworkBridge incoming : next.getIncoming()) {
					if (!networkBridges.contains(incoming)) {
						networkBridges.add(incoming);

						traverse.add(incoming.getStart());
					}
				}

				for (NetworkBridge outgoing : next.getOutgoing()) {
					if (!networkBridges.contains(outgoing)) {
						networkBridges.add(outgoing);

						traverse.add(outgoing.getEnd());
					}
				}
			}
		}
	}

	/**
	 * Calls addLine(line) and then increments the tab count on the indent.
	 * 
	 * @param line
	 *            String to output.
	 * @throws IOException
	 *             thrown on encountering a problem.
	 */
	private void addLineI(String line) throws IOException {
		addLine(line);

		indent += "\t";
	}

	/**
	 * If the indent is more than 1 then decrements the indent tab count by one,
	 * then calls addLine(line) to output the line.
	 * 
	 * @param line
	 *            String to output.
	 * @throws IOException
	 *             thrown on encountering a problem.
	 */
	private void addLineD(String line) throws IOException {
		if (indent.length() > 0) {
			indent = indent.substring(1);
		}

		addLine(line);
	}

	/**
	 * Writes the give line to the output, with the ident in front and new line
	 * mark behind.
	 * 
	 * @param line
	 *            String to output.
	 * @throws IOException
	 *             thrown on encountering a problem.
	 */
	private void addLine(String line) throws IOException {
		out.write(indent + line + "\n");
	}

	/**
	 * Writes a blank line to the output, used for spacing.
	 * 
	 * @throws IOException
	 *             thrown on encountering a problem.
	 */
	private void addBlankLine() throws IOException {
		out.write("\n");
	}

}
