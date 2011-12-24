package uk.ac.ic.doc.neuralnets.graph.neural;

import uk.ac.ic.doc.neuralnets.events.*;
import uk.ac.ic.doc.neuralnets.graph.Node;

/**
 *
 * @author Peter Coetzee
 */
public class NodeFired extends NumericalEvent {
    
    private int tick;
    private Node<?> node;
    
    public NodeFired( Node<?> node, int tick ) {
        this.node = node;
        this.tick = tick;
    }
    
    public Node<?> getNode() {
        return node;
    }

    public int getTick() {
        return tick;
    }

    @Override
    public void push( NumericalStatistician s ) {
        s.handle( tick, node.getID() );
    }
    
    @Override
    public double get( int idx ) {
        switch( idx ) {
            case 0 : return tick;
            case 1 : return node.getID();
            default : throw new UnsupportedOperationException( "Invalid idx " + idx );
        }
    }

    @Override
    public double numPoints() {
        return 2;
    }

    @Override
    public String toString() {
        return "Node " + node + " Fired at tick " + tick;
    }

}
