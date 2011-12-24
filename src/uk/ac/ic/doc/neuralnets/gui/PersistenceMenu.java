package uk.ac.ic.doc.neuralnets.gui;

import java.lang.reflect.Constructor;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import uk.ac.ic.doc.neuralnets.coreui.ZoomingInterfaceManager;
import uk.ac.ic.doc.neuralnets.gui.listeners.NewListener;
import uk.ac.ic.doc.neuralnets.gui.listeners.OpenListener;
import uk.ac.ic.doc.neuralnets.gui.listeners.SaveAsListener;
import uk.ac.ic.doc.neuralnets.gui.listeners.SaveListener;
import uk.ac.ic.doc.neuralnets.gui.ImageHandler;
import uk.ac.ic.doc.neuralnets.util.plugins.Plugin;
import uk.ac.ic.doc.neuralnets.util.plugins.PluginManager;

public class PersistenceMenu extends MenuPlugin {
	static Logger log = Logger.getLogger(PersistenceMenu.class);

	private GUIMenu menu;

	public void load(GUIMenu menu) {

		this.menu = menu;

		menu.addSubMenu("root", "&File");

		MenuItem newItem = menu.addMenuItem("&File", "New");
		newItem.addSelectionListener(NewListener.get(menu.getShell(), menu
				.getManager()));
		newItem.setImage(ImageHandler.get().getIcon("page"));
		newItem.setAccelerator(SWT.MOD1 + 'N');

		MenuItem openItem = menu.addMenuItem("&File", "Open...");
		openItem.addSelectionListener(new OpenListener(menu.getShell(),
				"Serialisation", menu.getManager(), true));
		openItem.setImage(ImageHandler.get().getIcon("folder_page"));
		openItem.setAccelerator(SWT.MOD1 + 'O');

		MenuItem saveItem = menu.addMenuItem("&File", "Save");
		saveItem.addSelectionListener(new SaveListener(menu.getShell(),
				"Serialisation", menu.getManager()));
		saveItem.setImage(ImageHandler.get().getIcon("disk"));
		saveItem.setAccelerator(SWT.MOD1 + 'S');

		MenuItem saveAsItem = menu.addMenuItem("&File", "Save As...");
		saveAsItem.addSelectionListener(new SaveAsListener(menu.getShell(),
				"Serialisation", menu.getManager()));
		saveAsItem.setImage(ImageHandler.get().getIcon("disk_multiple"));
		saveAsItem.setAccelerator(SWT.SHIFT + SWT.MOD1 + 'S');

		MenuItem insertItem = menu.addMenuItem("&File", "Insert From File");
		insertItem.addSelectionListener(new OpenListener(menu.getShell(),
				"Serialisation", menu.getManager(), false));
		insertItem.setImage(ImageHandler.get().getIcon("page_go"));
		insertItem.setAccelerator(SWT.SHIFT + SWT.MOD1 + 'O');

		/*
		 * menu.addMenuSeparator("&File");
		 * 
		 * MenuItem importItem = menu.addSubMenu("&File", "Import");
		 * createMenuForPlugins("Import", LoadService.class,
		 * OpenListener.class);
		 * 
		 * MenuItem exportItem = menu.addSubMenu("&File", "Export");
		 * createMenuForPlugins("Export", SaveService.class,
		 * ExportListener.class);
		 */
	}

	public String getName() {
		return "PersistenceMenu";
	}

	private <S extends SelectionListener> void createMenuForPlugins(
			String parent, Class<? extends Plugin> pluginClass,
			Class<S> listener) {
		try {
			Set<String> plugins = PluginManager.get().getPluginsOftype(
					pluginClass);
			for (String plugin : plugins) {
				MenuItem pluginItem = menu.addMenuItem(parent, "via " + plugin);
				Constructor<S> c = listener.getConstructor(new Class<?>[] {
						Shell.class, String.class,
						ZoomingInterfaceManager.class });
				SelectionListener l = c.newInstance(menu.getShell(), plugin,
						menu.getManager());
				pluginItem.addSelectionListener(l);
			}
		} catch (Exception e) {
			MenuItem error = menu.addMenuItem(parent,
					"Error loading Import plugins");
			error.setEnabled(false);
			log.error("Plugin load error", e);
		}
	}

	public int getPriority() {
		return 1;
	}

}
