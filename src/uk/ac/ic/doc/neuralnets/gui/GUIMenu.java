package uk.ac.ic.doc.neuralnets.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphItem;
import uk.ac.ic.doc.neuralnets.coreui.ZoomingInterfaceManager;
import uk.ac.ic.doc.neuralnets.util.plugins.PluginLoadException;
import uk.ac.ic.doc.neuralnets.util.plugins.PluginManager;

/**
 * Constructs the application menu. Looks for <code>MenuPlugins</code>, sorts
 * them according to priority, then loads them into the menu.
 * 
 * @author Fred van den Driessche
 * 
 */
public class GUIMenu {
	static Logger log = Logger.getLogger(GUIMenu.class);

	private ZoomingInterfaceManager<Graph, GraphItem> gm;
	private Shell shell;

	private Map<String, Menu> menus;

	/**
	 * Creates the application menu by requesting <code>MenuPlugins</code> from
	 * the PluginManager.
	 * 
	 * @see PluginManager
	 * @param rootShell
	 *            - the shell the menu is for
	 * @param gm
	 *            - the graph manager.
	 */
	public GUIMenu(Shell rootShell, ZoomingInterfaceManager<Graph, GraphItem> gm) {
		this.gm = gm;
		this.shell = rootShell;
		this.menus = new HashMap<String, Menu>();

		// Create the root menu.
		Menu bar = new Menu(rootShell, SWT.BAR);
		rootShell.setMenuBar(bar);
		menus.put("root", bar);

		// Get the plugin manager, can't get it, can't continue.
		PluginManager pm;

		try {
			pm = PluginManager.get();
		} catch (PluginLoadException ple) {
			log.error("Couldn't get plugin manager to load menu plugins", ple);
			return;
		}

		SortedSet<MenuPlugin> menuPlugins = new TreeSet<MenuPlugin>();

		// Instantiate all the plugins and put them in a sorted set.
		// They're priority plugins so this will order them correctly.
		for (String plugin : pm.getPluginsOftype(MenuPlugin.class)) {
			try {
				MenuPlugin p = pm.getPlugin(plugin, MenuPlugin.class);
				menuPlugins.add(p);
			} catch (PluginLoadException ple) {
				log.error("Error loading menu plugin " + plugin, ple);
			}
		}

		// Load the ordered plugins.
		for (MenuPlugin plugin : menuPlugins) {
			plugin.load(this);
		}
	}

	/**
	 * Adds a menu item to the parent menu and connects an empty menu to it. The
	 * highest level menu is <b>"root"</b> which is automatically created.
	 * 
	 * @param parent
	 *            - name of the parent menu, e.g. "root", if the parent menu is
	 *            not found then the root menu will be used.
	 * @param name
	 *            - name of the new submenu
	 * @return <code>MenuItem</code> for the new submenu, if the submenu already
	 *         exists then that <code>MenuItem</code> is returned.
	 */
	public MenuItem addSubMenu(String parent, String name) {
		log.info("Adding submenu " + name + " to " + parent);

		if (menus.containsKey(name)) { // If menu already exists get its item
			log.info("Submenu " + name + " already exists!");
			return menus.get(name).getParentItem();
		}

		Menu parentMenu = getParentMenu(parent);

		MenuItem item = new MenuItem(parentMenu, SWT.CASCADE);
		item.setText(name);

		Menu menu = new Menu(shell, SWT.DROP_DOWN);
		item.setMenu(menu);

		menus.put(name, menu);

		return item;
	}

	/**
	 * Adds a named menu item to a parent menu
	 * 
	 * @param parent
	 *            - the menu to add the item to. If the parent menu isn't found
	 *            then the root menu is used.
	 * @param name
	 *            - the name for the new menu item.
	 * @return the newly created <code>MenuItem</code>
	 */
	public MenuItem addMenuItem(String parent, String name) {
		Menu parentMenu = getParentMenu(parent);

		MenuItem m = new MenuItem(parentMenu, SWT.PUSH);
		m.setText(name);
		return m;
	}

	/**
	 * Add a separator to parent menu
	 * 
	 * @param parent
	 *            - menu to separate
	 */
	public void addMenuSeparator(String parent) {
		new MenuItem(getParentMenu(parent), SWT.SEPARATOR);
	}

	/**
	 * Get the parent shell of the menu.
	 * 
	 * @return the main program shell
	 */
	public Shell getShell() {
		return shell;
	}

	/**
	 * Get the graph manager.
	 * 
	 * @return the ZoomingInterfaceManager for the graph.
	 */
	public ZoomingInterfaceManager<Graph, GraphItem> getManager() {
		return gm;
	}

	/*
	 * Retrieves the parent menu from cache. If it's not found it's created as a
	 * child of the root menu.
	 */
	private Menu getParentMenu(String name) {
		Menu parentMenu = menus.get(name);

		if (parentMenu == null) {
			log.warn("Parent menu " + name + " not found, adding to root");
			parentMenu = addSubMenu("root", name).getMenu();
		}

		return parentMenu;
	}

}
