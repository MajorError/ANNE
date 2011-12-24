package uk.ac.ic.doc.neuralnets.gui.graph.events;

import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphItem;
import uk.ac.ic.doc.neuralnets.coreui.ZoomingInterfaceManager;
import uk.ac.ic.doc.neuralnets.events.*;

/**
 *
 * @author Peter Coetzee
 */
public class NodeLocationUpdater implements EventHandler {

    private ZoomingInterfaceManager<Graph,GraphItem> gm;
    
    public NodeLocationUpdater( ZoomingInterfaceManager<Graph,GraphItem> gm ) {
        this.gm = gm;
    }
    
    public void handle( Event e ) {
        if ( !(e instanceof GraphUpdateEvent) )
            return;
        gm.persistLocations();
    }

    public void flush() {
        // no-op
    }

    public boolean isValid() {
        return true;
    }

    public String getName() {
        return "NodeLocationUpdater";
    }

}
