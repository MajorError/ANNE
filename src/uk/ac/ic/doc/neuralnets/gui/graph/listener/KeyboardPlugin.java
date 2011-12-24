package uk.ac.ic.doc.neuralnets.gui.graph.listener;

import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphItem;
import uk.ac.ic.doc.neuralnets.coreui.ZoomingInterfaceManager;
import uk.ac.ic.doc.neuralnets.util.plugins.Plugin;

/**
 *
 * @author Peter Coetzee
 */
public abstract class KeyboardPlugin implements KeyListener, Plugin {
    
    protected ZoomingInterfaceManager<Graph,GraphItem> gm;
    
    public void setManager( ZoomingInterfaceManager<Graph,GraphItem> g ) {
        gm = g;
    }
    
    public void keyPressed( KeyEvent e ) {
        // no-op
    }
    
    public void keyReleased( KeyEvent e ) {
        // no-op
    }

    public abstract String getName();

}
