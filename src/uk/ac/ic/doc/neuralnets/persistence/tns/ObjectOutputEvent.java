package uk.ac.ic.doc.neuralnets.persistence.tns;

import uk.ac.ic.doc.neuralnets.events.Event;
import uk.ac.ic.doc.neuralnets.graph.Identifiable;

/**
 *
 * @author Peter Coetzee
 */
public class ObjectOutputEvent extends Event {
    
    private Identifiable id;
    
    public ObjectOutputEvent( Identifiable id ) {
        this.id = id;
    }
    
    public Identifiable getObject() {
        return id;
    }

    @Override
    public String toString() {
        return "ObjectOutputEvent for " + id;
    }

}
