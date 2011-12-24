package uk.ac.ic.doc.neuralnets.gui;

import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Listener;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphItem;
import uk.ac.ic.doc.neuralnets.coreui.ZoomingInterfaceManager;
import uk.ac.ic.doc.neuralnets.gui.NetworkModifier;
import uk.ac.ic.doc.neuralnets.gui.ImageHandler;
import uk.ac.ic.doc.neuralnets.util.plugins.PluginLoadException;
import uk.ac.ic.doc.neuralnets.util.plugins.PluginManager;

/**
 * Controls the Sidebar of the UI.
 * 
 * @author Peter Coetzee
 */
public class GUISideBar {

	private static final Logger log = Logger.getLogger(GUISideBar.class);
	private ZoomingInterfaceManager<Graph, GraphItem> gm;

	/**
	 * Create the Sidebar.
	 * 
	 * @param container
	 *            - sidebar container
	 * @param gm
	 *            - graph manager.
	 */
	public GUISideBar(final Composite container,
			ZoomingInterfaceManager<Graph, GraphItem> gm) {
		this.gm = gm;

		container.setLayout(new GridLayout(1, false));

		final CTabFolder sideFolder = new CTabFolder(container, SWT.BORDER
				| SWT.MULTI);

		sideFolder.setSimple(false);
		sideFolder.setTabHeight(24);
		sideFolder.setMinimizeVisible(true);

		sideFolder.setLayoutData(excludeData(false));

		/*
		 * Create tabs for Modify, Train and Run.
		 */
		CTabItem modify = new CTabItem(sideFolder, SWT.NONE);
		modify.setText(" Modify ");
		Composite c = new Composite(sideFolder, SWT.NONE);
		modify.setControl(c);
		createModifyTab(c);

		CTabItem training = new CTabItem(sideFolder, SWT.NONE);
		training.setText(" Training ");
		c = new Composite(sideFolder, SWT.NONE);
		training.setControl(c);
		createTrainingTab(c);

		CTabItem run = new CTabItem(sideFolder, SWT.NONE);
		run.setText(" Run ");
		c = new Composite(sideFolder, SWT.NONE);
		run.setControl(c);
		createRunTab(c);

		// Selects the Modify tab by default.
		sideFolder.setSelection(0);

		/*
		 * Create a toolbar to appear when the sidebar is minimised. When the
		 * button is click the sidebar is maximised.
		 */
		final ToolBar maximizeBar = new ToolBar(container, SWT.VERTICAL);
		ToolItem maximizeItem = new ToolItem(maximizeBar, SWT.NONE);
		maximizeItem.setImage(ImageHandler.get().getIcon("application_double"));

		maximizeItem.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent se) {
			}

			public void widgetSelected(SelectionEvent se) {
				log.trace("Maximize sidebar");
				// Send maximize message to the sidebar container.
				Event e = new Event();
				e.data = "Maximize";
				container.notifyListeners(SWT.Selection, e);

				// Hide the maximise, show sidebar
				maximizeBar.setVisible(false);
				maximizeBar.setLayoutData(excludeData(true));
				sideFolder.setLayoutData(excludeData(false));
				sideFolder.setVisible(true);

				// relayout.
				container.layout(false);
			}

		});
		// hide on startup.
		maximizeBar.setLayoutData(excludeData(true));
		maximizeBar.setVisible(false);

		// listener for minimising
		sideFolder.addCTabFolder2Listener(new CTabFolder2Listener() {
			public void close(CTabFolderEvent arg0) {
			}

			public void maximize(CTabFolderEvent arg0) {
			}

			public void minimize(CTabFolderEvent arg0) {
				log.trace("Minimize sidebar");

				// Send minimise event to sidebar container.
				Event e = new Event();
				e.data = "Minimize";
				e.width = 35;
				container.notifyListeners(SWT.Selection, e);

				// hide sidebar and show maximise.
				sideFolder.setVisible(false);
				sideFolder.setLayoutData(excludeData(true));
				maximizeBar.setLayoutData(excludeData(false));
				maximizeBar.setVisible(true);

				// re-lay out.
				container.layout(false);
			}

			public void restore(CTabFolderEvent arg0) {
			}

			public void showList(CTabFolderEvent arg0) {
			}
		});
	}

	// Creates default layout data to exclude a
	private GridData excludeData(boolean exclude) {
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.exclude = exclude;
		return gd;
	}

	// Loads the modifiers into the Modify tab.
	private void createModifyTab(Composite c) {
		c.setLayout(new FillLayout());
		ExpandBar bar = new ExpandBar(c, SWT.V_SCROLL);
		SortedSet<NetworkModifier> ms = new TreeSet<NetworkModifier>();
		try {
			// Get the relevant plugins
			for (String p : PluginManager.get().getPluginsOftype(
					NetworkModifier.class))
				ms.add(PluginManager.get().getPlugin(p, NetworkModifier.class));
			log.debug("Found " + ms.size() + " sections");

			// For each (possibly reverse-) sorted plugin create an expand tab.
			for (NetworkModifier nm : ms) {
				log.debug("Adding section for " + nm.getName());
				ExpandItem curr = new ExpandItem(bar, SWT.NONE, 0);
				curr.setText(nm.toString());

				Composite gui = nm.getConfigurationGUI(bar, gm, curr);
				curr.setHeight(gui.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
				curr.setControl(gui);
				log.trace("Loaded " + nm.getName());
			}
		} catch (PluginLoadException ex) {
			log.error("Bad NetworkModifier: " + ms, ex);
		}

		// Warn the user if there are no modifiers.
		if (bar.getItemCount() == 0) {
			ExpandItem errorItem = new ExpandItem(bar, SWT.NONE, 0);
			Label error = new Label(bar, SWT.NONE);
			error.setText("No sidebar items found!");
			errorItem.setHeight(error.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
			errorItem.setControl(error);
			errorItem.setText("Warning!");
		}

		bar.getItems()[0].setExpanded(true);
		bar.setSpacing(5);
	}

	private void createTrainingTab(Composite c) {
		new TrainingPanel(c, gm);
	}

	private void createRunTab(Composite c) {
		new RunPanel(c, gm);
	}

}
