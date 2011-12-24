package uk.ac.ic.doc.neuralnets.persistence.tns;

import uk.ac.ic.doc.neuralnets.graph.Saveable;
import uk.ac.ic.doc.neuralnets.persistence.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import org.apache.log4j.Logger;
import uk.ac.ic.doc.neuralnets.graph.Identifiable;
import uk.ac.ic.doc.neuralnets.util.plugins.PluginLoadException;
import uk.ac.ic.doc.neuralnets.util.plugins.PluginManager;

/**
 *
 * @author Peter Coetzee
 */
public class FastSaveTextSerialisationService extends SaveService<FileSpecification> {

    private BufferedWriter out;
    
    private static final Logger log = Logger.getLogger( FastSaveTextSerialisationService.class  );
    
    private Queue<ObjectOutputEvent> todo = new LinkedList<ObjectOutputEvent>();
    private List<ObjectIOEventHandler> handlers = new ArrayList<ObjectIOEventHandler>();
    private PersistenceAssistance pa = PersistenceAssistance.newInstance();

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
            
            log.debug( "Creating handlers for events" );
            for ( String p : PluginManager.get().getPluginsOftype( ObjectIOEventHandler.class ) ) {
                ObjectIOEventHandler h = PluginManager.get().getPlugin( p, ObjectIOEventHandler.class );
                h.setOutput( out );
                h.setSaveService( this );
                handlers.add( h );
            }
            
            pa.setSaveTarget( toSave );
            
            log.debug( "Firing initial event" );
            todo.add( new ObjectOutputEvent( (Identifiable)toSave ) );
            log.debug( "Awaiting completion" );
            int done = 0;
            while( todo.size() > 0 ) {
                ObjectOutputEvent e = todo.poll();
                if ( e != null )
                    for ( ObjectIOEventHandler h : handlers )
                        h.handle( e );
                if ( done++ % 20 == 0 )
                    pa.done( done );
            }
            
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
    
    public void todo( ObjectOutputEvent e ) {
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
