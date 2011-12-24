
package uk.ac.ic.doc.neuralnets.graph.neural;

import uk.ac.ic.doc.neuralnets.graph.neural.*;
import uk.ac.ic.doc.neuralnets.events.*;
import uk.ac.ic.doc.neuralnets.graph.neural.Neurone;

/**
 *
 * @author Peter Coetzee
 */
public class NodeChargeUpdateEvent extends SingletonEvent {
    
    private Neurone target;
    
    public NodeChargeUpdateEvent( Neurone n ) {
        target = n;
    }
    
    public Neurone getNeurone() {
        return target;
    }

    @Override
    public String toString() {
        return "Node Charge Update Event [" + target + "]";
    }

    @Override
    public boolean equals( Object o ) {
        return o != null && o instanceof NodeChargeUpdateEvent 
            && getNeurone().equals( ((NodeChargeUpdateEvent)o).getNeurone() );
    }

}
