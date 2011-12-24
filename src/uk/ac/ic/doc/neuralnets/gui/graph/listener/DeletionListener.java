package uk.ac.ic.doc.neuralnets.gui.graph.listener;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.zest.core.widgets.GraphItem;

/**
 * 
 * @author Peter Coetzee
 */
public class DeletionListener extends MousePlugin {
    
    private static final Logger log = Logger.getLogger( DeletionListener.class  );
    
    @Override
    public void handleDoubleClick( MouseEvent e, GraphItem i ) {
        if ( i != null && (e.stateMask & SWT.MOD2) != 0 ) {
            log.info( "Deleting " + i );
            gm.remove( i );
        }
    }

    @Override
    public String getName() {
        return "Deletion";
    }
    
}