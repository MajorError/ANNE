package uk.ac.ic.doc.neuralnets.gui.commands;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Display;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphItem;
import uk.ac.ic.doc.neuralnets.graph.Node;
import uk.ac.ic.doc.neuralnets.graph.neural.manipulation.GraphFactory;
import uk.ac.ic.doc.neuralnets.graph.neural.manipulation.HomogenousNetworkSpecification;
import uk.ac.ic.doc.neuralnets.graph.neural.NodeSpecification;
import uk.ac.ic.doc.neuralnets.graph.neural.NeuralNetwork;
import uk.ac.ic.doc.neuralnets.commands.Command;
import uk.ac.ic.doc.neuralnets.coreui.ZoomingInterfaceManager;
import uk.ac.ic.doc.neuralnets.gui.commands.RemoveNodes;
import uk.ac.ic.doc.neuralnets.gui.connector.LayeredNetworkConnector;

public class LayeredNetworkBuildingCommand extends Command {

    private RemoveNodes undoNodes = new RemoveNodes();
    private List<Node<?>> nets;
    private NeuralNetwork current;
    private List<NodeSpecification> specs;
    private List<Integer> quantity;
    private ZoomingInterfaceManager<Graph,GraphItem> gm;
    
    private static final Logger log = Logger.getLogger( LayeredNetworkBuildingCommand.class );
    
    public LayeredNetworkBuildingCommand( List<NodeSpecification> specs, List<Integer> quantity, ZoomingInterfaceManager<Graph,GraphItem> gm  ) {
        this.specs = specs;
        this.quantity = quantity;
        this.gm = gm;
    }

    @Override
    protected void execute() {
        log.debug( "Inserting layered network" );
        current = gm.getCurrentNetwork();
        if ( nets == null ) {
            nets = new LinkedList<Node<?>>();
            Iterator<Integer> qs = quantity.iterator();
            for ( NodeSpecification spec : specs ) {
                int count = qs.next();
                NeuralNetwork n = GraphFactory.get().create( 
                        new HomogenousNetworkSpecification( spec, count, 0.0 ) );
                gm.getCurrentNetwork().addNode( n );
                nets.add( n );
                log.trace( "Layer " + ": " + count + " : " + spec.getName() );
            }
            log.trace( "Connecting layers" );
            LayeredNetworkConnector lnc = new LayeredNetworkConnector();
            lnc.setGUIManager( gm );
            lnc.connect( nets );
            log.trace( "Connected fully." );
        } else {
            for ( Node<?> n : nets ) {
                current.getNodes().add( n );
            }
        }
        Display.getDefault().
                syncExec( new Runnable() {

            public void run() {
                gm.redrawCurrentView();
            }
        } );
    }

    @SuppressWarnings( value = "unchecked" )
    @Override
    protected void undo() {
        undoNodes.remove( (List) nets, gm, current );
    }
}
