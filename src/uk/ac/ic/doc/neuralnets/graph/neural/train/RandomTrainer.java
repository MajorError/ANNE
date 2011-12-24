package uk.ac.ic.doc.neuralnets.graph.neural.train;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import uk.ac.ic.doc.neuralnets.graph.Edge;
import uk.ac.ic.doc.neuralnets.graph.neural.NetworkBridge;
import uk.ac.ic.doc.neuralnets.graph.neural.NeuralNetwork;
import uk.ac.ic.doc.neuralnets.graph.neural.Synapse;

/**
 *
 * @author Peter Coetzee
 */
public class RandomTrainer extends StepwiseTrainer {
    
    private static final Logger log = Logger.getLogger( RandomTrainer.class );
    private double scale = 0.01;

    @Override
    protected void trainingStep( NeuralNetwork n, int test ) {
        double start = calcError(/* test */);
        Map<Synapse,Double> old = new HashMap<Synapse,Double>();
        trainEdges( old, n.getEdges() );
        double end = calcError(/* test */);
        //log.debug(  "Stepped from " + start + " to " + end );
        if ( start > end )
            return; // We've improved accuracy!
        for ( Map.Entry<Synapse, Double> e : old.entrySet())
            e.getKey().setWeight( e.getValue() );
    }
    
    private void trainEdges( Map<Synapse,Double> old, Collection<Edge<?,?>> in ) {
        for ( Edge<?, ?> e : in ) {
            if ( e instanceof Synapse ) {
                Synapse s = (Synapse)e;
                old.put( s, s.getWeight() );
                // delta in weight from -scale < 0 < scale
                double d = 2 * scale * Math.random() - scale;
                // -1 < weight - d < 1
                s.setWeight( Math.max( -1d, Math.min( 1d, s.getWeight() - d ) ) );
            } else {
                trainEdges( old, (Collection) ((NetworkBridge)e).getBundle() );
            }
        }
    }

    public String getName() {
        return "Random";
    }

}
