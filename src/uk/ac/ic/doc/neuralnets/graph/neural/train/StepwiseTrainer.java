package uk.ac.ic.doc.neuralnets.graph.neural.train;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import org.apache.log4j.Logger;
import uk.ac.ic.doc.neuralnets.graph.Node;
import uk.ac.ic.doc.neuralnets.graph.neural.io.InputNode;
import uk.ac.ic.doc.neuralnets.gui.graph.ionodes.ValueListingOutputNode;
import uk.ac.ic.doc.neuralnets.graph.neural.io.ValueReportingOutputNode;
import uk.ac.ic.doc.neuralnets.graph.neural.NeuralNetwork;
import uk.ac.ic.doc.neuralnets.graph.neural.Neurone;
import uk.ac.ic.doc.neuralnets.graph.neural.Synapse;
import uk.ac.ic.doc.neuralnets.graph.neural.manipulation.InteractionUtils;
import uk.ac.ic.doc.neuralnets.matrix.PartitionableMatrix;

/**
 *
 * @author Peter Coetzee
 */
public abstract class StepwiseTrainer implements Trainer {
    
    protected NeuralNetwork current;
    private int testLength = 1;
    protected PartitionableMatrix<Double> inputs, targets;
    protected Collection<InputNode> inputNodes;
    protected ValueReportingOutputNode valueReporter;
    
    private static final Logger log = Logger.getLogger( StepwiseTrainer.class  );
    
    public void setTestLength( int it ) {
        testLength = it;
    }
    
    public void setInputs( InputNode in ) {
        inputNodes = new ArrayList<InputNode>( 1 );
        inputNodes.add( in );
        inputs = in.getData();
        targets = in.getTargets();
    }
    
    public void setInputs( Collection<InputNode> in ) {
        inputNodes = in;
        for ( InputNode n : in ) {
            if ( inputs == null )
                inputs = n.getData();
            else
                inputs = append( inputs, n.getData() );
            if ( targets == null )
                targets = n.getTargets();
            else
                targets = append( targets, n.getTargets()  );
        }
    }
    
    protected <T> PartitionableMatrix<T> append( PartitionableMatrix<T> a, 
            PartitionableMatrix<T> b ) {
        PartitionableMatrix<T> out = new PartitionableMatrix<T>( a.getWidth(), 
                a.getHeight() + b.getHeight() );
        for ( int i = 0; i < a.getWidth(); i++ )
            for ( int j = 0; j < a.getHeight(); j++ )
                out.set( a.get( i, j ), i, j );
        for ( int i = 0; i < a.getWidth(); i++ )
            for ( int j = a.getHeight(); j < a.getHeight() + b.getHeight(); j++ )
                out.set( b.get( i - a.getHeight(), j ), i, j );
        return out;
    }

    /**
     * Train this network with one iteration
     * @param n The network to train
     * @return The accuracy of the network after training
     */
    public double trainOnce( NeuralNetwork n ) {
        assembleTestHarness( n );
        double acc = 0;
        int i;
        for ( i = 0; i < inputs.getHeight(); i++ ) {
            trainingStep( n, i );
            acc += calcError( i );
        }
        current = null;
        return acc / (double)i;
    }
    
    /**
     * Train this network until the accuracy >= target
     * @param n The network to train
     * @param errorTarget The target accuracy
     * @param maxIt The maximum number of iterations
     * @return The accuracy of the network after training
     */
    public double trainFully( NeuralNetwork n, double errorTarget, int maxIt ) {
        assembleTestHarness( n );
        double error = errorTarget + 1; // to start us off...
        for ( int i = 0; i < maxIt && error > errorTarget; i++ ) {
            int j;
            error = 0;
            for ( j = 0; j < inputs.getHeight(); j++ ) {
                trainingStep( n, j );
                error += calcError( j );
            }
            error /= (double)j; // Average accuracy
            log.info( "Iteration " + i + " complete. New accuracy: " + error );
        }
        current = null;
        return error;
    }
    
    /**
     * Assemble the structures needed to test a NN's accuracy
     * @param n The network to assemble the harness around
     */
    protected void assembleTestHarness( NeuralNetwork n ) {
        current = new NeuralNetwork();
        current.addNode( n );
        List<Neurone> outputs = new ArrayList<Neurone>();
        getOutputs( n, outputs );
        if ( outputs.size() < targets.getWidth() ) {
            log.error( "Got only " + outputs.size() + " outputs and " 
                    + targets.getWidth() + " targets!" );
            throw new UnsupportedOperationException( "Cannot train networks"
                    + " with more targets than outputs!" );
        }
        valueReporter = new ValueReportingOutputNode();
        NeuralNetwork vr = valueReporter.toNetwork( outputs.size() );
        current.addNode( vr );
        InteractionUtils util = new InteractionUtils( current );
        Collection es = util.connect1to1( outputs, vr.getNodes() );
//        NeuralNetwork vout = new ValueListingOutputNode().toNetwork( outputs.size() );
//        current.addNode( vout );
//        es.addAll( util.connect1to1( outputs, vout.getNodes() ) );
        for ( Object e : es )
            if ( e instanceof Synapse )
                ((Synapse)e).setWeight( 1d );
        log.debug( "Created test harness with " + outputs.size() 
                + " value reporting nodes, weighted at 1 only" );
    }
    
    /**
     * Get all output Neurones from the given network. A Neurone is decided to
     * be an output iff it has no outgoing edges. Note that it is possible
     * for a network to have more outputs than the target dataset. In this case
     * the lesser of the two sizes will be used in training the network.
     * 
     * Networks with more targets than outputs are not supported.
     * @param n The network to seek outputs in
     * @param outputs The List of outputs to add Neurones to
     */
    protected void getOutputs( NeuralNetwork n, List<Neurone> outputs ) {
        for ( Node<?> node : n.getNodes() )
            if ( node instanceof Neurone && node.getOutgoing().size() == 0 )
                outputs.add( (Neurone)node );
            else if ( node instanceof NeuralNetwork )
                getOutputs( (NeuralNetwork)node, outputs );
    }
    
    /**
     * Calculate the average error in the given network
     * @return The average error in the outputs. 0 is best possible
     */
    protected double calcError() {
        double error = 0;
        for ( int i = 0; i < targets.getHeight(); i++ )
            error += calcError( i );
        return error / (double)targets.getHeight();
    }
    
    /**
     * Calculate the average error of the given network against the selected
     * row of test data
     * @param test The test data row to use
     * @return The average error in the outputs. 0 is best possible
     */
    protected double calcError( int test ) {
        List<Double> outs = runNetwork( test );
        double error = 0;
        for ( int i = 0; i < targets.getWidth(); i++ )
            error += Math.abs( Math.pow( targets.get( i, test ) - outs.get( i ), 2 ) ) 
                            /* targets.get( i, test )*/;
        return error / (double)targets.getWidth();
    }
    
    /**
     * Run the input network for the specified test
     * @return The network outputs for this input
     */
    protected List<Double> runNetwork( int test ) {
        for ( InputNode in : inputNodes )
            in.setRow( test );
        for ( int i = 0; i < testLength; i++ )
            current.tick();
        return getNetworkOutputs();
    }

    protected List<Double> getNetworkOutputs() {
        return valueReporter.getValues();
    }
    
    /**
     * Answers a sorted array of the input set, sorted by the ID of the
     * input. Useful for trainers relying on a consistent ordering of nodes
     * @param in
     * @return
     */
    protected Node<?>[] order( Collection<Node<?>> in ) {
        Node<?>[] out = in.toArray( new Node<?>[0] );
        Arrays.sort( out, new Comparator<Node<?>>() {
            public int compare( Node<?> o1, Node<?> o2 ) {
                return o1.getID() < o2.getID() ? -1 :
                    (o1.getID() > o2.getID() ? 1 : 0);
            }
        } );
        return out;
    }
    
    protected double mult( double ... in ) {
        double out = 0;
        for ( double d : in )
            out = out == 0 ? d : (d == 0 ? out : out * d);
        return out;
    }
    
    /**
     * Perform one step of training, against the specified row of test data
     * @param n The network to train
     * @param test The test data row to train against
     */
    protected abstract void trainingStep( NeuralNetwork n, int test );
    
}
