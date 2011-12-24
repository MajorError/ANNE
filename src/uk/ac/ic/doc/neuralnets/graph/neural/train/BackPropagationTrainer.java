package uk.ac.ic.doc.neuralnets.graph.neural.train;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import uk.ac.ic.doc.neuralnets.graph.Node;
import uk.ac.ic.doc.neuralnets.graph.neural.io.IONeurone;
import uk.ac.ic.doc.neuralnets.graph.neural.NetworkBridge;
import uk.ac.ic.doc.neuralnets.graph.neural.NeuralNetwork;
import uk.ac.ic.doc.neuralnets.graph.neural.Neurone;
import uk.ac.ic.doc.neuralnets.graph.neural.Synapse;

/**
 *
 * @author Peter Coetzee
 */
public class BackPropagationTrainer extends StepwiseTrainer {
    
    private static final Logger log = Logger.getLogger( BackPropagationTrainer.class );
    private double lambda = 0.175; // between 0.15 and 0.2, suggested values
    private NeuralNetwork curr;
    private List<NeuralNetwork> layers;
    
    @Override
    protected void getOutputs( NeuralNetwork n, List<Neurone> outputs ) {
        if ( n != curr )
            prepareTrainer( n );
        for ( Node<?> node : layers.get( layers.size() - 1 ).getNodes() )
            if ( node instanceof Neurone )
                outputs.add( (Neurone)node );
    }

    @Override
    protected void trainingStep( NeuralNetwork n, int test ) {
        if ( curr != n )
            prepareTrainer( n );
        runNetwork( test );
        // Precalculate the errors at the last layer
        Map<Neurone,Double> errors = new HashMap<Neurone,Double>();
        Node<?>[] nodes = order( layers.get( layers.size() - 1 ).getNodes() );
        for ( int i = 0; i < nodes.length && i < targets.getWidth(); i++ ) {
            double charge = ((Neurone)nodes[i]).getCharge();
            log.debug( "Output charge " + i + ": " + charge + ", target: " + targets.get( i, test ) );
            /*errors.put( (Neurone)nodes[i], charge * (1 - charge) *
                    (targets.get( i, test ) - charge) );*/
            errors.put( (Neurone)nodes[i], targets.get( i, test ) - charge );
        }
        // Update from each layer to the one in front of it, backwards
        //  (the backpropagation step)
        for ( int i = layers.size() - 1; i > 0; i-- )
            updateWeights( errors, layers.get( i ) );
    }

    @Override
    protected List<Double> getNetworkOutputs() {
        Node<?>[] nodes = order( layers.get( layers.size() - 1 ).getNodes() );
        List<Double> out = new ArrayList<Double>();
        for ( Node<?> n : nodes )
            if ( n instanceof Neurone )
            out.add( ((Neurone)n).getCharge() );
        return out;
    }
    
    private void updateWeights( Map<Neurone,Double> errors, NeuralNetwork n ) {
        Node<?>[] nodes = order( n.getNodes() );
        for ( int i = 0; i < nodes.length; i++ ) {
            if ( !(nodes[i] instanceof Neurone) )
                continue; // we should maybe recurse here
            Neurone node = (Neurone)nodes[i];
            // Calculate error for given node
            double error = 0;
            if ( n.equals( layers.get( layers.size() - 1 ) ) ) {
                error = errors.get( node );
            } else {
                for ( Synapse s : node.getOutgoing() )
                    if ( !(s.getEnd() instanceof IONeurone) )
                        error += s.getWeight() * errors.get( s.getEnd() );
                error *= node.getCharge() * (1 - node.getCharge());
            }
            errors.put( node, error );
            // Apply new weight as delta = lambda * error * weight * input
            for ( Synapse s : node.getIncoming() ) {
                //log.trace( s + "  :::  e: " + error + ", w: " + s.getWeight() + ", c: " + node.getCharge() + ", i: " + s.getStart().getCharge() );
                //log.debug( "Training error " + error + " for weight " + s + " + " + lambda * error * s.getStart().getCharge() * node.getCharge() * (1 - node.getCharge()) );
                s.setWeight( s.getWeight() + lambda * error * s.getStart().getCharge() * node.getCharge() * (1 - node.getCharge()) );
            }
            node.setTrigger( node.getTrigger() + lambda * error * node.getCharge() * (1 - node.getCharge()) );
        }
    }
    

    /**
     * Build the list of layers, in the correct order
     * @param n The network we are training over
     */
    private void prepareTrainer( NeuralNetwork n ) {
        curr = n;
        layers = new ArrayList<NeuralNetwork>();
        NeuralNetwork start = null;
        // Find the starting network
        for ( Node<?> node : curr.getNodes() ) {
            if ( !(node instanceof NeuralNetwork) )
                continue;
            if ( node.getIncoming().size() == 0 ) {
                start = (NeuralNetwork)node;
                break;
            }
        }
        if ( start == null )
            throw new UnsupportedOperationException( 
                    "Invalid network structure; network has no start!" );
        while( start != null ) {
            log.debug( "Adding layer " + start + " (" + start.getNodes().size() + " nodes)" );
            layers.add( start );
            Collection<NetworkBridge> bridges = start.getOutgoing();
            start = null;
            for ( NetworkBridge edge : bridges ) { // shouldn't be more than one!
                start = edge.getEnd();
                break;
            }
        }
    }
    
    public String getName() {
        return "BackPropagation";
    }

}
