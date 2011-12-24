package uk.ac.ic.doc.neuralnets.gui;

import org.eclipse.swt.widgets.ToolItem;

import uk.ac.ic.doc.neuralnets.gui.GUIToolbar;
import uk.ac.ic.doc.neuralnets.gui.listeners.NewListener;
import uk.ac.ic.doc.neuralnets.gui.listeners.OpenListener;
import uk.ac.ic.doc.neuralnets.gui.listeners.SaveListener;
import uk.ac.ic.doc.neuralnets.gui.ImageHandler;

public class PersistenceToolbar extends ToolbarPlugin {

	public String getName() {
		return "PersistenceToolbar";
	}

	public void create(GUIToolbar toolbar) {
		toolbar.addGroup("Document");
		ToolItem newDoc = toolbar.addButton("Document", ImageHandler.get()
				.getIcon("page"));
		newDoc.setToolTipText("New");
		newDoc.setEnabled(true);
		newDoc.addSelectionListener(NewListener.get(toolbar.getShell(), toolbar
				.getManager()));

		ToolItem openDoc = toolbar.addButton("Document", ImageHandler.get()
				.getIcon("folder_page"));
		openDoc.setToolTipText("Open");
		openDoc.setEnabled(true);
		openDoc.addSelectionListener(new OpenListener(toolbar.getShell(),
				"Serialisation", toolbar.getManager(), true));

		ToolItem saveDoc = toolbar.addButton("Document", ImageHandler.get()
				.getIcon("disk"));
		saveDoc.setToolTipText("Save");
		saveDoc.setEnabled(true);
		saveDoc.addSelectionListener(new SaveListener(toolbar.getShell(),
				"Serialisation", toolbar.getManager()));
	}

	@Override
	public int getPriority() {
		return 1;
	}

}
