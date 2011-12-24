package uk.ac.ic.doc.neuralnets.gui.graph;

import org.apache.log4j.Logger;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.IContainer;

import uk.ac.ic.doc.neuralnets.graph.Node;
import uk.ac.ic.doc.neuralnets.graph.neural.NeuralNetwork;

/**
 * GUIAnchor acts as both a source and sink in a network to show what it
 * connects to and what connects to it.
 * 
 * @author Ismail
 */

public class GUIAnchor extends GraphNode implements NodeContainer {

	private static final Logger log = Logger.getLogger(GUIAnchor.class);

	private static int figureSize = 13;
	private NeuralNetwork network;
	private boolean isSink;

	/**
	 * Creates a GUI Anchor.
	 * 
	 * @param isSink
	 *            It is a Sink Node if true, Source Node if false
	 * @param network
	 *            Network to add Anchor to
	 * @param graphModel
	 *            Graph to insert Anchor into
	 * @param style
	 *            Style of Anchor
	 */
	public GUIAnchor(boolean isSink, NeuralNetwork network,
			IContainer graphModel, int style) {
		super(graphModel, style);
		this.isSink = isSink;
		this.setNode(network);
		this.setText(network.toString());
	}

	public void setNode(Node<?> network) {
		this.network = (NeuralNetwork) network;
	}

	public Node<?> getNode() {
		return network;
	}

	public boolean isSink() {
		return isSink;
	}

	public void createToolTip() {
		setTooltip(new Label(isSink() ? "Sink Node" : "Source Node"));
	}

	@Override
	/*
	 * Create the source figure. Just a black rectangle atm.
	 */
	protected IFigure createFigureForModel() {
		RectangleFigure figure = new RectangleFigure();
		figure.setBounds(new Rectangle(0, 0, figureSize, figureSize));
		figure.setBackgroundColor(ColorConstants.black);
		return figure;
	}

	/**
	 * Highlights the anchor node.
	 */
	public void highlight() {
		super.highlight();
		nodeFigure.setBackgroundColor(ColorConstants.gray);
	}

	/**
	 * Unhighlights the anchor node.
	 */
	public void unhighlight() {
		super.unhighlight();
		nodeFigure.setBackgroundColor(ColorConstants.black);
	}

}
