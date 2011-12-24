package uk.ac.ic.doc.neuralnets.gui.graph.listener;

import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphItem;
import uk.ac.ic.doc.neuralnets.coreui.ZoomingInterfaceManager;
import uk.ac.ic.doc.neuralnets.util.plugins.Plugin;

/**
 *
 * @author Peter Coetzee
 */
public abstract class MousePlugin extends MouseItemListener implements Plugin {
    
    protected ZoomingInterfaceManager<Graph,GraphItem> gm;
    
    public MousePlugin() {
        super( null );
    }
    
    public void setManager( ZoomingInterfaceManager<Graph,GraphItem> g ) {
        super.setGraph( g.getGraph() );
        gm = g;
    }

    public abstract String getName();

}
