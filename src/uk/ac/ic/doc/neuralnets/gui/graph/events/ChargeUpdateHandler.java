
package uk.ac.ic.doc.neuralnets.gui.graph.events;

import uk.ac.ic.doc.neuralnets.gui.graph.*;
import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Display;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphItem;
import uk.ac.ic.doc.neuralnets.coreui.ZoomingInterfaceManager;
import uk.ac.ic.doc.neuralnets.events.Event;
import uk.ac.ic.doc.neuralnets.events.EventHandler;
import uk.ac.ic.doc.neuralnets.graph.neural.NodeChargeUpdateEvent;
import uk.ac.ic.doc.neuralnets.graph.neural.Neurone;

/**
 *
 * @author Peter Coetzee
 */
public class ChargeUpdateHandler implements EventHandler {
    
    private ZoomingInterfaceManager<Graph,GraphItem> manager;
    private Logger log = Logger.getLogger( ChargeUpdateHandler.class );
    
    public ChargeUpdateHandler() {
        // no-op
    }
    
    public ChargeUpdateHandler( ZoomingInterfaceManager<Graph,GraphItem> m ) {
        setGUIManager( m );
    }
    
    public void setGUIManager( ZoomingInterfaceManager<Graph,GraphItem> m ) {
        manager = m;
    }

    public void handle( Event e ) {
        if ( !(e instanceof NodeChargeUpdateEvent) )
            return;
        if ( manager == null ) {
            log.warn( "Couldn't update charge; no GUIManager set!" );
            return;
        }
        Neurone n = ((NodeChargeUpdateEvent)e).getNeurone();
        final GUINode gn = (GUINode)manager.getNode( n );
        if ( gn != null ) { // is this GUINode displayed currently?
            Display.getDefault().syncExec( new Runnable() {
                public void run() {
                    gn.updateChargeOverlay();
                }
            } );
        }
    }

    public void flush() {
        // no-op
    }

    public boolean isValid() {
        return true;
    }

    public String getName() {
        return "ChargeUpdateHandler";
    }

}
