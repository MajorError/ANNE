package uk.ac.ic.doc.neuralnets.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphItem;
import uk.ac.ic.doc.neuralnets.coreui.ZoomingInterfaceManager;
import uk.ac.ic.doc.neuralnets.gui.ToolbarPlugin;
import uk.ac.ic.doc.neuralnets.util.plugins.PluginLoadException;
import uk.ac.ic.doc.neuralnets.util.plugins.PluginManager;

/**
 * Constructs the application toolbar from <code>ToolbarPlugins</code>. The
 * toolbar is a collection of groups which can each contain a number of
 * buttons/controls.
 * 
 * @author Fred van den Driessche
 * 
 */
public class GUIToolbar {
	private static final Logger log = Logger.getLogger(GUIToolbar.class);

	private ZoomingInterfaceManager<Graph, GraphItem> gm;

	private CoolBar coolbar;
	private Map<String, CoolItem> coolitems;

	/**
	 * Creates the application toolbar by requesting <code>ToolbarPlugins</code>
	 * from the plugin manager.
	 * 
	 * @param coolbar
	 * @param gm
	 */
	public GUIToolbar(CoolBar coolbar,
			final ZoomingInterfaceManager<Graph, GraphItem> gm) {
		this.gm = gm;
		this.coolbar = coolbar;
		this.coolitems = new HashMap<String, CoolItem>();

		PluginManager pm = null;

		try {
			pm = PluginManager.get();
		} catch (PluginLoadException e) {
			log.error("Couldn't get plugin manager to load toolbar plugins", e);
			return;
		}

		// Instantiate the plugins and add to a sorted set.
		SortedSet<ToolbarPlugin> plugins = new TreeSet<ToolbarPlugin>();

		for (String plugin : pm.getPluginsOftype(ToolbarPlugin.class)) {
			try {
				ToolbarPlugin p = pm.getPlugin(plugin, ToolbarPlugin.class);
				plugins.add(p);
			} catch (PluginLoadException e) {
				log.error("Error loading toolbar plugin " + plugin, e);
				e.printStackTrace();
			}
		}

		// Actually add them to the toolbar.
		for (ToolbarPlugin plugin : plugins) {
			plugin.create(this);
		}
	}

	/**
	 * Add a new group to the toolbar.
	 * 
	 * @param name
	 *            - name of the new toolbar.
	 */
	public CoolItem addGroup(String name) {
		ToolBar toolBar = new ToolBar(coolbar, SWT.FLAT);
		CoolItem item = new CoolItem(coolbar, SWT.NONE);
		item.setControl(toolBar);
		coolitems.put(name, item);
		return item;
	}

	/**
	 * Add a button to a parent group with text
	 * 
	 * @param parent
	 *            - the name parent group
	 * @param name
	 *            - text to appear on the button
	 * @return - the new button
	 */
	public ToolItem addButton(String parent, String name) {
		return addButton(parent, name, SWT.NONE);
	}

	/**
	 * Add a radio/toggle button to a parent group.
	 * 
	 * @param parent
	 *            - the parent group
	 * @param name
	 *            - the button name
	 * @param type
	 *            - the button type SWT.CHECK/SWT.RADIO/SWT.SEPARATOR
	 * @return - the new button
	 */
	public ToolItem addButton(String parent, String name, int type) {
		ToolBar bar = getParentBar(parent);
		ToolItem ti = new ToolItem(bar, type);
		ti.setText(name);
		repackGroup(parent);
		return ti;
	}

	/**
	 * Add a button to a parent group with an icon.
	 * 
	 * @param parent
	 *            - the parent group.
	 * @param icon
	 *            - the icon <code>Image</code>.
	 * @return - the new button
	 */
	public ToolItem addButton(String parent, Image icon) {
		ToolBar bar = getParentBar(parent);
		ToolItem ti = new ToolItem(bar, SWT.NONE);
		ti.setImage(icon);
		repackGroup(parent);
		return ti;
	}

	/**
	 * Get the parent shell. Allows toolbar buttons to have listeners which
	 * create new shells.
	 * 
	 * @return - the toolbars parent shell
	 */
	public Shell getShell() {
		return coolbar.getShell();
	}

	/**
	 * Get the graph manager. Allows toolbar buttons to have listeners which
	 * modify the graph.
	 * 
	 * @return - the manager for the graph.
	 */
	public ZoomingInterfaceManager<Graph, GraphItem> getManager() {
		return gm;
	}

	/**
	 * Recalculate the size of the toolbar group
	 * 
	 * @param itemGroup
	 */
	public void repackGroup(String itemGroup) {
		CoolItem item = coolitems.get(itemGroup);

		if (item == null) {
			log.warn("No toolbar item " + itemGroup);
			return;
		}

		ToolBar bar = (ToolBar) item.getControl();

		Point p = bar.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		bar.setSize(p);

		Point p2 = item.computeSize(p.x, p.y);
		item.setSize(p2);

		bar.pack();
	}

	// Get the parent group from name
	private ToolBar getParentBar(String parent) {
		CoolItem item = coolitems.get(parent);
		if (item == null) {
			item = addGroup(parent);
		}
		ToolBar bar = (ToolBar) item.getControl();
		return bar;
	}
}