package uk.ac.ic.doc.neuralnets.gui.events;

import uk.ac.ic.doc.neuralnets.events.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultXYDataset;

/**
 * 
 * @author Peter Coetzee
 */
public class GraphPlotStatistician implements EventHandler {

	private List<Point2D.Double> values = new ArrayList<Point2D.Double>();
	private Logger log = Logger.getLogger(GraphPlotStatistician.class);

	public void handle(Event e) {
		if (!(e instanceof NumericalEvent))
			return;
		NumericalEvent n = (NumericalEvent) e;
		values.add(new Point2D.Double(n.get(0), n.get(1)));
	}

	public void flush() {
		if (values.size() == 0) {
			log.info("No values to plot in GraphPlot Statistician");
			return;
		}
		double[][] data = new double[2][values.size()];
		for (int i = 0; i < values.size(); i++) {
			Point2D.Double p = values.get(i);
			data[0][i] = p.getX();
			data[1][i] = p.getY();
		}
		DefaultXYDataset dataset = new DefaultXYDataset();
		dataset.addSeries("Values", data);
		JFreeChart chart = ChartFactory
				.createScatterPlot("Graph Data Plot", "X", "Y", dataset,
						PlotOrientation.VERTICAL, false, true, false);
		new ChartPlot(chart);
	}

	public String getName() {
		return "GraphPlot";
	}

	public boolean isValid() {
		return true;
	}

}