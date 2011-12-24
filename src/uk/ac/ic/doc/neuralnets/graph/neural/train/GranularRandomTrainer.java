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
public class GranularRandomTrainer extends StepwiseTrainer {
    
    private static final Logger log = Logger.getLogger( GranularRandomTrainer.class  );
    private double scale = 0.1;

    @Override
    protected void trainingStep( NeuralNetwork n, int test ) {
        trainEdges( n.getEdges() );
    }
    
    private void trainEdges( Collection<Edge<?,?>> in ) {
        for ( Edge<?, ?> e : in ) {
            if ( e instanceof Synapse ) {
                Synapse s = (Synapse)e;
                double w = s.getWeight();
                double start = calcError();
                // delta in weight from -scale < 0 < scale
                double nw = Math.max( -1d, Math.min( 1d, 
                        w - (2 * scale * Math.random() - scale) ) );
                // 0 < weight - d < 1
                s.setWeight( nw );
                if ( start < calcError() )
                    s.setWeight( w ); // accuracy got worse, reset the weight
            } else {
                trainEdges( (Collection) ((NetworkBridge)e).getBundle() );
            }
        }
    }

    public String getName() {
        return "Random";
    }

}
