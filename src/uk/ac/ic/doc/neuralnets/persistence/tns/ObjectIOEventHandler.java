package uk.ac.ic.doc.neuralnets.persistence.tns;

import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import uk.ac.ic.doc.neuralnets.events.Event;
import uk.ac.ic.doc.neuralnets.events.EventHandler;
import uk.ac.ic.doc.neuralnets.events.EventManager;
import uk.ac.ic.doc.neuralnets.graph.Identifiable;
import uk.ac.ic.doc.neuralnets.graph.neural.io.IONeurone;
import uk.ac.ic.doc.neuralnets.persistence.MethodSelector;

/**
 * Serialisation takes the form:
 * ID,ClassName,@@Field1=Value1@@Field2=Value2@@Field3=Value3@@,SpecialParts
 * @author Peter Coetzee
 */
public abstract class ObjectIOEventHandler<T extends Identifiable> implements EventHandler {
    
    private BufferedWriter out;
    
    protected static final Map<Integer,Identifiable> cache = new HashMap<Integer,Identifiable>();
    protected static final MethodSelector ms = new MethodSelector();
    protected final Set<Integer> written = new HashSet<Integer>();
    private FastSaveTextSerialisationService save;
    private FastLoadTextSerialisationService load;
    
    protected final Logger log = Logger.getLogger( getClass() );
    
    protected static final String 
            OUTER_DELIMETER = ",", 
            FIELD_DELIMETER = "@@";
    
    public void setSaveService( FastSaveTextSerialisationService s ) {
        save = s;
    }
    
    public void setLoadService( FastLoadTextSerialisationService s ) {
        load = s;
    }
    
    public static void clear() {
        cache.clear();
    }
    
    public void setOutput( BufferedWriter o ) {
        out = o;
    }
    
    public void handle( Event e ) {
        if ( e instanceof ObjectOutputEvent )
            write( (ObjectOutputEvent)e );
        else if ( e instanceof ObjectInputEvent )
            read( (ObjectInputEvent)e );
    }
    
    protected void todo( final Identifiable id ) {
        if ( !cache.containsKey( id.getID() ) )
            todo( new ObjectOutputEvent( id ) );
    }
    
    protected void todo( ObjectOutputEvent e ) {
        if ( save == null )
            EventManager.get().fire( e );
        else
            save.todo( e );
    }
    
    protected void todo( ObjectInputEvent e ) {
        if ( load == null )
            EventManager.get().fire( e );
        else
            load.todo( e );
    }
    
    protected void write( String ... vs ) {
        try {
            for ( String s : vs ) 
                out.write( s );
        } catch ( IOException ex ) {
            log.warn( "Unable to write output!", ex );
        }
    }
    
    protected void writeEOL() {
        try {
            out.newLine();
        } catch ( IOException ex ) {
            log.warn( "Unable to write output!", ex );
        }
    }

    @SuppressWarnings( "unchecked" )
    private void write( ObjectOutputEvent e ) {
        if ( !getTarget().isInstance( e.getObject() ) || e.getObject() instanceof IONeurone )
            return; // wrong handler, ignore event
        if ( cache.containsKey( e.getObject().getID() ) ) {
            if ( written.contains( e.getObject().getID() ) ) {
                return; // Already written
            } else { // write a minimal record for the special parts
                T t = (T)e.getObject();
                written.add( t.getID() );
                if ( needsHeader( t ) ) {
                    writeHeader( t );
                    write( FIELD_DELIMETER, OUTER_DELIMETER );
                    writeSpecialParts( t );
                    writeEOL();
                } else {
                    writeSpecialParts( t );
                }
            }
        } else {
            write( (T)e.getObject() );
        }
    }
    
    public void write( T t ) {
        writeHeader( t );
        writeParameters( t );
        write( OUTER_DELIMETER );
        writeSpecialParts( t );
        writeEOL();
        cache.put( t.getID(), t );
        written.add( t.getID() );
    }
    
    protected void writeHeader( T t ) {
        write( String.valueOf( t.getID() ), OUTER_DELIMETER, 
                t.getClass().getCanonicalName(), OUTER_DELIMETER );
    }
    
    protected void writeParameters( T t ) {
        try {
            write( FIELD_DELIMETER );
            for ( Field f : ms.getPersistableMethodsAndFields( t.getClass() ) )
                write( f.getName(), "=", f.get( t ).toString(), FIELD_DELIMETER );
        } catch ( IllegalArgumentException ex ) {
            log.error( "Couldn't write object " + t.getID(), ex );
            write( FIELD_DELIMETER );
        } catch ( IllegalAccessException ex ) {
            log.error( "Couldn't write object " + t.getID(), ex );
            write( FIELD_DELIMETER );
        }
    }
    
    /**
     * Answers true iff this handler will write data in its body and thus
     * needs a header written.
     * @param t The object to write
     * @return
     */
    protected abstract boolean needsHeader( T t );
    
    protected abstract void writeSpecialParts( T t );

    public Identifiable read( String line ) {
        log.trace( "Publically reading " + line );
        return read( new ObjectInputEvent( line ) );
    }
    
    @SuppressWarnings( "unchecked" )
    private Identifiable read( ObjectInputEvent e ) {
        try {
            String[] parts = e.getLine().split( OUTER_DELIMETER, 4 );
            if ( parts.length != 4 ) {
                log.warn( "Invalid line " + e.getLine() + ", cannot continue" );
                return null;
            }
            Integer id = Integer.valueOf( parts[0] );
            if ( cache.containsKey( id ) ) {
                Identifiable cached = cache.get( id );
                if ( !getTarget().isInstance( cached ) )
                    return null; // Not this handler!
                try {
                    readSpecialParts( parts[3], (T)cached );
                } catch ( NotYetAvailableException ex ) {
                    todo( e );
                }
                return cache.get( id ); // Already read in!
            }
            @SuppressWarnings( "unchecked" )
            Class<T> cl = (Class<T>)Class.forName( parts[1] );
            if ( !getTarget().isAssignableFrom( cl ) )
                return null; // Not this handler!
            T t = cl.newInstance();
            t.setID( id );
            readParameters( parts[2], t );
            try {
                readSpecialParts( parts[3], t );
            } catch ( NotYetAvailableException ex ) {
                todo( e );
            }
            
            cache.put( t.getID(), t );
            return t;
        } catch ( InstantiationException ex ) {
            log.warn( "Invalid line " + e.getLine() + ": couldn't create object!", ex );
        } catch ( IllegalAccessException ex ) {
            log.warn( "Invalid line " + e.getLine() + ": couldn't create object!", ex );
        } catch ( ClassNotFoundException ex ) {
            log.warn( "Invalid line " + e.getLine() + ": couldn't create class!", ex );
        }
        return null;
    }
    
    protected void readParameters( String in, T t ) {
        Map<String,String> values = new HashMap<String,String>();
        for ( String field : in.split( FIELD_DELIMETER ) ) {
            if ( field.length() < 3 || !field.contains( "=" ) )
                continue; // discard junk fields
            String[] fv = field.split( "=", 2 );
            values.put( fv[0].toLowerCase(), fv[1] );
        }
        
        for ( Field f : ms.getPersistableMethodsAndFields( t.getClass() ) ) {
            try {
                if ( values.containsKey( f.getName().toLowerCase() ) ) 
                    f.set( t, values.get( f.getName().toLowerCase() ) );
                else
                    log.warn( "No value found for " + f.getName() );
            } catch ( IllegalArgumentException ex ) {
                log.error( "Cannot set value!", ex );
            } catch ( IllegalAccessException ex ) {
                log.error( "Cannot set value!", ex );
            }
        }
    }
    
    protected abstract T readSpecialParts( String in, T t ) throws NotYetAvailableException;
    
    protected abstract Class<T> getTarget();
    
    public void flush() {
        // no-op
    }
    
    public boolean isValid() {
        return true;
    }

}
