package uk.ac.ic.doc.neuralnets.graph.neural;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;
import org.apache.log4j.Logger;
import uk.ac.ic.doc.neuralnets.util.configuration.ConfigurationManager;
import uk.ac.ic.doc.neuralnets.util.configuration.Configurator;
import uk.ac.ic.doc.neuralnets.util.plugins.PluginLoadException;
import uk.ac.ic.doc.neuralnets.util.plugins.PluginManager;

/**
 * Configurator to load Statisticians
 * @author Peter Coetzee
 */
public class NeuroneTypeConfig implements Configurator {
    
    private Logger log = Logger.getLogger( NeuroneTypeConfig.class.getName() );
    private File config = new File( 
            ConfigurationManager.config.getParentFile().getAbsolutePath() 
            + File.separator + "nodetypes.cfg" );

    @SuppressWarnings( "unchecked" )
    public void configure() {
        try {
            Scanner s = new Scanner( config );
            String line = s.nextLine();
            while( s.hasNext() ) {
                if ( line.startsWith( "#" ) ) {
                    line = s.nextLine();
                    continue;
                }
                String[] curr = null;
                try {
                    curr = line.substring( 1, line.length() - 1 ).split( "\\|" );
                    List<String> params = new ArrayList<String>();
                    List<String> values = new ArrayList<String>();
                    EdgeDecoration ed = null;
                    while( s.hasNext() ) {
                        line = s.nextLine();
                        if ( line.startsWith( "[" ) )
                            break;
                        if ( line.length() > 0 ) {
                            String[] parts = line.split( "=" );
                            if ( parts[0].equals( NeuroneTypes.EDGE_DECORATION_NAME ) ) {
                                try {
                                     ed = PluginManager.get().getPlugin( 
                                             parts[1], EdgeDecoration.class );
                                } catch ( PluginLoadException e ) {
                                    log.warn( "Error loading edge decoration: " + e.getMessage(), e );
                                }
                                continue;
                            }
                            params.add( parts[0] );
                            values.add( parts.length > 1 ? parts[1] : "0" );
                        }
                    }
                    log.debug( "Attempting to load " + curr[0] + " as " + curr[1] );
                    NeuroneTypes.nodeTypes.put( curr[0], 
                          (Class<? extends Neurone>)Class.forName( curr[1] ) );
                    NeuroneTypes.nodeDecorations.put( curr[0], ed );
                    NeuroneTypes.nodeParams.put( curr[0], params );
                    NeuroneTypes.paramValues.put( curr[0], values );
                    log.info( "Loaded " + curr[0] + " with class " + curr[1] 
                            + " and params " + params 
                            + " with edge decoration " + ed );
                } catch ( Exception ex ) {
                    if ( curr == null )
                        log.error( "Cannot load Node!", ex );
                    else
                        log.error( "Cannot load Node " + curr[0], ex );
                }
            }
        } catch ( FileNotFoundException ex ) {
            log.error( "Cannot load Node Types; " + config.getAbsolutePath()
                    + " doesn't exist!" );
        } catch ( IOException ex ) {
            log.error( "Cannot load node types from file", ex );
        }
    }
    
    public void commitConfiguration() throws IOException {
        log.debug( "Committing neurone types to disk" );
        config.delete();
        config.createNewFile();
        FileWriter f = new FileWriter( config );
        f.write( "# Collection of Node Types (delimited by [name|class]) and their respective properties\n" +
                 "# Anything after the '=' is assumed to be a valid expression for the default value\n" +
                 "# of the parameter. If omitted, it defaults to '0'\n" );
        for ( Entry<String, Class<? extends Neurone>> e : NeuroneTypes.nodeTypes.entrySet() ) {
            StringBuilder b = new StringBuilder();
            b.append( "[" + e.getKey() + "|" + e.getValue().getCanonicalName() + "]\n" );
            b.append( NeuroneTypes.EDGE_DECORATION_NAME + "=" 
                    + NeuroneTypes.nodeDecorations.get( e.getKey() ).getName() + "\n" );
            Iterator<String> vals = NeuroneTypes.paramValues.get( 
                    e.getKey() ).iterator();
            for ( String param : NeuroneTypes.nodeParams.get( e.getKey() ) )
                b.append( param + "=" + vals.next() + "\n" );
            f.write( b.toString() + "\n" );
            log.debug( "Wrote " + e.getKey() );
        }
        f.close();
        log.debug( "Commit complete" );
    }

    public String getName() {
        return "NeuroneTypes";
    }

}
