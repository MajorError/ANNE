package uk.ac.ic.doc.neuralnets.gui;

import uk.ac.ic.doc.neuralnets.gui.GUIToolbar;
import uk.ac.ic.doc.neuralnets.util.plugins.PriorityPlugin;

/**
 * ToolbarPlugins add buttons to the application toolbar.
 * 
 * @see GUIToolbar
 * @author Fred van den Driessche
 * 
 */
public abstract class ToolbarPlugin extends PriorityPlugin {

	/**
	 * Create buttons to add to the toolbar.
	 * 
	 * For example: <code>
	 * 		toolbar.addItem("MyItem");
	 * 		toolbar.addButton("MyItem", "MyButton");
	 * </code>
	 * 
	 * @param toolbar
	 *            - the application toolbar to which to add buttons
	 */
	public abstract void create(GUIToolbar toolbar);

}
