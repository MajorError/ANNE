package uk.ac.ic.doc.neuralnets.gui.modifier;

import uk.ac.ic.doc.neuralnets.gui.commands.AddOutputNodesCommand;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphItem;
import uk.ac.ic.doc.neuralnets.coreui.ZoomingInterfaceManager;
import uk.ac.ic.doc.neuralnets.graph.neural.io.OutputNode;
import uk.ac.ic.doc.neuralnets.util.plugins.PluginLoadException;
import uk.ac.ic.doc.neuralnets.util.plugins.PluginManager;

/**
 * Create the GUI for adding output nodes.
 * 
 * @author Peter Coetzee
 */
public class AddOutputNodesPanel extends Composite {

	private static final Logger log = Logger
			.getLogger(AddInputNodesPanel.class);

	public AddOutputNodesPanel(Composite parent, final ZoomingInterfaceManager<Graph,GraphItem> gm) {
		super(parent, SWT.NONE);
		setLayout(new FillLayout(SWT.VERTICAL));
		new Label(this, SWT.NONE).setText("Number of Output Nodes:");
		final Text numNodes = new Text(this, SWT.BORDER);
		numNodes.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				e.doit = e.text.matches("[0-9]*");
			}
		});
		numNodes.setText("0");

		try {
			final PluginManager pm = PluginManager.get();
			for (final String plg : pm.getPluginsOftype(OutputNode.class)) {
				Button b = new Button(this, SWT.PUSH);
				b.setText("Add " + plg);
				b.addSelectionListener(new SelectionListener() {

					public void widgetSelected(SelectionEvent e) {
						final int ns = Integer.parseInt(numNodes.getText());
                        gm.getCommandControl().addCommand( 
                                new AddOutputNodesCommand( ns, plg, gm ) );
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
