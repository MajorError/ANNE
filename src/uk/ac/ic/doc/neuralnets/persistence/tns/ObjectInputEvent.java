package uk.ac.ic.doc.neuralnets.persistence.tns;

import uk.ac.ic.doc.neuralnets.events.Event;
import uk.ac.ic.doc.neuralnets.graph.neural.NeuralNetwork;

/**
 *
 * @author Peter Coetzee
 */
public class ObjectInputEvent extends Event {
    
    private String line;
    
    public ObjectInputEvent( String line ) {
        this.line = line;
    }
    
    public String getLine() {
        return line;
    }

    @Override
    public String toString() {
        return "ObjectInputEvent for Object " + line;
    }

}
