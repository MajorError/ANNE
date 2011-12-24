package uk.ac.ic.doc.neuralnets.events;

import org.apache.log4j.Logger;

/**
 *
 * @author Peter Coetzee
 */
public class GraphUpdateEvent extends Event {

    @Override
    public String toString() {
        return "Graph layout updated";
    }

}
