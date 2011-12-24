package uk.ac.ic.doc.neuralnets.gui;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphItem;
import uk.ac.ic.doc.neuralnets.coreui.ZoomingInterfaceManager;
import uk.ac.ic.doc.neuralnets.util.plugins.PriorityPlugin;

/**
 * Network Modifiers are pluggable units in the Modify tab.
 * 
 * @see GUISidebar
 * @see ExpandItem
 * @see ExpandBar
 * 
 * @author Peter Coetzee
 */
public abstract class NetworkModifier extends PriorityPlugin {

	/**
	 * Create the UI for the unit, called during the initialization of the
	 * modify tab.
	 * 
	 * @param parent
	 *            - the expand bar for modifiers
	 * @param gm
	 *            - the graph manager
	 * @param ei
	 *            - the expand item for the modifier.
	 * @return composite containing the UI components for the modifier
	 */
	public abstract Composite getConfigurationGUI(Composite parent,
			ZoomingInterfaceManager<Graph, GraphItem> gm, ExpandItem ei);

	@Override
	public abstract String toString();

}
