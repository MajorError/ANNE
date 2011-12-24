package uk.ac.ic.doc.neuralnets.gui;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphItem;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.IContainer;

import uk.ac.ic.doc.neuralnets.commands.CommandControl;
import uk.ac.ic.doc.neuralnets.commands.CommandEvent;
import uk.ac.ic.doc.neuralnets.coreui.ZoomingInterfaceManager;
import uk.ac.ic.doc.neuralnets.events.Event;
import uk.ac.ic.doc.neuralnets.events.EventHandler;
import uk.ac.ic.doc.neuralnets.events.EventManager;
import uk.ac.ic.doc.neuralnets.events.GraphUpdateEvent;
import uk.ac.ic.doc.neuralnets.graph.Edge;
import uk.ac.ic.doc.neuralnets.graph.GraphStreamer;
import uk.ac.ic.doc.neuralnets.graph.Node;
import uk.ac.ic.doc.neuralnets.graph.neural.NetworkBridge;
import uk.ac.ic.doc.neuralnets.graph.neural.NeuralNetwork;
import uk.ac.ic.doc.neuralnets.graph.neural.NeuralNetworkSimulationEvent;
import uk.ac.ic.doc.neuralnets.graph.neural.NeuralNetworkTickEvent;
import uk.ac.ic.doc.neuralnets.graph.neural.Neurone;
import uk.ac.ic.doc.neuralnets.graph.neural.NewNeuroneTypeEvent;
import uk.ac.ic.doc.neuralnets.graph.neural.NodeChargeUpdateEvent;
import uk.ac.ic.doc.neuralnets.gui.graph.CachingLayout;
import uk.ac.ic.doc.neuralnets.gui.graph.GUIAnchor;
import uk.ac.ic.doc.neuralnets.gui.graph.GUIBridge;
import uk.ac.ic.doc.neuralnets.gui.graph.GUIEdge;
import uk.ac.ic.doc.neuralnets.gui.graph.GUINetwork;
import uk.ac.ic.doc.neuralnets.gui.graph.GUINode;
import uk.ac.ic.doc.neuralnets.gui.graph.NodeContainer;
import uk.ac.ic.doc.neuralnets.gui.graph.events.ChargeUpdateHandler;
import uk.ac.ic.doc.neuralnets.gui.graph.events.NeuroneTypesPersister;
import uk.ac.ic.doc.neuralnets.gui.graph.events.NodeLocationUpdater;
import uk.ac.ic.doc.neuralnets.gui.graph.events.ToolTipUpdater;
import uk.ac.ic.doc.neuralnets.gui.graph.listener.KeyboardPlugin;
import uk.ac.ic.doc.neuralnets.gui.graph.listener.MousePlugin;
import uk.ac.ic.doc.neuralnets.persistence.FileSpecification;
import uk.ac.ic.doc.neuralnets.util.Transformer;
import uk.ac.ic.doc.neuralnets.util.plugins.PluginLoadException;
import uk.ac.ic.doc.neuralnets.util.plugins.PluginManager;

/**
 * Manages the GUI representation of a layered neural network. Controls
 * importing and exporting networks to and from their standard model
 * representation, zooming into and out of layers of the network, and tooltips.
 * 
 * Listens synchronously for GraphUpdateEvents, NewNeuroneTypeEvents,
 * NeuralNetworkTickEvents and NeuralNetworkSimulationEvents
 * 
 * @see GraphUpdateEvent
 * @see NewNeuroneTypeEvent
 * @see NeuralNetworkSimulationEvent
 * @see NeuralNetworkTickEvent
 * 
 * @author Chris Matthews
 */
public class GUIManager extends ZoomingInterfaceManager<Graph, GraphItem> {

	static Logger log = Logger.getLogger(GUIManager.class);
	private IContainer graph;
	private Stack<NeuralNetwork> zoomLevels;
	private Stack<Integer> zoomIDs;
	private Map<Node<?>, GraphNode> guinodes;
	private Map<NeuralNetwork, GUIAnchor> sources;
	private Map<NeuralNetwork, GUIAnchor> sinks;
	private Transformer<Edge<?, ?>, GraphConnection> edgeBuilder;
	private Transformer<Node<?>, GraphNode> nodeBuilder;

	/**
	 * Creates a GUIManager to display a given Neural Network on a given SWT
	 * IContainer canvas.
	 * 
	 * @param graph
	 *            the canvas on which to display the network
	 * @param network
	 *            the network to be displayed in the GUI
	 */
	public GUIManager(final IContainer graph, NeuralNetwork network) {
		this(graph, network, null);
	}

	/**
	 * Creates a GUIManager to display a given Neural Network, from a given
	 * location, on a given SWT IContainer canvas.
	 * 
	 * @param graph
	 *            the canvas on which to display the network
	 * @param network
	 *            the network to be displayed in the GUI
	 * @param location
	 *            the location of the network
	 */
	public GUIManager(final IContainer graph, NeuralNetwork network,
			FileSpecification location) {
		this.graph = graph;

		guinodes = new HashMap<Node<?>, GraphNode>();
		sources = new HashMap<NeuralNetwork, GUIAnchor>();
		sinks = new HashMap<NeuralNetwork, GUIAnchor>();
		zoomLevels = new Stack<NeuralNetwork>();
		zoomIDs = new Stack<Integer>();
		commandControl = new CommandControl();

		EventManager.get().registerSynchro(GraphUpdateEvent.class,
				new NodeLocationUpdater(this));
		EventManager.get().registerAsync(GraphUpdateEvent.class,
				new ToolTipUpdater(this));
		EventManager.get().registerSynchro(NewNeuroneTypeEvent.class,
				new NeuroneTypesPersister());
		log.trace("Registering handler");
		EventManager.get().registerSynchro(NeuralNetworkTickEvent.class,
				new EventHandler() {

					public void flush() {
						// no-op
					}

					public void handle(final Event e) {
						log.trace(e);
						Display.getDefault().syncExec(new Runnable() {
							public void run() {
								log.debug("Handling " + e);
								for (GraphNode g : guinodes.values()) {
									if (g instanceof GUINode) {
										((GUINode) g).updateChargeOverlay();
									}
								}
							}
						});
					}

					public boolean isValid() {
						return true;
					}

					public String getName() {
						return "WTFAMIOVERHERE";
					}
				});
		EventManager.get().registerSynchro(NeuralNetworkSimulationEvent.class,
				new EventHandler() {

					public void flush() {
					}

					public void handle(final Event e) {
						Display.getDefault().syncExec(new Runnable() {

							public void run() {
								if (((NeuralNetworkSimulationEvent) e)
										.started()) {
									disableGraph();
								} else {
									enableGraph();
								}
							}
						});
					}

					public boolean isValid() {
						return true;
					}

					public String getName() {
						return "GraphDisabler";
					}
				});

		edgeBuilder = new Transformer<Edge<?, ?>, GraphConnection>() {
			private static final long serialVersionUID = 1L;

			/**
			 * Transforms an edge into the correct format used by the GUI. If
			 * the edge runs to or from an external node, a source or sink node
			 * to the appropriate external network is created (if one does not
			 * already exist). If the edge cannot be shown in the current GUI
			 * view, null is returned.
			 * 
			 * @param e
			 *            the edge to be shown in the GUI
			 * @return the edge transformed into the correct format to be used
			 *         by the GUI
			 */
			public GraphConnection transform(Edge<?, ?> e) {
				GraphNode start = guinodes.get(e.getStart());
				GraphNode end = guinodes.get(e.getEnd());
				if (start == null && end == null) {
					return null;
				}

				if (start == null) {
					Node<?> startNode = e.getStart();
					NeuralNetwork startNetwork = utils.findNetwork(startNode);
					if (startNetwork == null)
						return null;
					start = sources.get(startNetwork);
					if (start == null) {
						start = new GUIAnchor(false, startNetwork, graph,
								SWT.NONE);
						sources.put(startNetwork, (GUIAnchor) start);
					}

				} else if (end == null) {
					Node<?> endNode = e.getEnd();
					NeuralNetwork endNetwork = utils.findNetwork(endNode);
					if (endNetwork == null)
						return null;
					end = sinks.get(endNetwork);
					if (end == null) {
						end = new GUIAnchor(true, endNetwork, graph, SWT.NONE);
						sinks.put(endNetwork, (GUIAnchor) end);
					}
				}
				Graph g = graph instanceof GUINetwork ? ((GUINetwork) graph)
						.getGraph() : (Graph) graph;
				return e instanceof NetworkBridge ? new GUIBridge(
						(NetworkBridge) e, g, SWT.NONE, start, end)
						: new GUIEdge(e, g, SWT.NONE, start, end);
			}
		};
		nodeBuilder = new Transformer<Node<?>, GraphNode>() {
			private static final long serialVersionUID = 1L;

			/**
			 * Transforms a node into the correct format used by the GUI.
			 * 
			 * @param n
			 *            the node to be shown in the GUI
			 * @return the node transformed into the correct format to be used
			 *         by the GUI
			 */
			public GraphNode transform(Node<?> n) {
				return n instanceof NeuralNetwork ? new GUINetwork(
						(NeuralNetwork) n, graph,
						graph instanceof GUINetwork ? ((GUINetwork) graph)
								.getGraph() : (Graph) graph, SWT.NONE)
						: new GUINode(n, graph, SWT.NONE);
			}
		};

		setNetwork(network, location);

		graph.setLayoutAlgorithm(new CachingLayout(), true);
		if (graph instanceof Graph)
			createListeners();
		EventManager.get().registerAsync(NodeChargeUpdateEvent.class,
				new ChargeUpdateHandler(this));
	}

	protected void reset() {
		clearCanvas();
		zoomLevels.clear();
		zoomIDs.clear();
		zoomLevels.push(network);
		zoomIDs.push(network.getID());
		createGraph(network);
		graph.applyLayout();
	}

	/**
	 * Draws a network onto the GUI, to be displayed in the current view.
	 * 
	 * @param network
	 *            the network to be imported into the GUI
	 */
	private void createGraph(NeuralNetwork network) {
		createGraph(new GraphStreamer<GraphConnection, GraphNode>(network,
				edgeBuilder, nodeBuilder));
	}

	/**
	 * Draws a network onto the GUI, to be displayed in the current view. Uses a
	 * GraphStreamer to transform the network into the correct format.
	 * 
	 * @param gs
	 *            the GraphStreamer used to transform the network
	 */
	private void createGraph(GraphStreamer<GraphConnection, GraphNode> gs) {
		guinodes.clear();
		Iterator<GraphNode> nit = gs.getNodeIterator();
		while (nit.hasNext()) {
			GraphNode n = nit.next();
			guinodes.put(((NodeContainer) n).getNode(), n);
		}

		Iterator<GraphConnection> eit = gs.getEdgeIterator();
		while (eit.hasNext())
			// just consume it, SWT does the rest
			eit.next();

		// Add source GUIAnchors
		for (NetworkBridge nb : getCurrentNetwork().getIncoming())
			for (Edge<?, ?> y : nb.getBundle())
				edgeBuilder.transform(y);

		// Add sink GUIAnchors
		for (NetworkBridge nb : getCurrentNetwork().getOutgoing())
			for (Edge<?, ?> y : nb.getBundle())
				edgeBuilder.transform(y);

	}

	/**
	 * Removes the given neural network from the current view, and redraws the
	 * screen as necessary.
	 * 
	 * @param n
	 *            the neural network to add to the current section of the neural
	 *            network
	 */
	@Override
	public void removeNetwork(NeuralNetwork n) {
		for (Node<?> gn : n.getNodes()) {
			guinodes.remove(gn);
		}
		super.removeNetwork(n);
	}

	@Override
	public void addConnection(Edge<?, ?> e) {
		edgeBuilder.transform(e);
		super.addConnection(e);
	}

	/**
	 * Removes the figure corresponding to the given GraphItem from the view.
	 * Called by remove().
	 * 
	 * @param i
	 *            the GraphItem whos figure is to be removed
	 */
	private void removeFigure(GraphItem i) {
		i.unhighlight();
		i.dispose();
		getGraph().getConnections().remove(i);
		getGraph().getNodes().remove(i);
		getGraph().redraw();
	}

	public NeuralNetwork getCurrentNetwork() {
		return zoomLevels.peek();
	}

	public Stack<Integer> getZoomIDs() {
		return zoomIDs;
	}

	public Stack<NeuralNetwork> getZoomLevels() {
		return zoomLevels;
	}

	/**
	 * Loads all mouse and keyboard plugins. These can be used for editing the
	 * neural network using the GUI.
	 */
	private void createListeners() {
		Graph g = getGraph();
		try {
			for (String plugin : PluginManager.get().getPluginsOftype(
					MousePlugin.class)) {
				log.debug("Loading mouse plugin " + plugin);
				try {
					MousePlugin p = PluginManager.get().getPlugin(plugin,
							MousePlugin.class);
					p.setManager(this);
					g.addMouseListener(p);
				} catch (PluginLoadException ex) {
					log.error("Bad plugin " + plugin + " being ignored", ex);
				}
			}
		} catch (PluginLoadException ex) {
			log.error("Couldn't get list of mouse plugins!", ex);
		}
		try {
			for (String plugin : PluginManager.get().getPluginsOftype(
					KeyboardPlugin.class)) {
				log.debug("Loading keyboard plugin " + plugin);
				try {
					KeyboardPlugin p = PluginManager.get().getPlugin(plugin,
							KeyboardPlugin.class);
					p.setManager(this);
					g.addKeyListener(p);
				} catch (PluginLoadException ex) {
					log.error("Bad plugin " + plugin + " being ignored", ex);
				}
			}
		} catch (PluginLoadException ex) {
			log.error("Couldn't get list of keyboard plugins!", ex);
		}
	}

	public void zoomIn(NeuralNetwork n) {
		persistLocations();
		clearCanvas();
		zoomLevels.push(n);
		zoomIDs.push(n.getID());
		redrawCurrentView();
		getGraph().getSelection().clear();
		EventManager.get().fire(new CommandEvent());
		EventManager.get().fire(new GraphUpdateEvent());
	}

	public void zoomOut() {
		persistLocations();
		if (zoomLevels.size() == 1)
			return;
		clearCanvas();
		zoomLevels.pop();
		zoomIDs.pop();
		redrawCurrentView();
		getGraph().getSelection().clear();
		EventManager.get().fire(new CommandEvent());
		EventManager.get().fire(new GraphUpdateEvent());
	}

	@SuppressWarnings("unchecked")
	public boolean canZoomIn() {
		List<GraphItem> selection = getGraph().getSelection();
		int numNetworksSelected = 0;
		for (GraphItem n : selection) {
			if (n instanceof GUINetwork) {
				numNetworksSelected++;
				if (numNetworksSelected >= 2)
					break;
			}
		}
		return numNetworksSelected == 1;
	}

	public boolean canZoomOut() {
		return zoomLevels.size() >= 2;
	}

	public void redrawCurrentView() {
		clearCanvas();
		createGraph(zoomLevels.peek());
		graph.applyLayout();
	}

	/**
	 * Clears the current view.
	 */
	private void clearCanvas() {

		for (GraphNode n : guinodes.values()) {
			removeFigure(n);
		}
		guinodes.clear();

		for (GraphNode n : sources.values()) {
			removeFigure(n);
		}
		sources.clear();

		for (GraphNode n : sinks.values()) {
			removeFigure(n);
		}
		sinks.clear();

	}

	public void persistLocations() {
		// Iterate through all existing nodes.
		for (GraphNode n : guinodes.values()) {
			// Get the node and if it is a GUINode then persist location.
			if (n.getLocation().x == 0 || n.getLocation().y == 0)
				continue;
			if (n instanceof GUINode)
				((GUINode) n).persistLocation();
			else if (n instanceof GUINetwork)
				((GUINetwork) n).persistLocation();
		}
	}

	public void updateInterfaceHints() {
		for (GraphNode n : guinodes.values())
			if (n instanceof GUINode)
				((GUINode) n).createToolTip();
			else if (n instanceof GUINetwork)
				((GUINetwork) n).createToolTip();
			else
				log.trace("Cannot update tooltip on " + n);
		for (Object c : graph.getGraph().getConnections()) {
			if (c instanceof GUIEdge)
				((GUIEdge) c).createToolTip();
			else if (c instanceof GUIBridge)
				((GUIBridge) c).createToolTip();
			else
				log.trace("Cannot update tooltip on " + c);
		}
	}

	public Graph getGraph() {
		return (Graph) graph;
	}

	public GraphItem getNode(Neurone n) {
		return guinodes.get(n);
	}

	public void remove(GraphItem i) {
		if (i instanceof GUINode) {
			GUINode gn = (GUINode) i;
			getCurrentNetwork().getEdges()
					.removeAll(gn.getNode().getOutgoing());
			getCurrentNetwork().getEdges()
					.removeAll(gn.getNode().getIncoming());
			getCurrentNetwork().getNodes().remove(gn.getNode());
		} else if (i instanceof GUIEdge) {
			GUIEdge ge = (GUIEdge) i;
			getCurrentNetwork().getEdges().remove(ge.getEdge());
			ge.getEdge().getStart().getOutgoing().remove(ge.getEdge());
			ge.getEdge().getEnd().getIncoming().remove(ge.getEdge());
		}
		removeFigure(i);
	}

	/**
	 * Disable clicks to the graph area.
	 */
	public void disableGraph() {
		((Graph) graph).setEnabled(false);
	}

	/**
	 * Enable clicks to the graph area
	 */
	public void enableGraph() {
		((Graph) graph).setEnabled(true);
	}
}
