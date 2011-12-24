package uk.ac.ic.doc.neuralnets.gui.events;

import java.awt.Color;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.Semaphore;
import javax.swing.JFrame;
import org.apache.log4j.Logger;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

public class ChartPlot extends JFrame {

	public static final long serialVersionUID = -1875047310534L;

	private JFreeChart chart;
	private Logger log = Logger.getLogger(ChartPlot.class);
	private Semaphore mtx; // Mutex to synchronise dataset access
	private boolean ok = true;

	public ChartPlot(JFreeChart chart, Semaphore mtx) {
		this.chart = chart;
		this.mtx = mtx;
		initComponents();
	}

	public ChartPlot(JFreeChart chart) {
		this(chart, new Semaphore(1)); // no concurrency
	}

	private void initComponents() {
		setSize(640, 480);
		// Set up the chart's visual appearance
		XYPlot p = chart.getXYPlot();
		p.setRenderer(new XYLineAndShapeRenderer(false, true) {

			@Override
			public Shape getItemShape(int row, int col) {
				Shape s = new Rectangle(2, 2);
				return s;
			}

			@Override
			public Paint getItemFillPaint(int row, int col) {
				return Color.BLUE;
			}

			@Override
			public Paint getItemPaint(int row, int col) {
				return getItemFillPaint(row, col);
			}

		});
		p.setNoDataMessage("No data yet...");

		// Prepare the container for the chart
		ChartPlotPanel pa = new ChartPlotPanel(chart, mtx);
		add(pa);
		addKeyListener(pa);

		validate();
		setVisible(true);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				ok = false;
			}
		});
	}

	public boolean isOK() {
		return ok;
	}

}
