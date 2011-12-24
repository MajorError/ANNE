package uk.ac.ic.doc.neuralnets.tests.train;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import uk.ac.ic.doc.neuralnets.graph.Node;
import uk.ac.ic.doc.neuralnets.graph.neural.manipulation.GraphFactory;
import uk.ac.ic.doc.neuralnets.graph.neural.NodeSpecification;
import uk.ac.ic.doc.neuralnets.graph.neural.manipulation.PerceptronSpecification;
import uk.ac.ic.doc.neuralnets.gui.graph.ionodes.DATInputNode;
import uk.ac.ic.doc.neuralnets.graph.neural.io.InputNode;
import uk.ac.ic.doc.neuralnets.graph.neural.NeuralNetwork;
import uk.ac.ic.doc.neuralnets.graph.neural.manipulation.HomogenousNetworkSpecification;
import uk.ac.ic.doc.neuralnets.gui.GUIMain;
import uk.ac.ic.doc.neuralnets.graph.neural.manipulation.InteractionUtils;
import uk.ac.ic.doc.neuralnets.graph.neural.train.BackPropagationTrainer;
import uk.ac.ic.doc.neuralnets.graph.neural.train.StepwiseTrainer;

/**
 *
 * @author Peter Coetzee
 */
public class TestBackPropTrainer {
    
    private static final Logger log = Logger.getLogger( TestBackPropTrainer.class  );
    
    public static void main( String[] args ) {
        new Thread( new Runnable() {
            public void run() {
                try {
                    Thread.sleep( 5000 );
                } catch( InterruptedException ex ) { }
                log.info( "Preparing network components" );
                InputNode inNode = new DATInputNode();
                NeuralNetwork in = inNode.toNetwork();
                
                // Build a layered network
                List<NodeSpecification> specs = new ArrayList<NodeSpecification>();
                List<Integer> nodes = new ArrayList<Integer>();
                specs.add( new PerceptronSpecification() );
                nodes.add( 1 );
                final NeuralNetwork n = GraphFactory.get().create( 
                        new HomogenousNetworkSpecification( specs, nodes, 1 ) );

                InteractionUtils utils = new InteractionUtils( n );
                // Connect up the input nodes now
                n.addNode( in );
                for ( Node<?> node : n.getNodes() ) {
                    if ( node != in && node instanceof NeuralNetwork && 
                            ((NeuralNetwork)node).getIncoming().size() == 0 ) {
                        utils.connect( in.getNodes(), ((NeuralNetwork)node).getNodes() );
                        break;
                    }
                }
                
                utils.prettyPrintNetwork( System.out );

                log.info( "Created networks" );

                StepwiseTrainer t = new BackPropagationTrainer();
                t.setInputs( inNode );

                log.info( "Begin training" );

                t.trainFully( n, 0.000001, 10000 );
            }
        } ).start();
        // Just to get the Display started, configured etc.
        GUIMain.main( null );
    }

}
