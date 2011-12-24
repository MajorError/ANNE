package uk.ac.ic.doc.neuralnets.gui.listeners;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphItem;
import uk.ac.ic.doc.neuralnets.coreui.ZoomingInterfaceManager;

/**
 * Performs a redo action when selected
 * 
 * @author Fred van den Driessche
 *
 */
public class RedoListener implements SelectionListener{
	
	private static RedoListener instance;
	
	private ZoomingInterfaceManager<Graph,GraphItem> gm;
	
	private RedoListener(ZoomingInterfaceManager<Graph,GraphItem> gm){
		this.gm = gm;
	}

	/**
	 * Get the redo listener instance
	 * @param gm
	 * @return
	 */
	public static RedoListener get(ZoomingInterfaceManager<Graph,GraphItem> gm){
		if(instance == null){
			instance = new RedoListener(gm);
		}
		return instance;
	}
	
	public void widgetDefaultSelected(SelectionEvent arg0) {

	}

	public void widgetSelected(SelectionEvent arg0) {
		gm.getCommandControl().redo();
	}

}
