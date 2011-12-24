package uk.ac.ic.doc.neuralnets.gui.graph.listener;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.zest.core.widgets.GraphItem;
import uk.ac.ic.doc.neuralnets.events.EventManager;
import uk.ac.ic.doc.neuralnets.events.GraphUpdateEvent;

/**
 *
 * @author Peter Coetzee
 */
public class NodeDragUpdateListener extends MousePlugin {
    
    @Override
    public void handleUp( MouseEvent e, GraphItem i ) {
        if ( i != null )
            EventManager.get().fire( new GraphUpdateEvent() );
    }

    @Override
    public String getName() {
        return "NodeDragUpdate";
    }

}
