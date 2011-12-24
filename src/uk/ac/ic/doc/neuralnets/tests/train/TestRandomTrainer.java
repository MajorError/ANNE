package uk.ac.ic.doc.neuralnets.tests.train;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import uk.ac.ic.doc.neuralnets.graph.Edge;
import uk.ac.ic.doc.neuralnets.graph.neural.manipulation.GraphFactory;
import uk.ac.ic.doc.neuralnets.graph.neural.manipulation.HomogenousNetworkSpecification;
import uk.ac.ic.doc.neuralnets.graph.neural.NodeSpecification;
import uk.ac.ic.doc.neuralnets.graph.neural.manipulation.PerceptronSpecification;
import uk.ac.ic.doc.neuralnets.gui.graph.ionodes.DATInputNode;
import uk.ac.ic.doc.neuralnets.graph.neural.io.InputNode;
import uk.ac.ic.doc.neuralnets.graph.neural.NetworkBridge;
import uk.ac.ic.doc.neuralnets.graph.neural.NeuralNetwork;
import uk.ac.ic.doc.neuralnets.gui.GUIMain;
import uk.ac.ic.doc.neuralnets.graph.neural.manipulation.InteractionUtils;
import uk.ac.ic.doc.neuralnets.graph.neural.train.GranularRandomTrainer;
import uk.ac.ic.doc.neuralnets.graph.neural.train.StepwiseTrainer;

/**
 *
 * @author Peter Coetzee
 */
public class TestRandomTrainer {
    
    private static final Logger log = Logger.getLogger( TestRandomTrainer.class );
    
    public static void main( String[] args ) {
        try {
            new Thread( new Runnable() {
                public void run() {
                    // Just to get the Display started, configured etc.
                    GUIMain.main( null );
                }
            } ).start();
            Thread.sleep( 5000 );
        } catch( InterruptedException ex ) { }
        log.info( "Preparing network components" );
        InputNode inNode = new DATInputNode();
        NeuralNetwork in = inNode.toNetwork();
        List<NodeSpecification> specs = new ArrayList<NodeSpecification>();
        List<Integer> nodes = new ArrayList<Integer>();
        specs.add( new PerceptronSpecification() );
        nodes.add( 6 );
        NeuralNetwork on = GraphFactory.get().create( 
                new HomogenousNetworkSpecification( specs, nodes, 0 ) );
        
        final NeuralNetwork n = new NeuralNetwork();
        n.addNode( in );
        n.addNode( on );
        
        log.info( "Created networks" );
        
        InteractionUtils util = new InteractionUtils( n );
        util.connect( in.getNodes(), on.getNodes() );
        for ( Edge<?, ?> edge : n.getEdges() )
            log.debug( edge );
        
        log.info( "Connected networks" );
        
        StepwiseTrainer t = new GranularRandomTrainer();
        t.setInputs( inNode );
        
        log.info( "Begin training" );
        
        t.trainFully( n, 0.2, 500 );
    }

}
