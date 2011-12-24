
package uk.ac.ic.doc.neuralnets.graph.neural.manipulation;

import uk.ac.ic.doc.neuralnets.events.*;

/**
 * Event to indicate an edge has been created
 * @author Peter Coetzee
 */
public class EdgeCreatedEvent extends Event {
    
    private int num, count;
    
    public EdgeCreatedEvent( int num, int count ) {
        this.num = num;
        this.count = count;
    }
    
    /**
     * Answer the number of edges thus far created
     * @return How many edges were created at the point of this event
     */
    public int getEdgeNumber() {
        return num;
    }
    
    /**
     * Answer the approximate number of edges to be created; this may be 
     * probabilistic and thus differ to the actual number created
     * @return A guess at the number of edges that will be created
     */
    public int getEdgeCount() {
        return count;
    }

    @Override
    public String toString() {
        return "Edge Created Event [" + num + "/" + count + "]";
    }

}
