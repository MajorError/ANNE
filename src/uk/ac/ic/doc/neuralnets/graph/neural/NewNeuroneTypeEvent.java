
package uk.ac.ic.doc.neuralnets.graph.neural;

import uk.ac.ic.doc.neuralnets.events.*;

/**
 * Indicates a new neurone type has been created
 * @author Peter Coetzee
 */
public class NewNeuroneTypeEvent extends Event {
    
    private String name;

    public NewNeuroneTypeEvent( String name ) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    @Override
    public String toString() {
        return "New Neurone Type [" + name + "]";
    }

}
