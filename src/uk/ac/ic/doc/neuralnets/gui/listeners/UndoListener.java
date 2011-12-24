package uk.ac.ic.doc.neuralnets.gui.listeners;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphItem;
import uk.ac.ic.doc.neuralnets.coreui.ZoomingInterfaceManager;

/**
 * Performs an undo action when selected
 * 
 * @author Fred van den Driessche
 *
 */
public class UndoListener implements SelectionListener {
	
	private static UndoListener instance;
	
	private ZoomingInterfaceManager<Graph,GraphItem> gm;
	
	private UndoListener(ZoomingInterfaceManager<Graph,GraphItem> gm){
		this.gm = gm;
	}

	/**
	 * Get the listener instance
	 * @param gm - graph manager
	 * @return the instance
	 */
	public static UndoListener get(ZoomingInterfaceManager<Graph,GraphItem> gm){
		if(instance == null){
			instance = new UndoListener(gm);
		}
		return instance;
	}
	
	public void widgetDefaultSelected(SelectionEvent arg0) {

	}

	public void widgetSelected(SelectionEvent arg0) {
		gm.getCommandControl().undo();
	}

}
