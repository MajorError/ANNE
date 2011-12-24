package uk.ac.ic.doc.neuralnets.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MenuItem;

import uk.ac.ic.doc.neuralnets.gui.GUIMenu;
import uk.ac.ic.doc.neuralnets.gui.listeners.AboutListener;

public class HelpMenu extends MenuPlugin {

	public void load(GUIMenu menu) {
		menu.addSubMenu("root", "&Help");

		MenuItem aboutItem = menu.addMenuItem("&Help", "About");
		aboutItem.addSelectionListener(AboutListener.get(menu.getShell()));
		aboutItem.setAccelerator(SWT.MOD1 + 'A');
	}

	public String getName() {
		return "HelpMenu";
	}

	public int getPriority() {
		return 4;
	}

}
