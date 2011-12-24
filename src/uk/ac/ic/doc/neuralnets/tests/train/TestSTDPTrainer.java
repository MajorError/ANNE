package uk.ac.ic.doc.neuralnets.tests.train;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import org.apache.log4j.Logger;
import uk.ac.ic.doc.neuralnets.events.EventManager;
import uk.ac.ic.doc.neuralnets.graph.neural.NodeFired;
import uk.ac.ic.doc.neuralnets.expressions.ExpressionException;
import uk.ac.ic.doc.neuralnets.expressions.ast.ASTExpression;
import uk.ac.ic.doc.neuralnets.expressions.ast.ASTExpressionFactory;
import uk.ac.ic.doc.neuralnets.graph.Edge;
import uk.ac.ic.doc.neuralnets.graph.Node;
import uk.ac.ic.doc.neuralnets.graph.neural.manipulation.EdgeFactory;
import uk.ac.ic.doc.neuralnets.graph.neural.manipulation.GraphFactory;
import uk.ac.ic.doc.neuralnets.graph.neural.manipulation.HomogenousNetworkSpecification;
import uk.ac.ic.doc.neuralnets.graph.neural.manipulation.InhibitoryNodeSpecification;
import uk.ac.ic.doc.neuralnets.graph.neural.NodeSpecification;
import uk.ac.ic.doc.neuralnets.graph.neural.manipulation.SpikingNodeSpecification;
import uk.ac.ic.doc.neuralnets.graph.neural.io.IONeurone;
import uk.ac.ic.doc.neuralnets.graph.neural.io.InputNode;
import uk.ac.ic.doc.neuralnets.graph.neural.NeuralNetwork;
import uk.ac.ic.doc.neuralnets.graph.neural.Neurone;
import uk.ac.ic.doc.neuralnets.graph.neural.Synapse;
import uk.ac.ic.doc.neuralnets.gui.GUIMain;
import uk.ac.ic.doc.neuralnets.matrix.PartitionableMatrix;
import uk.ac.ic.doc.neuralnets.graph.neural.train.STDPTrainer;
import uk.ac.ic.doc.neuralnets.graph.neural.train.Trainer;
import uk.ac.ic.doc.neuralnets.gui.events.RealTimePlotStatistician;

/**
 *
 * @author Peter Coetzee
 */
public class TestSTDPTrainer {
    
    private static final Logger log = Logger.getLogger( TestSTDPTrainer.class );
    
    public static void main( String[] args ) {
        new Thread( new Runnable() {
            public void run() {
                try {
                    Thread.sleep( 7000 );
                } catch( InterruptedException ex ) { }
                log.info( "Preparing network components" );
                
                // Prepare input node
                InputNode ni = new InputNode() {

                    boolean s = true;
                    @Override
                    public void configure() {
                        data = new PartitionableMatrix<Double>( 2, 1 );
                        data.set( 1000d, 1, 0 );
                        data.set( s ? 1000d : 0d, 0, 0 );
                        s = !s;
                        /*data.set( 0d, 0, 1 );
                        data.set( 100d, 1, 1 );
                        
                        data.set( 0d, 0, 2 );
                        data.set( 0d, 1, 2 );
                        
                        data.set( 100d, 0, 3 );
                        data.set( 100d, 1, 3 );*/
                        
                        targets = new PartitionableMatrix<Double>( 1, data.getHeight() );
                        targets.set( 0d, 0, 0 );
                        /*targets.set( 0d, 0, 1 );
                        targets.set( 0d, 0, 2 );
                        targets.set( 0d, 0, 3 );*/
                        System.out.println( data + "\n\n" + targets );
                    }
                    
                    public void recreate() {}
                    public void destroy() {}

                    public String getName() {
                        return "SimpleSTDPInputNode";
                    }
                };
                // Build a homogenous network
                List<NodeSpecification> specs = new ArrayList<NodeSpecification>();
                List<Integer> nodes = new ArrayList<Integer>();
                ASTExpression zero = null;
                try {
                    zero = ASTExpressionFactory.get().getExpression( "0.0" );
                } catch ( ExpressionException ex ) {
                    log.error( "Bad expression", ex );
                }
                specs.add( new SpikingNodeSpecification().set( "Thalamic Input", zero ) );
                nodes.add( 800 );
                specs.add( new InhibitoryNodeSpecification().set( "Thalamic Input", zero ) );
                nodes.add( 200 );
                final NeuralNetwork n = GraphFactory.get().create( 
                        new HomogenousNetworkSpecification( specs, nodes, 1 ) );
                
                Neurone[] targets = n.getNodes().toArray( new Neurone[]{} );
                Arrays.sort( targets, new Comparator<Neurone>() {
                    public int compare( Neurone o1, Neurone o2 ) {
                        return o1.getID() < o2.getID() ? -1 : 
                            (o1.getID() == o2.getID() ? 0 : 1);
                    }
                } );
                n.addNode( ni );
                IONeurone[] src = ni.getNodes().toArray( new IONeurone[]{} );
                for ( int i = 0; i < 10; i++ ) {
                    Synapse e = (Synapse)(Edge)EdgeFactory.get().create( src[0], targets[i] );
                    src[0].connect( e );
                    targets[i].connect( e );
                }
                int topStart = targets.length / 2;
                for ( int i = topStart; i > topStart - 11; i-- ) {
                    Synapse e = (Synapse)(Edge)EdgeFactory.get().create( src[1], targets[i] );
                    src[1].connect( e );
                    targets[i].connect( e );
                }
                    
                for ( Node<?> node : ni.toNetwork().getNodes() )
                    for ( Edge e : node.getOutgoing() )
                        log.info( "Input " + node + " connects to " + e.getEnd() );
                EventManager.get().registerAsync( NodeFired.class, new RealTimePlotStatistician() );
                log.info( "Created networks" );
                for ( int i = 0; i < 100; i++ )
                    n.tick();
                    
                log.info( "Had " + n.getTicks() + " ticks" );
                ni.configure();
                long start = System.currentTimeMillis();
                Trainer t = new STDPTrainer();
                t.setInputs( ni );
                t.setTestLength( 1 );

                log.info( "Begin training" );

                t.trainFully( n, 0.000001, 500 );
                log.info( "Training took " + (System.currentTimeMillis() - start) + "ms" );
                log.info( "Had " + n.getTicks() + " ticks" );
                try {
                    Thread.sleep( 5000 );
                } catch ( InterruptedException ex ) { }
                
                ni.configure();
                for ( int i = 0; i < 1000; i++ )
                    n.tick();
                log.info( "Had " + n.getTicks() + " ticks" );
                
                EventManager.get().flush( NodeFired.class );
                try {
                    Thread.sleep( 5000 );
                } catch ( InterruptedException ex ) { }
                
                log.info( "Test complete" );
                
            }
        } ).start();
        // Just to get the Display started, configured etc.
        GUIMain.main( new String[]{} );
    }

}
