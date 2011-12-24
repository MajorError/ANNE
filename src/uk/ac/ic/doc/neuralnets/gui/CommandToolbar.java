package uk.ac.ic.doc.neuralnets.gui;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.ToolItem;

import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphItem;
import uk.ac.ic.doc.neuralnets.events.Event;
import uk.ac.ic.doc.neuralnets.events.EventHandler;
import uk.ac.ic.doc.neuralnets.events.EventManager;
import uk.ac.ic.doc.neuralnets.gui.GUIToolbar;
import uk.ac.ic.doc.neuralnets.commands.CommandEvent;
import uk.ac.ic.doc.neuralnets.coreui.ZoomingInterfaceManager;
import uk.ac.ic.doc.neuralnets.gui.listeners.RedoListener;
import uk.ac.ic.doc.neuralnets.gui.listeners.UndoListener;
import uk.ac.ic.doc.neuralnets.gui.ImageHandler;

public class CommandToolbar extends ToolbarPlugin implements EventHandler,
		Runnable {
	private static final Logger log = Logger.getLogger(CommandToolbar.class);
	private ZoomingInterfaceManager<Graph, GraphItem> gm;
	private ToolItem undo;
	private ToolItem redo;

	public void create(GUIToolbar toolbar) {
		this.gm = toolbar.getManager();

		toolbar.addGroup("Commands");
		undo = toolbar.addButton("Commands", ImageHandler.get().getIcon(
				"arrow_undo"));
		undo.setToolTipText("Undo");
		redo = toolbar.addButton("Commands", ImageHandler.get().getIcon(
				"arrow_redo"));
		redo.setToolTipText("Redo");
		undo.setEnabled(toolbar.getManager().getCommandControl().canUndo());
		redo.setEnabled(toolbar.getManager().getCommandControl().canRedo());
		undo.addSelectionListener(UndoListener.get(toolbar.getManager()));
		redo.addSelectionListener(RedoListener.get(toolbar.getManager()));

		EventManager.get().registerAsync(CommandEvent.class, this);
	}

	public String getName() {
		return "CommandToolbar";
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

	@Override
	public int getPriority() {
		return 3;
	}

}
