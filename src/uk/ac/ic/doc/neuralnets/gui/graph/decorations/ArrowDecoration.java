package uk.ac.ic.doc.neuralnets.gui.graph.decorations;

import uk.ac.ic.doc.neuralnets.graph.neural.EdgeDecoration;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.PolygonDecoration;

public class ArrowDecoration extends EdgeDecoration<PolygonDecoration> {

	public ArrowDecoration() {
	}

	@Override
	public String getName() {
		return "Arrow";
	}

	@Override
	public PolygonDecoration getFigure() {
		PolygonDecoration pd = new PolygonDecoration();
		pd.setScale(8, 6);
		pd.setBackgroundColor(ColorConstants.white);
		pd.setTemplate(PolygonDecoration.TRIANGLE_TIP);
		return pd;
	}

}
