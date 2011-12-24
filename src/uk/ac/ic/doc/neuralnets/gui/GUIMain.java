package uk.ac.ic.doc.neuralnets.gui;

import java.io.File;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphItem;
import org.eclipse.zest.core.widgets.IContainer;

import uk.ac.ic.doc.neuralnets.coreui.ZoomingInterfaceManager;
import uk.ac.ic.doc.neuralnets.graph.neural.NeuralNetwork;
import uk.ac.ic.doc.neuralnets.persistence.FileSpecification;
import uk.ac.ic.doc.neuralnets.persistence.LoadException;
import uk.ac.ic.doc.neuralnets.persistence.LoadManager;
import uk.ac.ic.doc.neuralnets.util.configuration.ConfigurationManager;

/**
 * Bootstrap.
 * 
 * @author Fred van den Driessche
 * 
 */
public class GUIMain {

	static Logger log = Logger.getLogger(GUIMain.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Thread.currentThread().setName("SWT Display [main]");
		Display display = new Display();

		ConfigurationManager.configure();
		NeuralNetwork n = null;

		Shell shell = new Shell(display);

		GUILayout layout = new GUILayout(shell);

		Graph graph = new Graph(layout.getGraphContainer(), SWT.NONE);

		ZoomingInterfaceManager<Graph, GraphItem> zm;

		if (args.length > 0) {
			String load = System.getProperty("user.dir") + File.separator
					+ args[0];
			FileSpecification spec = new FileSpecification(load,
					"Serialisation");
			try {
				n = (NeuralNetwork) LoadManager.get().load(spec);
			} catch (LoadException e) {
				log.error("Failed to load network from " + args[0]);
				n = new NeuralNetwork();
				spec = null;
			} finally {
				zm = new GUIManager((IContainer) graph, n, spec);
			}
		} else {
			n = new NeuralNetwork();
			zm = new GUIManager((IContainer) graph, n);
		}

		// initialize the menu, log, toolbar and sidebar
		new GUIMenu(shell, zm);

		new GUILog(layout.getBottomContainer());

		new GUIToolbar(layout.getToolbar(), zm);

		new GUISideBar(layout.getSidebarContainer(), zm);

		shell.pack();
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
		System.exit(0);
	}

}
