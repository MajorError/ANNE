package uk.ac.ic.doc.neuralnets.graph.neural.io;

import uk.ac.ic.doc.neuralnets.graph.Node;
import uk.ac.ic.doc.neuralnets.graph.neural.NeuralNetwork;
import uk.ac.ic.doc.neuralnets.graph.neural.Synapse;
import uk.ac.ic.doc.neuralnets.util.plugins.Plugin;

/**
 * OutputNodes are the default method for harvesting data from a neural network 
 * for use in external cases.
 *
 * Each time an output node fires the abstract fire method is called.
 *
 * @author Peter Coetzee
 */
public abstract class OutputNode extends NeuralNetwork implements Plugin {
    
	/**
	 * Create the empty output node. A call to toNetwork should be made soon.
	 */
    public OutputNode() {
        // no-op : MUST call toNetwork soon!!
    }
    
    /**
     * Create the output nodes
     * @param nodes - the number of nodes to create
     */
    public OutputNode( int nodes ) {
        toNetwork( nodes );
    }
    
	/**
	 * Sends data to the network.
	 * 
	 * @return Itself.
	 */
    public NeuralNetwork toNetwork( int nodes ) {
        for ( int i = 0; i < nodes; i++ ) {
            final int curr = i;
            addNode( new IONeurone() {
                @Override
                public Node<Synapse> tick() {
                    ticks++;
                    fire( curr, charge );
                    charge = 0;
                    return this;
                }
            } );
        }
        setNodes( nodes );
        return this;
    }
    
    @Override
    public String toString() {
        return "Output Node (" + getID() + ")";
    }
    
    /**
     * Called when an output node fires.
     * @param n the index of the node.
     * @param amt the charge passed through.
     */
    protected abstract void fire( int n, Double amt );
    
    /**
     * Configures the nodes in the OutputNode after they've been added to the 
     * network.
     * @param n - the 
     */
    protected abstract void setNodes( int n );
    
	/**
	 * Tear-down housekeeping for when the node is removed from the graph.
	 */
    public abstract void destroy();
    

	/**
	 * Called when configuration data is already in memory and the user need not
	 * be promted for it again.
	 */
    public abstract void recreate();

}
