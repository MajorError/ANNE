package uk.ac.ic.doc.neuralnets.gui.graph;

import org.eclipse.draw2d.Label;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;

import uk.ac.ic.doc.neuralnets.graph.neural.NetworkBridge;

/**
 * Connection between two GUI Networks containing links connecting nodes between
 * each network
 * 
 * @author Ismail
 */

public class GUIBridge extends GraphConnection {

	private NetworkBridge bridge;

	/**
	 * Create GUI Bridge that connects two GUI Networks in the UI.
	 * 
	 * @param bridge
	 *            Network Bridge between the neural networks
	 * @param graphModel
	 *            Graph that the bridge is inserted into
	 * @param style
	 *            Style of edge
	 * @param source
	 *            Start point of bridge
	 * @param destination
	 *            End point of bridge
	 */
	public GUIBridge(NetworkBridge bridge, Graph graphModel, int style,
			GraphNode source, GraphNode destination) {
		super(graphModel, style, source, destination);
		this.setLineWidth(2);
		this.setBridge(bridge);
	}

	public void createToolTip() {
		setTooltip(new Label("Network Bridge with " + bridge.getBundle().size()
				+ " edges within."));
	}

	public NetworkBridge getBridge() {
		return bridge;
	}

	public void setBridge(NetworkBridge bridge) {
		this.bridge = bridge;
	}

}
