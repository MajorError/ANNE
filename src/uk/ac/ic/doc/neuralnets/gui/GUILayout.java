package uk.ac.ic.doc.neuralnets.gui;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.swt.widgets.Shell;

/**
 * This class lays out the GUI skeleton in a given a shell giving access to the
 * main pane, side pane and bottom pane.
 * 
 * @author Fred van den Driessche
 * 
 */
public class GUILayout {

	static Logger log = Logger.getLogger(GUILayout.class);
	private Shell rootShell;

	// Accessible components
	private Composite sideContainer;
	private Composite graphContainer;
	private Composite bottomContainer;
	private CoolBar toolbar;

	// Private components
	private Sash sideSash;
	private Sash bottomSash;

	/**
	 * Adds layout containers to the shell.
	 * 
	 * @param shell
	 */
	public GUILayout(Shell shell) {
		this.rootShell = shell;
		createShell();
	}

	private void createShell() {

		GridLayout rootLayout = new GridLayout(3, false);
		rootLayout.horizontalSpacing = rootLayout.verticalSpacing = 1;
		rootLayout.marginHeight = rootLayout.marginWidth = 0;

		rootShell.setText("ANNE: the Artificial Neural Network Editor");
		rootShell.setLayout(rootLayout);
		rootShell.setSize(1024, 768);
		rootShell.setMinimumSize(800, 600);

		// Top toolbar, accessible through getToolbar.
		toolbar = new CoolBar(rootShell, SWT.NONE);

		// Container for the sidebar
		sideContainer = new Composite(rootShell, SWT.NONE);
		sideContainer.setLayout(new FillLayout());

		sideSash = new Sash(rootShell, SWT.VERTICAL);

		// Container for graphs.
		graphContainer = new Composite(rootShell, SWT.BORDER);
		graphContainer.setLayout(new FillLayout());

		bottomSash = new Sash(rootShell, SWT.HORIZONTAL);

		// Container for... bottom contents? (This might need a re-word...)
		bottomContainer = new Composite(rootShell, SWT.BORDER);

		// Handle layout... some of these numbers should be 'constantized'...
		GridData gdTb = new GridData(GridData.HORIZONTAL_ALIGN_FILL
				| GridData.GRAB_HORIZONTAL);
		gdTb.horizontalSpan = 3;

		GridData gdSb = new GridData(250, rootShell.getSize().y - 224);
		gdSb.grabExcessVerticalSpace = true;
		gdSb.verticalAlignment = SWT.FILL;

		GridData gdSSash = new GridData(GridData.VERTICAL_ALIGN_FILL
				| GridData.GRAB_VERTICAL);
		gdSSash.widthHint = 4;

		GridData gdGraph = new GridData(GridData.FILL_BOTH);

		GridData gdBSash = new GridData(GridData.HORIZONTAL_ALIGN_FILL
				| GridData.GRAB_HORIZONTAL);
		gdBSash.horizontalSpan = 3;
		gdBSash.heightHint = 4;

		GridData gdBb = new GridData(rootShell.getSize().x, 200);
		gdBb.horizontalSpan = 3;
		gdBb.grabExcessHorizontalSpace = true;
		gdBb.horizontalAlignment = SWT.FILL;

		toolbar.setLayoutData(gdTb);
		sideContainer.setLayoutData(gdSb);
		sideSash.setLayoutData(gdSSash);
		graphContainer.setLayoutData(gdGraph);
		bottomSash.setLayoutData(gdBSash);
		bottomContainer.setLayoutData(gdBb);

		sideContainer.addListener(SWT.Selection, new Listener() {

			private int width;

			public void handleEvent(Event e) {
				if (e.data.equals("Minimize")) {
					width = sideContainer.getSize().x;
					setSideContainerWidth(e.width);
					sideSash.setEnabled(false);
				}
				if (e.data.equals("Maximize")) {
					setSideContainerWidth(width);
					sideSash.setEnabled(true);
				}

			}

		});

		// Listener to resize the side container when sash is dragged.
		sideSash.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				Rectangle sashRect = sideSash.getBounds();
				Rectangle shellRect = rootShell.getClientArea();
				int right = shellRect.width - sashRect.width - 50;
				e.x = Math.max(Math.min(e.x, right), 50);
				if (e.x != sashRect.x) {
					setSideContainerWidth(e.x - 2);
				}
			}
		});

		// Listener to resize the bottom container when sash is dragged.
		bottomSash.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				Rectangle sashRect = bottomSash.getBounds();
				Rectangle shellRect = rootShell.getClientArea();
				int right = shellRect.height - sashRect.height - 50;
				e.y = Math.max(Math.min(e.y, right), 50);
				if (e.y != sashRect.y) {
					int w = sideContainer.getSize().x;

					GridData sideData = new GridData(w, e.y - 22);
					sideData.grabExcessVerticalSpace = true;
					sideData.verticalAlignment = SWT.FILL;
					sideContainer.setLayoutData(sideData);

					int h = rootShell.getSize().y - e.y - 20;
					GridData newData = new GridData(rootShell.getSize().x, h);
					newData.horizontalSpan = 3;
					newData.grabExcessHorizontalSpace = true;
					newData.horizontalAlignment = SWT.FILL;
					bottomContainer.setLayoutData(newData);
					rootShell.layout();
				}
			}
		});
	}

	private void setSideContainerWidth(int w) {
		log.trace("Adjusting sidecontainer width to " + w);
		int h = sideContainer.getSize().y;
		GridData d = new GridData(w, h);
		d.grabExcessVerticalSpace = true;
		d.verticalAlignment = SWT.FILL;
		sideContainer.setLayoutData(d);
		rootShell.layout();
	}

	/**
	 * Gets the main window pane
	 * 
	 * @return the Composite for the graph container
	 */
	public Composite getGraphContainer() {
		return graphContainer;
	}

	/**
	 * Gets the side pane
	 * 
	 * @return the Composite for the side container
	 */
	public Composite getSidebarContainer() {
		return sideContainer;
	}

	/**
	 * Get the bottom pane
	 * 
	 * @return the Composite for the bottom container
	 */
	public Composite getBottomContainer() {
		return bottomContainer;
	}

	/**
	 * Get the toolbar
	 * 
	 * @return the application toolbar as a CoolBar
	 */
	public CoolBar getToolbar() {
		return toolbar;
	}

}
