package uk.ac.ic.doc.neuralnets.gui.graph.decorations;

import uk.ac.ic.doc.neuralnets.graph.neural.EdgeDecoration;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;

public class CircleDecoration extends EdgeDecoration<PolygonDecoration> {
	
	private static double SCALE = 0.2;
	private static int RADIUS = 30;
	private static int VERTICES = 30;
	private static Point CENTER = new Point(-20, 0);

	private PointList points;

	public CircleDecoration() {
		
	}

	public String getName() {
		return "Circle";
	}
	
    private PointList circlePoints(){
    	if(points == null){
        	PointList pl = new PointList();
        	for(int i = 0; i <= VERTICES; i++){
        		double x = CENTER.x + Math.cos(((2*Math.PI)/VERTICES)*i) * RADIUS;
        		double y = CENTER.y + Math.sin(((2*Math.PI)/VERTICES)*i) * RADIUS;
        		pl.addPoint((int) x, (int)y);
        	}
        	points = pl;
    	}
    	return points;
    }
    
	public PolygonDecoration getFigure() {
		PolygonDecoration pd = new PolygonDecoration();
		pd.setBackgroundColor(ColorConstants.white);
    	pd.setScale(SCALE, SCALE);
    	pd.setTemplate(circlePoints());
		return pd;
	}
}
