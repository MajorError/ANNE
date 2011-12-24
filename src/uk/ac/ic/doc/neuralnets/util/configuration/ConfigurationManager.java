package uk.ac.ic.doc.neuralnets.util.configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import uk.ac.ic.doc.neuralnets.util.plugins.PluginLoadException;
import uk.ac.ic.doc.neuralnets.util.plugins.PluginManager;

/**
 * The ConfigurationManager controls Configurator objects, calling their 
 * <code>configure</code> methods at application load time.
 * 
 * @author Peter Coetzee
 */
public class ConfigurationManager {
    
	/**
	 * Master configuration file.
	 */
    public static final File config = new File( "conf" + File.separator
            + "configurator.cfg" );
    
    private static Logger log 
            = Logger.getLogger( ConfigurationManager.class.getName() );
    
    /**
     * Configure all configurators found in conf/configurator.cfg.
     */
    public static void configure() {
        //DOMConfigurator.configure( "conf/log-conf.xml" );
        if ( !config.exists() || !config.isFile() ) {
            DOMConfigurator.configure( "conf/log-conf.xml" );
            log.warn("No config file found!");
            return;
            /*throw new RuntimeException( "Dynamic configuration file " 
                    + config.getAbsolutePath() + " doesn't exist!" );*/
        }
        try {
            Scanner s = new Scanner( config );
            PluginManager m = PluginManager.get();
            while( s.hasNext() ) {
                String l = s.nextLine();
                if ( l.startsWith( "#" ) )
                    continue;
                try {
                    m.getPlugin( l, Configurator.class ).configure();
                } catch ( PluginLoadException ex ) {
                    log.error( "Error loading configurator", ex );
                }
                log.info( "Configured " + l );
            }
            log.info( "Configuration complete" );
        } catch ( FileNotFoundException ex ) {
            log.error( "Unable to read dynamic config!", ex );
        } catch ( PluginLoadException ex ) {
            log.error( "Error loading PluginManager", ex );
        } 
    }
}
