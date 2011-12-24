
package uk.ac.ic.doc.neuralnets.gui.modifier;

import uk.ac.ic.doc.neuralnets.gui.NetworkModifier;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphItem;
import uk.ac.ic.doc.neuralnets.coreui.ZoomingInterfaceManager;

/**
 * Network modifier for changing the graph layout algorithm
 *
 * @author Peter Coetzee
 */
public class LayoutGraphModifier extends NetworkModifier {
    
    public Composite getConfigurationGUI( Composite parent, ZoomingInterfaceManager<Graph,GraphItem> gm, ExpandItem ei ) {
        return new LayoutConfig( parent, gm );
    }

    public String getName() {
        return "LayoutGraph";
    }
    
    @Override
    public String toString() {
        return "Graph Layout";
    }

    @Override
    public int getPriority() {
        return 1;
    }

}
