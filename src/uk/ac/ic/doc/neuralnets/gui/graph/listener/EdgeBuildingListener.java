package uk.ac.ic.doc.neuralnets.gui.graph.listener;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.zest.core.widgets.GraphItem;
import uk.ac.ic.doc.neuralnets.graph.Edge;
import uk.ac.ic.doc.neuralnets.gui.graph.GUINode;

/**
 * 
 * @author Peter Coetzee
 */
public class EdgeBuildingListener extends MousePlugin {
    
    private GraphItem last = null;
    private static final Logger log = Logger.getLogger( EdgeBuildingListener.class );
    
    @Override
    public void handleClick( MouseEvent e, GraphItem i ) {
        if ( i != null && i instanceof GUINode ) {
            if ( last != null && last instanceof GUINode &&
                    (e.stateMask & SWT.MOD1) != 0) {
                log.info( "Connecting " + last + " --> " + i );
                Edge ed = gm.getUtils().connect( ((GUINode)last).getNode(), 
                        ((GUINode)i).getNode() );
                gm.addConnection( ed );
            }
            last = i;
        }
    }

    @Override
    public String getName() {
        return "EdgeBuilder";
    }
    
}