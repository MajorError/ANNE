package uk.ac.ic.doc.neuralnets.gui;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ToolItem;

import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphItem;
import uk.ac.ic.doc.neuralnets.events.Event;
import uk.ac.ic.doc.neuralnets.events.EventHandler;
import uk.ac.ic.doc.neuralnets.events.EventManager;
import uk.ac.ic.doc.neuralnets.gui.GUIToolbar;
import uk.ac.ic.doc.neuralnets.commands.CommandEvent;
import uk.ac.ic.doc.neuralnets.coreui.ZoomingInterfaceManager;
import uk.ac.ic.doc.neuralnets.gui.listeners.ZoomInListener;
import uk.ac.ic.doc.neuralnets.gui.listeners.ZoomOutListener;
import uk.ac.ic.doc.neuralnets.gui.ImageHandler;

/**
 * Create the buttons for the zooming.
 * @author Ismail
 *
 */
public class ZoomToolbar extends ToolbarPlugin implements EventHandler, Runnable {
	private static final Logger log = Logger.getLogger(ZoomToolbar.class);
	
	private ZoomingInterfaceManager<Graph,GraphItem> gm;
	private ToolItem zoomOutTool;
	private ToolItem zoomInTool;
	
	public ZoomToolbar(){
		EventManager.get().registerAsync(CommandEvent.class, this);
	}

	public void create(GUIToolbar toolbar) {
		this.gm = toolbar.getManager();
		toolbar.addGroup("Zoom");
		zoomInTool = toolbar.addButton("Zoom", ImageHandler.get().getIcon(
				"zoom_in"));
		zoomInTool.setToolTipText("Zoom In");
		zoomInTool.setEnabled(gm.canZoomIn());
		zoomInTool.addSelectionListener(ZoomInListener.get(gm));
		
		zoomOutTool = toolbar.addButton("Zoom", ImageHandler.get().getIcon(
				"zoom_out"));
		zoomOutTool.setToolTipText("Zoom out");
		zoomOutTool.setEnabled(gm.canZoomOut());
		zoomOutTool.addSelectionListener(ZoomOutListener.get(gm));
	}
	
	public String getName() {
		return "ZoomToolbar";
	}

	public void flush() {
	}

	public void handle(Event e) {
		if(e instanceof CommandEvent) {
			Display.getDefault().asyncExec(this);
		}			
	}

	public boolean isValid() {
		return true;
	}

	public void run() {
		zoomOutTool.setEnabled(gm.canZoomOut());
		zoomInTool.setEnabled(gm.canZoomIn());
	}

	@Override
	public int getPriority() {
		return 2;
	}

}
