
package uk.ac.ic.doc.neuralnets.gui.graph.events;

import uk.ac.ic.doc.neuralnets.graph.neural.NewNeuroneTypeEvent;
import uk.ac.ic.doc.neuralnets.events.*;
import java.io.IOException;
import org.apache.log4j.Logger;
import uk.ac.ic.doc.neuralnets.graph.neural.NeuroneTypeConfig;

/**
 *
 * @author Peter Coetzee
 */
public class NeuroneTypesPersister implements EventHandler {
    
    private NeuroneTypeConfig cfg;

    public void handle( Event e ) {
        try {
            if ( e instanceof NewNeuroneTypeEvent )
            getCfg().commitConfiguration();
        } catch ( IOException ex ) {
            Logger.getLogger( NeuroneTypesPersister.class ).error( 
                    "Couldn't save " + e + "!", ex );
        }
    }
    
    private NeuroneTypeConfig getCfg() {
        if ( cfg == null )
            cfg = new NeuroneTypeConfig();
        return cfg;
    }

    public void flush() {
        // no-op
    }

    public boolean isValid() {
        return true;
    }

    public String getName() {
        return "NeuroneTypesPersister";
    }

}
