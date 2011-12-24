package uk.ac.ic.doc.neuralnets.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Peter Coetzee
 */
public abstract class NumericalStatistician implements EventHandler {
    
    protected Logger log = Logger.getLogger( getClass().getName() );

    protected List<List<Integer>> values = new ArrayList<List<Integer>>();
    
    protected String saveTo = null;
    
    public void saveAs( String file ) {
        saveTo = file;
    }
    
    public void handle( Event e ) {
        log.debug( e );
        if ( !(e instanceof NumericalEvent) )
            throw new UnsupportedOperationException( "Cannot handle " 
                    + e.getClass().getSimpleName() + " events." );
        handle( (NumericalEvent)e );
    }
    
    public void handle( NumericalEvent e ) {
        e.push( this );
    }
    
    public void handle( Integer ... vs ) {
        handle( Arrays.asList( vs ) );
    }
    
    public void handle( List<Integer> vs ) {
        values.add( vs );
    }
    
    public boolean isValid() {
        return true;
    }
    
}
