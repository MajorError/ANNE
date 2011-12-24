package uk.ac.ic.doc.neuralnets.gui;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MenuItem;

import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphItem;
import uk.ac.ic.doc.neuralnets.events.Event;
import uk.ac.ic.doc.neuralnets.events.EventHandler;
import uk.ac.ic.doc.neuralnets.events.EventManager;
import uk.ac.ic.doc.neuralnets.commands.CommandEvent;
import uk.ac.ic.doc.neuralnets.coreui.ZoomingInterfaceManager;
import uk.ac.ic.doc.neuralnets.gui.listeners.ZoomInListener;
import uk.ac.ic.doc.neuralnets.gui.listeners.ZoomOutListener;
import uk.ac.ic.doc.neuralnets.gui.ImageHandler;

public class ZoomMenu extends MenuPlugin implements EventHandler, Runnable {
	private static final Logger log = Logger.getLogger(ZoomMenu.class);
	
	private MenuItem zoomOutMenu;
	private MenuItem zoomInMenu;

	private ZoomingInterfaceManager<Graph,GraphItem> gm;
	
	public ZoomMenu(){
		EventManager.get().registerAsync(CommandEvent.class, this);
	}
	
	public void load(GUIMenu menu) {
		this.gm = menu.getManager();
		
		menu.addSubMenu("root", "&Edit");
		
		zoomInMenu = menu.addMenuItem("&Edit", "Zoom In");
		zoomInMenu.setImage(ImageHandler.get().getIcon("zoom_out"));
		zoomInMenu.addSelectionListener(ZoomInListener.get(gm));
		zoomInMenu.setEnabled(gm.canZoomIn());
		
		zoomOutMenu = menu.addMenuItem("&Edit", "Zoom Out");
		zoomOutMenu.setImage(ImageHandler.get().getIcon("zoom_in"));
		zoomOutMenu.addSelectionListener(ZoomOutListener.get(gm));
		zoomOutMenu.setEnabled(gm.canZoomOut());
	}

	public String getName() {
		return "ZoomMenu";
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
		zoomOutMenu.setEnabled(gm.canZoomOut());
		zoomInMenu.setEnabled(gm.canZoomIn());
	}

	public int getPriority() {
		return 3;
	}

}
