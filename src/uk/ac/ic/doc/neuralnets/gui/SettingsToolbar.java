package uk.ac.ic.doc.neuralnets.gui;

import org.eclipse.swt.widgets.ToolItem;

import uk.ac.ic.doc.neuralnets.gui.GUIToolbar;
import uk.ac.ic.doc.neuralnets.gui.listeners.NeuroneDesignerListener;

public class SettingsToolbar extends ToolbarPlugin {

	public void create(GUIToolbar toolbar) {
		toolbar.addGroup("Settings");
		ToolItem neuroneDesigner = toolbar.addButton("Settings",
				"Neurone Designer");
		neuroneDesigner.addSelectionListener(NeuroneDesignerListener.get());
	}

	public String getName() {
		return "SettingsToolbar";
	}

	@Override
	public int getPriority() {
		return 4;
	}

}
