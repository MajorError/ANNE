package uk.ac.ic.doc.neuralnets.persistence;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.log4j.Logger;
import uk.ac.ic.doc.neuralnets.events.EventManager;
import uk.ac.ic.doc.neuralnets.graph.Edge;
import uk.ac.ic.doc.neuralnets.graph.Graph;
import uk.ac.ic.doc.neuralnets.graph.Identifiable;
import uk.ac.ic.doc.neuralnets.graph.Node;
import uk.ac.ic.doc.neuralnets.graph.Saveable;
import uk.ac.ic.doc.neuralnets.graph.neural.NetworkBridge;
import uk.ac.ic.doc.neuralnets.graph.neural.NeuralNetwork;
import uk.ac.ic.doc.neuralnets.util.plugins.Plugin;
import uk.ac.ic.doc.neuralnets.util.plugins.PluginLoadException;
import uk.ac.ic.doc.neuralnets.util.plugins.PluginManager;

/**
 *
 * @author Peter Coetzee
 */
public class NeuralNetworkAssistance extends PersistenceAssistance {
    
    private static final Logger log = Logger.getLogger( NeuralNetworkAssistance.class );
    
    private int todo = 0;
    
    public void setSaveTarget( Saveable o ) {
        if ( !( o instanceof Graph || o instanceof Node ) )
            throw new UnsupportedOperationException( "Cannot assist with Saveable " + o.getClass() );
        todo = 0;
        if ( o instanceof Graph )
            toSave( (Graph)o );
        else if ( o instanceof Node )
            todo = 1;
    }
    
    private void toSave( Graph g ) {
        todo++;
        for ( Node<?> n : g.getNodes() )
            if ( n instanceof Graph )
                toSave( (Graph)n );
            else
                todo++;
        for ( Edge<?, ?> e : g.getEdges() )
            todo++;
    }
    
    public void setObjectsToLoad( int objects ) {
        todo = objects;
    }
    
    public void done( int num ) {
        EventManager.get().fire( new PersistenceProgressEvent( num, todo ) );
    }
    
    public void fixIDs( Saveable s ) {
        if ( s instanceof Identifiable )
            fixIDs( (Identifiable)s );
    }

    public void fixIDs( Identifiable o ) {
        o.getFreshID();
        if ( o instanceof NeuralNetwork ) {
            for ( Node<?> n : ((NeuralNetwork)o).getNodes() )
                fixIDs( n );
            for ( Edge<?, ?> e : ((NeuralNetwork)o).getEdges() ) 
                e.getFreshID();
            for ( NetworkBridge nb : ((NeuralNetwork)o).getOutgoing() )
                nb.getFreshID();
        }
    }

    public String inferLoadPlugin( String filename ) {
        return infer( filename, LoadService.class );
    }

    public String inferSavePlugin( String filename ) {
        return infer( filename, SaveService.class );
    }
    
    protected String infer( String filename, Class<? extends Plugin> c ) {
        try {
            Collection<Plugin> ps = new ArrayList<Plugin>();
            for ( String p : PluginManager.get().getPluginsOftype( c ) )
                ps.add( PluginManager.get().getPlugin( p, c ) );
            return infer( filename, ps );
        } catch ( PluginLoadException ex ) {
            log.error( "Couldn't perform inference: " + ex.getMessage(), ex );
        }
        return "NONE";
    }
    
    protected String infer( String filename, Collection<Plugin> plugins ) {
        String[] parts = filename.split( "\\." );
        String ext = "*." + parts[parts.length - 1].toLowerCase();
        String plugin = "NONE";
        Method m = null;
        for ( Plugin p : plugins ) {
            if ( m == null ) {
                try {
                    m = p.getClass().getMethod( "getExtension" );
                } catch ( NoSuchMethodException ex ) {
                    log.error( "Couldn't perform inference: " + ex.getMessage(), ex );
                } catch ( SecurityException ex ) {
                    log.error( "Couldn't perform inference: " + ex.getMessage(), ex );
                }
            }
            try {
                if ( m.invoke( p, ext ).toString().equalsIgnoreCase( ext ) ) {
                    plugin = p.getName();
                    break;
                }
            } catch ( IllegalAccessException ex ) {
                    log.error( "Couldn't perform inference: " + ex.getMessage(), ex );
            } catch ( IllegalArgumentException ex ) {
                    log.error( "Couldn't perform inference: " + ex.getMessage(), ex );
            } catch ( InvocationTargetException ex ) {
                    log.error( "Couldn't perform inference: " + ex.getMessage(), ex );
            }
        }
        log.debug( "Seeking plugin for extension " + ext + " rendered " + plugin );
        return plugin;
    }

}
