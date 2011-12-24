package uk.ac.ic.doc.neuralnets.gui.graph;

import org.apache.log4j.Logger;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphContainer;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.IContainer;

import uk.ac.ic.doc.neuralnets.graph.Node;
import uk.ac.ic.doc.neuralnets.graph.neural.NeuralNetwork;

public class GUINetwork extends GraphContainer implements NodeContainer {

	private NeuralNetwork network;
	private static final Logger log = Logger.getLogger(GUINetwork.class);

	/**
	 * Creates a GUI Network which can contain more GUI Networks or GUI Nodes.
	 * 
	 * @param network
	 *            Network to model in GUI
	 * @param container
	 *            Graph to insert GUI Network into
	 * @param g
	 *            Contents of network in a displayable format
	 * @param style
	 *            Style of GUI Network
	 */
	public GUINetwork(NeuralNetwork network, IContainer container, Graph g,
			int style) {
		super(container, style);
		setNode(network);
		setText(network.toString());
		graph = g;
		createContents();
	}

	public void createToolTip() {
		nodeFigure.setToolTip( new Label( "Neural Network of " 
                + network.getNodes().size() + " nodes.") );
	}

	@SuppressWarnings("unchecked")
	public void setNode(Node network) {
		this.network = (NeuralNetwork) network;
	}

	@SuppressWarnings("unchecked")
	public Node getNode() {
		return network;
	}

	/**
	 * Persists the location of this node in the GUI to the model node.
	 */
	public void persistLocation() {
		Point loc = getLocation();
		log.debug("Persisting " + loc + " for " + this);
		getNode().setPos(loc.x, loc.y, 0);
	}

	/**
	 * Creates contents of GUI Network (drop down box) showing information
	 * regarding the Neural Network.
	 */
	private void createContents() {
		StringBuffer text = new StringBuffer();
		text.append("\nNeural Network Contents:\n\n");
		text.append("\t" + network.getNodes().size() + " Nodes.\n");
		text.append("\t" + network.getEdges().size() + " Edges.\n");
		GraphNode lbl = new GraphNode(this, SWT.NONE, text.toString());
		lbl.setFont(new Font(Display.getCurrent(), new FontData("Arial", 20,
				SWT.BOLD)));
		lbl.setBackgroundColor(ColorConstants.white);
		lbl.setBorderWidth(0);
		lbl.setHighlightColor(ColorConstants.white);
		lbl.setBorderColor(ColorConstants.white);
		lbl.setLocation(getSize().width / 2, 75);
	}

}
