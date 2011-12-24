package uk.ac.ic.doc.neuralnets.gui.modifier;

import uk.ac.ic.doc.neuralnets.gui.commands.AddInputNodesCommand;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;


import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphItem;
import uk.ac.ic.doc.neuralnets.coreui.ZoomingInterfaceManager;
import uk.ac.ic.doc.neuralnets.graph.neural.io.InputNode;
import uk.ac.ic.doc.neuralnets.util.plugins.PluginLoadException;
import uk.ac.ic.doc.neuralnets.util.plugins.PluginManager;

/**
 * Creates the UI for adding input nodes to the network.
 * 
 * @author Peter Coetzee
 */
public class AddInputNodesPanel extends Composite {

	private static final Logger log = Logger
			.getLogger(AddInputNodesPanel.class);
	
	/**
	 * Creates the SWT UI components
	 * @param parent - container
	 * @param gm - graph manager
	 */
	public AddInputNodesPanel(Composite parent, final ZoomingInterfaceManager<Graph,GraphItem> gm) {
		super(parent, SWT.NONE);
		setLayout(new FillLayout(SWT.VERTICAL));
		try {
			final PluginManager pm = PluginManager.get();
			for (final String plg : pm.getPluginsOftype(InputNode.class)) {
				Button b = new Button(this, SWT.PUSH);
				b.setText("Add " + plg);
				b.addSelectionListener(new SelectionListener() {

					public void widgetSelected(SelectionEvent e) {
                        gm.getCommandControl().addCommand(
                                new AddInputNodesCommand( plg, gm ) );
					}

					public void widgetDefaultSelected(SelectionEvent e) {
						widgetSelected(e);
					}
				});
			}
		} catch (PluginLoadException ex) {
			log.error("Couldn't get InputNodes", ex);
		}
	}

}
