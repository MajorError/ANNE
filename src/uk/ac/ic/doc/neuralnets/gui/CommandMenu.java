package uk.ac.ic.doc.neuralnets.gui;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.MenuItem;

import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphItem;
import uk.ac.ic.doc.neuralnets.events.Event;
import uk.ac.ic.doc.neuralnets.events.EventHandler;
import uk.ac.ic.doc.neuralnets.events.EventManager;
import uk.ac.ic.doc.neuralnets.commands.CommandEvent;
import uk.ac.ic.doc.neuralnets.coreui.ZoomingInterfaceManager;
import uk.ac.ic.doc.neuralnets.gui.listeners.RedoListener;
import uk.ac.ic.doc.neuralnets.gui.listeners.UndoListener;
import uk.ac.ic.doc.neuralnets.gui.ImageHandler;

public class CommandMenu extends MenuPlugin implements EventHandler, Runnable {
	static Logger log = Logger.getLogger(CommandMenu.class);

	private MenuItem undo;
	private MenuItem redo;
	private ZoomingInterfaceManager<Graph, GraphItem> gm;

	@Override
	public void load(GUIMenu menu) {
		this.gm = menu.getManager();

		menu.addSubMenu("root", "&Edit");

		undo = menu.addMenuItem("&Edit", "Undo");
		undo.setImage(ImageHandler.get().getIcon("arrow_undo"));
		undo.setEnabled(gm.getCommandControl().canUndo());
		undo.addSelectionListener(UndoListener.get(gm));

		redo = menu.addMenuItem("&Edit", "Redo");
		redo.setImage(ImageHandler.get().getIcon("arrow_redo"));
		redo.setEnabled(gm.getCommandControl().canRedo());
		redo.addSelectionListener(RedoListener.get(gm));

		menu.addMenuSeparator("&Edit");

		EventManager.get().registerAsync(CommandEvent.class, this);
	}

	@Override
	public int getPriority() {
		return 2;
	}

	public String getName() {
		return "CommandMenu";
	}

	public void flush() {

	}

	public void handle(Event e) {
		if (e instanceof CommandEvent) {
			undo.getDisplay().asyncExec(this);
		}
	}

	public boolean isValid() {
		return true;
	}

	public void run() {
		undo.setEnabled(gm.getCommandControl().canUndo());
		redo.setEnabled(gm.getCommandControl().canRedo());
	}

}
