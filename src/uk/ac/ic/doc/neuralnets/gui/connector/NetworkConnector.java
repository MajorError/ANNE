package uk.ac.ic.doc.neuralnets.gui.connector;

import java.util.Collection;
import java.util.List;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphItem;
import uk.ac.ic.doc.neuralnets.coreui.ZoomingInterfaceManager;
import uk.ac.ic.doc.neuralnets.graph.Edge;
import uk.ac.ic.doc.neuralnets.graph.Node;
import uk.ac.ic.doc.neuralnets.util.plugins.Plugin;

/**
 *
 * @author Peter Coetzee
 */
public abstract class NetworkConnector implements Plugin {
    
    protected ZoomingInterfaceManager<Graph,GraphItem> gm;
    
    public NetworkConnector(){}
    
    public NetworkConnector( ZoomingInterfaceManager<Graph,GraphItem> gm ){
    	setGUIManager(gm);
    }
    
    public void setGUIManager( ZoomingInterfaceManager<Graph,GraphItem> gm ) {
        this.gm = gm;
    }
    
    public abstract Collection<Edge<Node<?>,Node<?>>> connect( List<Node<?>> nodes );
    
    public abstract Composite getConfigurationPanel( Composite parent );

}
