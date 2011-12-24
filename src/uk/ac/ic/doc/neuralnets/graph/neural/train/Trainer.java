package uk.ac.ic.doc.neuralnets.graph.neural.train;

import java.util.Collection;
import uk.ac.ic.doc.neuralnets.graph.neural.io.InputNode;
import uk.ac.ic.doc.neuralnets.graph.neural.NeuralNetwork;
import uk.ac.ic.doc.neuralnets.util.plugins.Plugin;

/**
 *
 * @author Peter Coetzee
 */
public interface Trainer extends Plugin {

    public void setTestLength( int it );
    
    public void setInputs( InputNode in );
    
    public void setInputs( Collection<InputNode> in );

    /**
     * Train this network with one iteration
     * @param n The network to train
     * @return The accuracy of the network after training
     */
    public double trainOnce( NeuralNetwork n );
    
    /**
     * Train this network until the accuracy >= target
     * @param n The network to train
     * @param errorTarget The target accuracy
     * @param maxIt The maximum number of iterations
     * @return The accuracy of the network after training
     */
    public double trainFully( NeuralNetwork n, double errorTarget, int maxIt );
    
    
}
