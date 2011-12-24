package uk.ac.ic.doc.neuralnets.gui.graph.listener;

import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphItem;
import uk.ac.ic.doc.neuralnets.gui.commands.DeleteItemsCommand;
import uk.ac.ic.doc.neuralnets.gui.graph.GUIAnchor;
import uk.ac.ic.doc.neuralnets.gui.graph.GUIBridge;
import uk.ac.ic.doc.neuralnets.gui.graph.GUIEdge;
import uk.ac.ic.doc.neuralnets.gui.graph.GUINetwork;
import uk.ac.ic.doc.neuralnets.gui.graph.GUINode;

/**
 *
 * @author Peter Coetzee
 */
public class MassDeletionListener extends KeyboardPlugin {
    
    @Override
    @SuppressWarnings( "unchecked" )
    public void keyReleased( KeyEvent e ) {
        if ( e.keyCode == SWT.DEL ) {
            DeleteItemsCommand cmd = new DeleteItemsCommand( gm );
            for ( GraphItem o : (List<GraphItem>)gm.getGraph().getSelection() ) {
            	if (o instanceof GUIAnchor )
            		continue;
                if ( o instanceof GraphConnection )
                    cmd.addToDeletion( 
                        o instanceof GUIBridge ? ((GUIBridge)o).getBridge() : 
                        (o instanceof GUIEdge ? ((GUIEdge)o).getEdge() : 
                            null) );
                else
                    cmd.addToDeletion( 
                        o instanceof GUINetwork ? ((GUINetwork)o).getNode() :
                        (o instanceof GUINode ? ((GUINode)o).getNode() : 
                            null) );
                o.unhighlight();
            }
            if ( cmd.deletedQuantity() > 0 )
                gm.getCommandControl().addCommand( cmd );
        }
    }

    @Override
    public String getName() {
        return "MassDeletion";
    }
    
    

}
