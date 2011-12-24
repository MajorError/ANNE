package uk.ac.ic.doc.neuralnets.gui.graph.listener;

import java.util.List;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.zest.core.widgets.GraphItem;
import org.eclipse.zest.core.widgets.GraphNode;

public class ClickSelectionListener extends MousePlugin {

    @Override
    public String getName() {
        return "ClickSelection";
    }

    @Override
    @SuppressWarnings( "unchecked" )
    protected void handleClick( MouseEvent e, GraphItem i ) {
        gm.getGraph().getSelection().clear();
        for ( GraphNode gn : (List<GraphNode>) gm.getGraph().getNodes() )
            gn.unhighlight();
        if ( i != null ) {
            i.highlight();
            gm.getGraph().getSelection().add( i );
        }
    }

}
