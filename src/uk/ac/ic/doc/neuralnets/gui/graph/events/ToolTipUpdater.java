package uk.ac.ic.doc.neuralnets.gui.graph.events;

import uk.ac.ic.doc.neuralnets.events.*;
import org.eclipse.swt.widgets.Display;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphItem;
import uk.ac.ic.doc.neuralnets.coreui.ZoomingInterfaceManager;

/**
 *
 * @author Peter Coetzee
 */
public class ToolTipUpdater implements EventHandler {

    private ZoomingInterfaceManager<Graph,GraphItem> gm;
    
    public ToolTipUpdater( ZoomingInterfaceManager<Graph,GraphItem> gm ) {
        this.gm = gm;
    }
    
    public void handle( Event e ) {
        if ( !(e instanceof GraphUpdateEvent) )
            return;
        Display.getDefault().syncExec( new Runnable() {
            public void run() {
                gm.updateInterfaceHints();
            }
        } );
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
