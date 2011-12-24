package uk.ac.ic.doc.neuralnets.gui.graph.listener;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphItem;
import org.eclipse.zest.core.widgets.GraphNode;

/**
 * 
 * @author Peter Coetzee
 */
public class MouseItemListener implements MouseListener {

	private Graph g;
	private GraphItem down;
	private int downX, downY;

	public MouseItemListener() {
		// no-op
	}

	public MouseItemListener(Graph g) {
		this.g = g;
	}

	public void setGraph(Graph g) {
		this.g = g;
	}

	public Graph getGraph() {
		return g;
	}

	protected IFigure getFigureAt(int x, int y) {
		Point point = new Point(x, y);
		g.getRootLayer().translateToRelative(point);
		return g.getFigureAt(point.x, point.y);
	}

	/**
	 * This could be hideously slow, in theory. We're iterating over all the
	 * nodes, then all the edges. However, experimentally it is faster than the
	 * GUI update for a given size of network.
	 * 
	 * We could store this data in a Map<IFigure,GraphItem>, but then there's a
	 * lot of housekeeping involved in keeping the map up to date - plus we end
	 * up with a big chunk of memory storing all the pointers again
	 */
	protected GraphItem getItemFor(IFigure figure) {
		for (Object o : g.getNodes())
			if (((GraphNode) o).getNodeFigure().equals(figure))
				return (GraphNode) o;
		for (Object o : g.getConnections())
			if (((GraphConnection) o).getConnectionFigure().equals(figure))
				return (GraphConnection) o;
		return null;
	}

	protected GraphItem getItemAt(int x, int y) {
		IFigure figureAtPoint = getFigureAt(x, y);
		if (figureAtPoint != null)
			return getItemFor(figureAtPoint);
		return null;
	}

	public void mouseDoubleClick(MouseEvent e) {
		handleDoubleClick(e, getItemAt(e.x, e.y));
	}

	public void mouseDown(MouseEvent e) {
		down = getItemAt(e.x, e.y);
		downX = e.x;
		downY = e.y;
		handleDown(e, down);
	}

	public void mouseUp(MouseEvent e) {
		GraphItem up = getItemAt(e.x, e.y);
		handleUp(e, up);
		if (down == up && Math.abs(downX - e.x) < 5
				&& Math.abs(downY - e.y) < 5)
			handleClick(e, up);
		down = null;
	}

	/**
	 * No operation!
	 * 
	 * @param e
	 * @param i
	 */
	protected void handleClick(MouseEvent e, GraphItem i) {
		// no-op
	}

	/**
	 * No Operation!
	 * 
	 * @param e
	 * @param i
	 */
	protected void handleDoubleClick(MouseEvent e, GraphItem i) {
		// no-op
	}

	/**
	 * No Operation!
	 * 
	 * @param e
	 * @param i
	 */
	protected void handleDown(MouseEvent e, GraphItem i) {
		// no-op
	}

	/**
	 * No Operation!
	 * 
	 * @param e
	 * @param i
	 */
	protected void handleUp(MouseEvent e, GraphItem i) {
		// no-op
	}
}
