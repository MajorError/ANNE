package uk.ac.ic.doc.neuralnets.gui.commands;

import uk.ac.ic.doc.neuralnets.commands.Command;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Display;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphItem;
import uk.ac.ic.doc.neuralnets.coreui.InterfaceManager;
import uk.ac.ic.doc.neuralnets.coreui.ZoomingInterfaceManager;
import uk.ac.ic.doc.neuralnets.graph.neural.manipulation.GraphFactory;
import uk.ac.ic.doc.neuralnets.graph.neural.manipulation.HomogenousNetworkSpecification;
import uk.ac.ic.doc.neuralnets.graph.neural.NodeSpecification;
import uk.ac.ic.doc.neuralnets.graph.neural.NeuralNetwork;
import uk.ac.ic.doc.neuralnets.graph.neural.NeuroneTypes;

public class AddNodesCommand extends Command {
    
    private static Logger log = Logger.getLogger( AddNodesCommand.class );

    private ZoomingInterfaceManager<Graph,GraphItem> gm;
    private NeuralNetwork nn;
    private NeuralNetwork redoNetwork;
    private NeuralNetwork current;
    private RemoveNodes undoNodes;
    private Map<String,Integer> types;
    private double edgeProb;

    public AddNodesCommand( ZoomingInterfaceManager<Graph,GraphItem> gm, Map<String,Integer> types, double edgeProb ) {
        super();
        this.types = types;
        this.gm = gm;
        current = gm.getCurrentNetwork();
        this.undoNodes = new RemoveNodes();
        this.edgeProb = edgeProb;
    }

    public void execute() {
        if ( redoNetwork == null ) {
            List<Integer> counts = new ArrayList<Integer>( types.values() );
            List<NodeSpecification> specs = new ArrayList<NodeSpecification>( types.size() );
            for ( String nType : types.keySet() ) {
                specs.add( NeuroneTypes.specFor( nType ) );
            }
            nn = GraphFactory.get().
                    create( new HomogenousNetworkSpecification( specs, counts, edgeProb ) );
            addNetwork( gm, nn );
            redoNetwork = nn;
            types.clear();
        } else {
            current.getNodes().add( redoNetwork );
            Display.getDefault().
                    asyncExec( new Runnable() {

                public void run() {
                    gm.redrawCurrentView();
                }
            } );
        }
    }

	private void addNetwork(final InterfaceManager gm, final NeuralNetwork nn) {
		log.trace("Adding neural network");
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				gm.addNetwork(nn);
			}
		});
	}

    @Override
    protected void undo() {
        undoNodes.remove( nn, gm, current );
    }

    public String getName() {
        return "InsertThread";
    }
}
