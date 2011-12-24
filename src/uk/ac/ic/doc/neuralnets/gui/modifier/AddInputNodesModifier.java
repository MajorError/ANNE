
package uk.ac.ic.doc.neuralnets.gui.modifier;

import uk.ac.ic.doc.neuralnets.gui.NetworkModifier;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphItem;
import uk.ac.ic.doc.neuralnets.coreui.ZoomingInterfaceManager;

/**
 * NetworkModifier for adding input nodes to the network.
 * 
 * @author Peter Coetzee
 */
public class AddInputNodesModifier extends NetworkModifier {

    public Composite getConfigurationGUI( Composite parent, ZoomingInterfaceManager<Graph,GraphItem> gm, ExpandItem ei ) {
        return new AddInputNodesPanel( parent, gm );
    }

    public String getName() {
        return "AddInputNodes";
    }
    
    @Override
    public String toString() {
        return "Add Inputs to the Network";
    }

    @Override
    public int getPriority() {
        return 3;
    }

}
