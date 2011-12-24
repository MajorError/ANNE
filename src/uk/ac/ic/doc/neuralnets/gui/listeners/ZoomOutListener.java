package uk.ac.ic.doc.neuralnets.gui.listeners;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphItem;
import uk.ac.ic.doc.neuralnets.coreui.ZoomingInterfaceManager;

/**
 * Zooms the graph out when selected
 * 
 * @author Fred van den Driessche
 *
 */
public class ZoomOutListener implements SelectionListener {
	
	private static ZoomOutListener instance;
	private ZoomingInterfaceManager<Graph,GraphItem> gm;
	
	private ZoomOutListener(ZoomingInterfaceManager<Graph,GraphItem> gm){
		this.gm = gm;
	}
	
	/**
	 * Get the listener instance
	 * 
	 * @param gm - the graph manager
	 * @return the listener instance
	 */
	public static ZoomOutListener get(ZoomingInterfaceManager<Graph,GraphItem> gm){
		if(instance == null){
			instance = new ZoomOutListener(gm);
		}
		return instance;
	}
	
	public void widgetDefaultSelected(SelectionEvent arg0) {
	
	}

	public void widgetSelected(SelectionEvent arg0) {
		gm.zoomOut();
	}

}
