package uk.ac.ic.doc.neuralnets.gui;

import uk.ac.ic.doc.neuralnets.gui.GUIMenu;
import uk.ac.ic.doc.neuralnets.util.plugins.PriorityPlugin;

/**
 * Menu plugins create the application menu structure. See GUIMenu for the
 * interface used to create menus.
 * 
 * @see GUIMenu
 * 
 * @author Fred van den Driessche
 * 
 */
public abstract class MenuPlugin extends PriorityPlugin {

	/**
	 * Creates the menu for the plugin.s
	 * 
	 * @param menu
	 */
	public abstract void load(GUIMenu menu);

}
