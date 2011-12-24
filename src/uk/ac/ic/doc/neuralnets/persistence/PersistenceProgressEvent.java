package uk.ac.ic.doc.neuralnets.persistence;

import org.apache.log4j.Logger;
import uk.ac.ic.doc.neuralnets.events.Event;

/**
 *
 * @author Peter Coetzee
 */
public class PersistenceProgressEvent extends Event {
    
    private double progress;
    private static final Logger log = Logger.getLogger( PersistenceProgressEvent.class );

    public PersistenceProgressEvent( int num, int total ) {
        this( ((double)num) / ((double)total) );
        log.trace( "PPE : " + num + " / " + total );
    }
    
    public PersistenceProgressEvent( double progress ) {
        this.progress = progress;
    }
    
    public double getProgress() {
        return progress;
    }
    
    public int getProgressPercentage() {
        return (int)Math.round( 100 * progress );
    }
    
    @Override
    public String toString() {
        return "Persistence progress: " + getProgressPercentage() + "%";
    }

}
