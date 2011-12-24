package uk.ac.ic.doc.neuralnets.gui.events;

import uk.ac.ic.doc.neuralnets.events.*;
import java.util.concurrent.Semaphore;
import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultXYDataset;

/**
 * 
 * @author Peter Coetzee
 */
public class RealTimePlotStatistician implements EventHandler {

	private DoubleList xs = new DoubleList(), ys = new DoubleList();
	private Logger log = Logger.getLogger(RealTimePlotStatistician.class);
	private Semaphore mtx = new Semaphore(1);
	private DefaultXYDataset dataset;
	private JFreeChart chart;
	private ChartPlot plotContainer;
	private int minY = Integer.MAX_VALUE;
	private int maxY = Integer.MIN_VALUE;

	public RealTimePlotStatistician() {
		dataset = new DefaultXYDataset();
		chart = ChartFactory.createScatterPlot("Real-Time Data Plot", "Time",
				"Neurone", dataset, PlotOrientation.VERTICAL, false, true,
				false);
		EventManager.get().registerSynchro(GraphUpdateEvent.class, this);
	}

	public void handle(Event e) {
		if (e instanceof GraphUpdateEvent) {
			log.debug("Graph updated, clearing plot");
			xs.clear();
			ys.clear();
			if (plotContainer != null) {
				plotContainer.dispose();
				plotContainer = null;
			}
			return;
		}
		if (!(e instanceof NumericalEvent && (plotContainer == null || plotContainer
				.isOK())))
			return;
		if (plotContainer == null) // now that we have data, create chart
			plotContainer = new ChartPlot(chart, mtx);
		NumericalEvent n = (NumericalEvent) e;
		xs.add(n.get(0));
		ys.add(n.get(1));
		minY = Math.min(minY, (int) Math.floor(n.get(1)));
		maxY = Math.max(maxY, (int) Math.ceil(n.get(1)));
		flush(true);
		chart.getXYPlot().getRangeAxis().setRange(minY, maxY);
	}

	public void flush() {
		mtx.acquireUninterruptibly();
		flush(false);
		mtx.release();
		chart.fireChartChanged();
	}

	public void flush(boolean acquire) {
		double[][] data = new double[2][xs.size()];
		data[0] = xs.get();
		data[1] = ys.get();
		if (acquire && mtx.tryAcquire()) {
			dataset.addSeries("Values", data);
			mtx.release();
		}
	}

	public String getName() {
		return "RealTimePlot";
	}

	public boolean isValid() {
		return plotContainer == null ? true : plotContainer.isOK();
	}

	public class DoubleList {

		private double[] contents;
		private int ptr = 0;

		public DoubleList() {
			this(10);
		}

		public void clear() {
			ptr = 0;
			for (int i = 0; i < contents.length; i++)
				contents[i] = 0;
		}

		public DoubleList(int initialCapacity) {
			contents = new double[initialCapacity];
		}

		public double[] get() {
			return contents;
		}

		public void add(double d) {
			if (ptr == contents.length) {
				double[] newcontents = new double[contents.length * 2];
				System.arraycopy(contents, 0, newcontents, 0, contents.length);
				contents = newcontents;
			}
			contents[ptr++] = d;
		}

		public int size() {
			return ptr;
		}

	}

}