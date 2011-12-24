package uk.ac.ic.doc.neuralnets.persistence.tns;

import uk.ac.ic.doc.neuralnets.graph.Saveable;
import uk.ac.ic.doc.neuralnets.persistence.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import uk.ac.ic.doc.neuralnets.events.EventManager;
import uk.ac.ic.doc.neuralnets.graph.Identifiable;
import uk.ac.ic.doc.neuralnets.persistence.tns.ObjectIOEventHandler;
import uk.ac.ic.doc.neuralnets.persistence.tns.ObjectOutputEvent;
import uk.ac.ic.doc.neuralnets.util.plugins.PluginLoadException;
import uk.ac.ic.doc.neuralnets.util.plugins.PluginManager;

/**
 *
 * @author Peter Coetzee
 */
public class SaveTextSerialisationService extends SaveService<FileSpecification> {

    private BufferedWriter out;
    
    private static final Logger log = Logger.getLogger( SaveTextSerialisationService.class );

    @Override
    public void save( Saveable toSave, FileSpecification spec ) throws SaveException {
        FileWriter fstream = null;
        try {
            File outputfile = new File( spec.getSavePath() );
            if ( outputfile.exists() )
                outputfile.delete();
            outputfile.createNewFile();
            fstream = new FileWriter( outputfile );
            log.trace( "Writing to '" + outputfile.getCanonicalPath() + "'" );
            out = new BufferedWriter( fstream );
            List<ObjectIOEventHandler> handlers = new ArrayList<ObjectIOEventHandler>();
            log.debug( "Creating handlers for events" );
            for ( String p : PluginManager.get().getPluginsOftype( ObjectIOEventHandler.class ) ) {
                ObjectIOEventHandler h = PluginManager.get().getPlugin( p, ObjectIOEventHandler.class );
                h.setOutput( out );
                handlers.add( h );
                EventManager.get().registerAsync( ObjectOutputEvent.class, h );
            }
            
            log.debug( "Firing initial event" );
            EventManager.get().fire( new ObjectOutputEvent( (Identifiable)toSave ) );
            
            log.debug( "Awaiting completion" );
            int flushing = 0;
            while( flushing < 3 ) {
                flushing += EventManager.get().flush( ObjectOutputEvent.class ) ? 0 : 1;
                try { Thread.sleep( 500 ); } catch ( InterruptedException e ) {}
            }
            for ( ObjectIOEventHandler h : handlers )
                EventManager.get().deregisterAsync( ObjectOutputEvent.class, h );
            ObjectIOEventHandler.clear();
            log.debug( "Complete! All handlers de-registered" );
        } catch ( PluginLoadException ex ) {
            throw new SaveException( "Unable to get save handlers!", ex );
        } catch ( IOException ex ) {
            throw new SaveException( "Unable to save!", ex );
        } finally {
            try {
                out.flush();
                fstream.close();
            } catch ( IOException ex ) {
                log.error( "", ex );
            }
        }
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
