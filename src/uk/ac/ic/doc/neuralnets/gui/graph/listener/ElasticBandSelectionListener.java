package uk.ac.ic.doc.neuralnets.gui.graph.listener;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.zest.core.widgets.GraphItem;
import org.eclipse.zest.core.widgets.GraphNode;

public class ElasticBandSelectionListener extends MousePlugin implements
		MouseMoveListener {
	private Point start, end;
	private Rectangle rect;
	private RectangleFigure rectFigure;

	@Override
	public String getName() {
		return "ElasticBandSelection";
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void handleClick(MouseEvent e, GraphItem i) {
		// gm.getGraph().getSelection().clear();
		for (GraphNode gn : (List<GraphNode>) gm.getGraph().getNodes()) {
			gn.unhighlight();
		}
		if (i != null)
			i.highlight();
	}

	@Override
	protected void handleDown(MouseEvent e, GraphItem i) {
		if (i != null) {
			return;
		}

		start = new Point(e.x, e.y);
		gm.getGraph().addMouseMoveListener(this);

	}

	@Override
	protected void handleUp(MouseEvent e, GraphItem i) {
		if (start == null)
			return;

		gm.getGraph().removeMouseMoveListener(this);

		if (rectFigure != null) {
			gm.getGraph().getContents().remove(rectFigure);
			rectFigure = null;
		}

		for (GraphNode gn : (List<GraphNode>) gm.getGraph().getNodes()) {
			if (inBand(gn)) {
				gm.getGraph().getSelection().add(gn);
				gn.highlight();
			}
		}

		start = null;
	}

	public void mouseMove(MouseEvent e) {
		if (start != null) {
			if (rectFigure != null) {
				rect = new Rectangle(start, new Point(e.x, e.y));
				rectFigure.setBounds(rect);
			} else {
				rectFigure = new RectangleFigure();
				rect = new Rectangle(start, new Point(e.x, e.y));
				rectFigure.setFill(false);
				rectFigure.setLineStyle(Graphics.LINE_DOT);
				rectFigure.setBounds(rect);
				gm.getGraph().getContents().add(rectFigure);
			}
			if (rect != null) {
				for (GraphNode gn : (List<GraphNode>) gm.getGraph().getNodes()) {
					if (inBand(gn)) {
						gn.highlight();
					} else {
						gn.unhighlight();
					}
				}
			}
		}
	}

	private boolean inBand(GraphNode gn) {
		Point p = gn.getLocation();
		Logger.getLogger(getClass()).debug(p);
		return rect != null
				&& rect.contains(p.x + gn.getSize().width / 2, p.y
						+ gn.getSize().height / 2);
	}

}
