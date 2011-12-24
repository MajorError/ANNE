package uk.ac.ic.doc.neuralnets.gui.listeners;

import org.apache.log4j.Logger;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphItem;
import uk.ac.ic.doc.neuralnets.coreui.ZoomingInterfaceManager;
import uk.ac.ic.doc.neuralnets.graph.neural.NeuralNetwork;

/**
 * Creates a new network on widget selection
 * 
 * @author Fred van den Driessche
 *
 */
public class NewListener implements SelectionListener {
	static Logger log = Logger.getLogger(NewListener.class);

	private static NewListener instance;

	private Shell shell;
	private ZoomingInterfaceManager<Graph,GraphItem> gm;

	private NewListener(Shell shell, ZoomingInterfaceManager<Graph,GraphItem> gm) {
		this.shell = shell;
		this.gm = gm;
	}
	
	/**
	 * Get the listener instance
	 * @param shell - the parent shell
	 * @param gm - the graph manager
	 * @return the listener instance
	 */
	public static NewListener get(Shell shell, ZoomingInterfaceManager<Graph,GraphItem> gm){
		if(instance == null){
			instance = new NewListener(shell, gm);
		}
		return instance;
	}

	public void widgetDefaultSelected(SelectionEvent arg0) {
	}

	public void widgetSelected(SelectionEvent se) {
		log.info("Creating network");
		if(ContinueQuestion.ask(shell)){
			gm.setNetwork(new NeuralNetwork(), null);
			log.info("Created network");
		}else{
			log.info("Cancel create network");
		}
	}
}
