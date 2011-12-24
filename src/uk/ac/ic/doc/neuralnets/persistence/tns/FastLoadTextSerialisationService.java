package uk.ac.ic.doc.neuralnets.persistence.tns;

import uk.ac.ic.doc.neuralnets.graph.Saveable;
import uk.ac.ic.doc.neuralnets.persistence.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import org.apache.log4j.Logger;
import uk.ac.ic.doc.neuralnets.util.plugins.PluginLoadException;
import uk.ac.ic.doc.neuralnets.util.plugins.PluginManager;

/**
 *
 * @author Peter Coetzee
 */
public class FastLoadTextSerialisationService extends LoadService<FileSpecification> {
    
    private static final Logger log = Logger.getLogger( FastLoadTextSerialisationService.class );
    
    private Queue<ObjectInputEvent> todo = new LinkedList<ObjectInputEvent>();
    private List<ObjectIOEventHandler> handlers = new ArrayList<ObjectIOEventHandler>();
    private PersistenceAssistance pa = PersistenceAssistance.newInstance();

    @Override
    public Saveable load( FileSpecification spec ) throws LoadException {
        FileReader fstream = null;
        Saveable output = null;
        try {
            File outputfile = new File( spec.getSavePath() );
            fstream = new FileReader( outputfile );
            log.trace( "Reading from '" + outputfile.getCanonicalPath() + "'" );
            BufferedReader in = new BufferedReader( fstream );
            
            NodeIOHandler nh = null;
            log.debug( "Creating handlers for events" );
            for ( String p : PluginManager.get().getPluginsOftype( ObjectIOEventHandler.class ) ) {
                ObjectIOEventHandler h = PluginManager.get().getPlugin( p, ObjectIOEventHandler.class );
                if ( h instanceof NodeIOHandler )
                    nh = (NodeIOHandler)h;
                handlers.add( h );
                h.setLoadService( this );
            }
            
            log.debug( "Begin read stream" );
            if ( !in.ready() )
                throw new LoadException( "Cannot load from empty file!" );
            String s = in.readLine();
            output = (Saveable)nh.read( s );
            todo( new ObjectInputEvent( s ) );
            
            int tobedone = 1;
            log.debug( "Created top level: " + output + ". Now for the event stream" );
            while( in.ready() ) {
                todo( new ObjectInputEvent( in.readLine() ) );
                tobedone++;
            }
            pa.setObjectsToLoad( tobedone++ );
            
            log.debug( "Flushing buffer" );
            int done = 0;
            while( todo.size() > 0 ) {
                ObjectInputEvent e = todo.poll();
                if ( e != null )
                    for ( ObjectIOEventHandler h : handlers )
                        h.handle( e );
                if ( done++ % 20 == 0 )
                    pa.done( done );
            }
            
            ObjectIOEventHandler.clear();
			pa.fixIDs( output );
            log.debug( "Complete and de-registered" );
        } catch ( PluginLoadException ex ) {
            throw new LoadException( "Unable to get save handlers!", ex );
        } catch ( IOException ex ) {
            throw new LoadException( "Unable to save!", ex );
        } finally {
            try {
                fstream.close();
            } catch ( IOException ex ) {
                log.error( "", ex );
            }
        }
        return output;
    }
    
    public void todo( ObjectInputEvent e ) {
        todo.add( e );
    }

    @Override
    public String getFileType() {
        return "*.tns";
    }

    @Override
    public int getPriority() {
        return 2;
    }

    public String getName() {
        return "TextNetworkSerialiser";
    }
    
}
