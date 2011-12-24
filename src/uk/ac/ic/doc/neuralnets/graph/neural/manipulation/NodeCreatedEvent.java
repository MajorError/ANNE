
package uk.ac.ic.doc.neuralnets.graph.neural.manipulation;

import uk.ac.ic.doc.neuralnets.events.*;

/**
 * Indicates a node has been created by the factory
 * @author Peter Coetzee
 */
public class NodeCreatedEvent extends Event {
    
    private int num, count;
    
    public NodeCreatedEvent( int num, int count ) {
        this.num = num;
        this.count = count;
    }
    
    /**
     * Get the number of nodes created so far
     * @return The quantity of nodes thus far created
     */
    public int getNodeNumber() {
        return num;
    }
    
    /**
     * Get the number of nodes that need to be created
     * @return The maximum number of nodes to be created
     */
    public int getNodeCount() {
        return count;
    }

    @Override
    public String toString() {
        return "Node Created Event [" + num + "/" + count + "]";
    }

}
