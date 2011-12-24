package uk.ac.ic.doc.neuralnets.persistence.tns;

import uk.ac.ic.doc.neuralnets.graph.Saveable;
import uk.ac.ic.doc.neuralnets.persistence.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import uk.ac.ic.doc.neuralnets.events.EventManager;
import uk.ac.ic.doc.neuralnets.graph.Identifiable;
import uk.ac.ic.doc.neuralnets.persistence.tns.NodeIOHandler;
import uk.ac.ic.doc.neuralnets.persistence.tns.ObjectIOEventHandler;
import uk.ac.ic.doc.neuralnets.persistence.tns.ObjectInputEvent;
import uk.ac.ic.doc.neuralnets.util.plugins.PluginLoadException;
import uk.ac.ic.doc.neuralnets.util.plugins.PluginManager;

/**
 *
 * @author Peter Coetzee
 */
public class LoadTextSerialisationService extends LoadService<FileSpecification> {
    
    private static final Logger log = Logger.getLogger( LoadTextSerialisationService.class  );

    @Override
    public Saveable load( FileSpecification spec ) throws LoadException {
        FileReader fstream = null;
        Saveable output = null;
        try {
            File outputfile = new File( spec.getSavePath() );
            fstream = new FileReader( outputfile );
            log.trace( "Reading from '" + outputfile.getCanonicalPath() + "'" );
            BufferedReader in = new BufferedReader( fstream );
            List<ObjectIOEventHandler> handlers = new ArrayList<ObjectIOEventHandler>();
            NodeIOHandler nh = null;
            log.debug( "Creating handlers for events" );
            for ( String p : PluginManager.get().getPluginsOftype( ObjectIOEventHandler.class ) ) {
                ObjectIOEventHandler h = PluginManager.get().getPlugin( p, ObjectIOEventHandler.class );
                if ( h instanceof NodeIOHandler )
                    nh = (NodeIOHandler)h;
                handlers.add( h );
                EventManager.get().registerAsync( ObjectInputEvent.class, h );
            }
            
            log.debug( "Begin read stream" );
            if ( !in.ready() )
                throw new LoadException( "Cannot load from empty file!" );
            output = (Saveable)nh.read( in.readLine() );
            
            log.debug( "Created top level: " + output + ". Now for the event stream" );
            while( in.ready() )
                EventManager.get().fire( new ObjectInputEvent( in.readLine() ) );
            
            log.debug( "Flushing buffer" );
            int flushing = 0;
            while( flushing < 3 ) {
                flushing += EventManager.get().flush( ObjectInputEvent.class ) ? 0 : 1;
                try { Thread.sleep( 500 ); } catch ( InterruptedException e ) {}
            }
            for ( ObjectIOEventHandler h : handlers )
                EventManager.get().deregisterAsync( ObjectInputEvent.class, h );
            ObjectIOEventHandler.clear();
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
