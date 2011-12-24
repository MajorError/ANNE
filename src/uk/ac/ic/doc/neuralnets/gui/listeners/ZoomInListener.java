package uk.ac.ic.doc.neuralnets.gui.listeners;

import java.util.List;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.zest.core.widgets.Graph;

import org.eclipse.zest.core.widgets.GraphItem;
import uk.ac.ic.doc.neuralnets.coreui.ZoomingInterfaceManager;
import uk.ac.ic.doc.neuralnets.graph.neural.NeuralNetwork;
import uk.ac.ic.doc.neuralnets.gui.graph.GUINetwork;

/**
 * Zooms the graph in when selected
 * 
 * @author Fred van den Driessche
 *
 */
public class ZoomInListener implements SelectionListener {
	
	private static ZoomInListener instance;
	private ZoomingInterfaceManager<Graph,GraphItem> gm;

	public ZoomInListener(ZoomingInterfaceManager<Graph,GraphItem> gm) {
		this.gm = gm;
	}

	/**
	 * Get the listener instance
	 * @param gm - the graph manager
	 * @return the listener instance
	 */
	public static ZoomInListener get(ZoomingInterfaceManager<Graph,GraphItem> gm){
		if(instance == null){
			instance = new ZoomInListener(gm);
		}
		return instance;
	}

	public void widgetDefaultSelected(SelectionEvent arg0) {
		// TODO Auto-generated method stub
	}

	@SuppressWarnings("unchecked")
	public void widgetSelected(SelectionEvent arg0) {
		Graph g = gm.getGraph();
		List<Object> selection = g.getSelection();
		if(selection.size() == 1 && selection.get(0) instanceof GUINetwork){
			gm.zoomIn((NeuralNetwork) ((GUINetwork) selection.get(0))
					.getNode());
		}
	}
}
