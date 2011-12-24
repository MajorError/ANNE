package uk.ac.ic.doc.neuralnets.gui.commands;

import uk.ac.ic.doc.neuralnets.commands.Command;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Display;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphItem;
import uk.ac.ic.doc.neuralnets.coreui.ZoomingInterfaceManager;
import uk.ac.ic.doc.neuralnets.graph.Edge;
import uk.ac.ic.doc.neuralnets.graph.Node;
import uk.ac.ic.doc.neuralnets.graph.neural.NeuralNetwork;
import uk.ac.ic.doc.neuralnets.gui.commands.RemoveNodes;

/**
 *
 * @author Peter Coetzee
 */
public class DeleteItemsCommand extends Command {
    
    private ZoomingInterfaceManager<Graph,GraphItem> manager;
    private NeuralNetwork current;
    private Collection<Node> nodes = new HashSet<Node>();
    private Collection<Edge> edges = new HashSet<Edge>();
    private RemoveNodes deleteNodes = new RemoveNodes();
    
    private static final Logger log = Logger.getLogger( DeleteItemsCommand.class );
    
    public DeleteItemsCommand( ZoomingInterfaceManager<Graph,GraphItem> manager ) {
        this.manager = manager;
        current = manager.getCurrentNetwork();
    }
    
    public DeleteItemsCommand( ZoomingInterfaceManager<Graph,GraphItem> manager, Node ... nodes ) {
        this( manager );
        addToDeletion( nodes );
    }
    
    public DeleteItemsCommand( ZoomingInterfaceManager<Graph,GraphItem> manager, Edge ... edges ) {
        this( manager );
        addToDeletion( edges );
    }
    
    public DeleteItemsCommand( ZoomingInterfaceManager<Graph,GraphItem> manager, Collection c ) {
        this( manager );
        addToDeletion( c );
    }
    
    public void addToDeletion( Collection c ) {
        for ( Object o : c ) {
            if ( o instanceof Node )
                nodes.add( (Node)o );
            else if ( o instanceof Edge )
                edges.add( (Edge)o );
            else
                log.debug( "Couldn't delete item " + o );
        }
    }
    
    public void addToDeletion( Node ... nodes ) {
        this.nodes.addAll( Arrays.asList( nodes ) );
    }
    
    public void addToDeletion( Edge ... edges ) {
        this.edges.addAll( Arrays.asList( edges ) );
    }
    
    public int deletedQuantity() {
        return nodes.size() + edges.size();
    }

    @Override
    @SuppressWarnings( "unchecked" )
    protected void execute() {
        // Make sure there are no dangling edges
        for ( Node n : nodes ) {
            edges.addAll( n.getIncoming() );
            edges.addAll( n.getOutgoing() );
        }
        log.debug( "Deleting " + edges + " and " + nodes + " from " + current );
       
        deleteNodes.remove(edges, nodes, manager, current);
    }

    @Override
    @SuppressWarnings( "unchecked" )
    protected void undo() {
        log.debug( "Un-Deleting " + edges + " and " + nodes + " in " + current );       
        current.getNodes().addAll( (Collection)nodes );
        current.getEdges().addAll( (Collection)edges );
        Display.getDefault().syncExec( new Runnable() {
             public void run() {
                manager.redrawCurrentView();
             }
        } );
    }

}
