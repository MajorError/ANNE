package uk.ac.ic.doc.neuralnets.gui;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.MenuItem;

import uk.ac.ic.doc.neuralnets.gui.GUIMenu;
import uk.ac.ic.doc.neuralnets.gui.listeners.ExitListener;

public class DefaultMenu extends MenuPlugin {
	static Logger log = Logger.getLogger(DefaultMenu.class);

	public String getName() {
		return "DefaultMenu";
	}

	public void load(GUIMenu menu) {

		menu.addSubMenu("root", "&File");

		MenuItem exitItem = menu.addMenuItem("&File", "Close...");
		exitItem.addSelectionListener(ExitListener.get(menu.getShell()));
	}

	public int getPriority() {
		return 2;
	}

}
