package uk.ac.ic.doc.neuralnets.gui.graph.listener;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.zest.core.widgets.GraphItem;

import uk.ac.ic.doc.neuralnets.events.EventManager;
import uk.ac.ic.doc.neuralnets.commands.CommandEvent;

/**
 * Sends a command event to the EventManager to inform zoom buttons whether to
 * be enabled or not
 * 
 * @author Ismail
 * 
 */
public class ZoomListener extends MousePlugin {

	public ZoomListener() {
		super();
	}

	protected void handleUp(MouseEvent e, GraphItem i) {
		EventManager.get().fire(new CommandEvent());
	}

	@Override
	public String getName() {
		return "Zoom";
	}
}