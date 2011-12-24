package uk.ac.ic.doc.neuralnets.gui.graph;

import org.apache.log4j.Logger;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;

import uk.ac.ic.doc.neuralnets.graph.Edge;
import uk.ac.ic.doc.neuralnets.graph.neural.EdgeDecoration;
import uk.ac.ic.doc.neuralnets.graph.neural.Neurone;
import uk.ac.ic.doc.neuralnets.graph.neural.Synapse;

/**
 * Represent a Synapse in the Zest graph.
 * 
 * @author Fred van den Driessche
 * 
 */
public class GUIEdge extends GraphConnection {

	private Edge<?, ?> edge;
	private Logger log = Logger.getLogger(GUIEdge.class);

	/**
	 * Creates a new edge in the specified graph for a Synapse. The edge
	 * decoration is set through the node specification, essentially ignoring
	 * the specified edge style.
	 * 
	 * @param edge
	 *            - the synapse to represent.
	 * @param graphModel
	 *            - the graph into which to insert the edge
	 * @param style
	 *            - the style of the edge (see ZestStyles) - this is overridden
	 * @param source
	 *            - the start point of the edge.
	 * @param destination
	 *            - the end point of the edge.
	 */
	public GUIEdge(Edge<?, ?> edge, Graph graphModel, int style,
			GraphNode source, GraphNode destination) {
		super(graphModel, style, source, destination);
		this.setEdge(edge);
		this.setDecoration();
	}

	public void createToolTip() {
		if (edge instanceof Synapse)
			setTooltip(new Label("Synapse of weight "
					+ ((Synapse) edge).getWeight()));
		else
			setTooltip(new Label(edge.toString()));
        if ( edge instanceof Synapse )
            setWeight( ((Synapse)edge).getWeight() );
	}

	/**
	 * Set the Synapse represented.
	 * 
	 * @param edge
	 *            - synapse to represent.
	 */
	public void setEdge(Edge<?, ?> edge) {
		this.edge = edge;
	}

	/**
	 * Get the Synapse represented.
	 * 
	 * @return the synapse edge.
	 */
	public Edge<?, ?> getEdge() {
		return edge;
	}

	// Retrieves the decoration from the source neurone.
	private void setDecoration() {
		EdgeDecoration<PolygonDecoration> d = ((Neurone) getEdge().getStart()).getEdgeDecoration();
		((PolylineConnection) getConnectionFigure()).setTargetDecoration(d
				.getFigure());
	}

	/*
	 * Ensure that the decoration is always the one specified, no matter the
	 * ZestStyles set. This is a hack due to the way Zest works.
	 */
	/**
	 * Highlight the edge.
	 */
	public void unhighlight() {
		super.unhighlight();
		setDecoration();
	}

	/*
	 * As above for unhighlight();
	 */
	/**
	 * Unhighlight the edge
	 */
	public void highlight() {
		super.highlight();
		setDecoration();
	}
}
