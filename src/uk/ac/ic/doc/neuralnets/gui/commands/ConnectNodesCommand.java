package uk.ac.ic.doc.neuralnets.gui.commands;

import uk.ac.ic.doc.neuralnets.commands.Command;
import java.util.Collection;
import java.util.List;
import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Display;
import uk.ac.ic.doc.neuralnets.coreui.ZoomingInterfaceManager;
import uk.ac.ic.doc.neuralnets.graph.Edge;
import uk.ac.ic.doc.neuralnets.graph.Node;
import uk.ac.ic.doc.neuralnets.graph.neural.NeuralNetwork;
import uk.ac.ic.doc.neuralnets.graph.neural.Neurone;
import uk.ac.ic.doc.neuralnets.graph.neural.Synapse;
import uk.ac.ic.doc.neuralnets.gui.connector.NetworkConnector;

/**
 *
 * @author Peter Coetzee
 */
public class ConnectNodesCommand extends Command {
    
    private NetworkConnector conn;
    private List<Node<?>> items;
    private Collection<Edge<Node<?>,Node<?>>> edges = null;
    private ZoomingInterfaceManager gm;
    private NeuralNetwork addedTo;
    
    private static final Logger log = Logger.getLogger( ConnectNodesCommand.class );
    
    public ConnectNodesCommand( NetworkConnector conn, List<Node<?>> items, ZoomingInterfaceManager gm ) {
        this.conn = conn;
        this.items = items;
        this.gm = gm;
        this.addedTo = gm.getCurrentNetwork();
    }

    @Override
    protected void execute() {
        log.debug( edges == null ? "Creating edges for " + items.size() + " items" : "Using old edges" );
        if ( edges == null )
            edges = conn.connect( items );
        else
            for ( Edge e : edges )
                if ( e instanceof Synapse )
                    ((Synapse)gm.getUtils().connect( (Neurone)e.getStart(), (Neurone)e.getEnd() ))
                            .setWeight( ((Synapse)e).getWeight() );
                else
                    gm.getUtils().connect( e.getStart(), e.getEnd() );
        log.debug( "Added " + edges.size() + " edges" );
        
        Display.getDefault().syncExec( new Runnable() {
             public void run() {
                gm.redrawCurrentView();
             }
        } );
    }

    @Override
    protected void undo() {
        undo( addedTo );
        for ( Edge<Node<?>, Node<?>> e : edges ) {
            e.getStart().getOutgoing().remove( e );
            e.getEnd().getIncoming().remove( e );
        }
        
        Display.getDefault().syncExec( new Runnable() {
             public void run() {
                gm.redrawCurrentView();
             }
        } );
    }
    
    private void undo( NeuralNetwork n ) {
        n.getEdges().removeAll( edges );
        for ( Node<?> node : n.getNodes() )
            if ( node instanceof NeuralNetwork )
                undo( (NeuralNetwork)node );
    }

}
