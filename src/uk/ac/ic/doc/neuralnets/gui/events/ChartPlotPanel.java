package uk.ac.ic.doc.neuralnets.gui.events;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import javax.swing.JPanel;
import org.apache.log4j.Logger;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.event.ChartChangeEvent;
import org.jfree.chart.event.ChartChangeListener;
import org.jfree.data.Range;

class ChartPlotPanel extends JPanel implements ChartChangeListener, KeyListener {

	public static final long serialVersionUID = 342095794287504l;

	private JFreeChart chart;
	private Semaphore mtx;
	private Logger log = Logger.getLogger(ChartPlotPanel.class);
	// scale in range [0,..], relative to window width
	// pos in range [0-1] strict, is % distance from upper bound
	private double scale = 1, pos = 0;
	// For keeping track of user defined upper and lower bounds
	private double u = -1, l = -1;

	public ChartPlotPanel(JFreeChart chart, Semaphore mtx) {
		this.chart = chart;
		this.mtx = mtx;
		chart.addChangeListener(this);
		addKeyListener(this);
	}

	public void scrollRight() {
		pos = Math.max(0, pos - 0.1);
		l = -1;
		updateRange();
	}

	public void scrollLeft() {
		pos = Math.min(1, pos + 0.1);
		l = -1;
		updateRange();
	}

	public void zoomOut() {
		scale *= 1.5;
		updateRange();
	}

	public void zoomIn() {
		scale /= 1.5;
		updateRange();
	}

	@Override
	public void update(Graphics g) {
		paint(g);
	}

	@Override
	public void paint(Graphics g) {
		try {
			if (mtx.tryAcquire(100, TimeUnit.MILLISECONDS)) {
				paintNow(g);
				mtx.release();
			}
		} catch (Exception e) {
			log.error("Rendering exception", e);
		}
	}

	private void paintNow(Graphics g) {
		BufferedImage bi = chart.createBufferedImage(getWidth(), getHeight());
		g.drawImage(bi, 0, 0, null);
	}

	public void chartChanged(ChartChangeEvent event) {
		repaint();
	}

	public void keyTyped(KeyEvent e) { // no-op
	}

	public void keyPressed(KeyEvent e) {
		log.trace(e);
		if (e.getKeyCode() == KeyEvent.VK_LEFT)
			scrollLeft();
		else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
			scrollRight();
		else if (e.getKeyCode() == KeyEvent.VK_UP)
			zoomIn();
		else if (e.getKeyCode() == KeyEvent.VK_DOWN)
			zoomOut();
	}

	public void keyReleased(KeyEvent e) {
		repaint();
	}

	private void updateRange() {
		try {
			if (mtx.tryAcquire(1000, TimeUnit.MILLISECONDS)) {
				Range r = chart.getXYPlot().getDataRange(
						chart.getXYPlot().getDomainAxis());
				if (r != null) {
					log.debug("Range: " + r + ", Scale: " + scale + ", Pos: "
							+ pos);
					// Keep lower bound constant if user put it there
					u = r.getUpperBound() - r.getLength() * pos;
					l = pos == 0 || l == -1 ? Math.max(r.getLowerBound(), u
							- getWidth() * scale) : l;
					chart.getXYPlot().getDomainAxis().setRange(l, u);
				}
				mtx.release();
				repaint();
			}
		} catch (InterruptedException ex) {
			// Thread was interrupted; try again
			updateRange();
		}
	}
}
