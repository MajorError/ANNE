package uk.ac.ic.doc.neuralnets.util.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import uk.ac.ic.doc.neuralnets.util.plugins.PluginLoadException;
import uk.ac.ic.doc.neuralnets.util.plugins.PluginManager;

/**
 * Configurator to load plugins per the plugins.cfg specification
 * It will copy them from the given location into the plugins directory,
 * creating new parent dirs as needed. It subsequently refreshes the
 * PluginManager to ensure that all remains well.
 * @author Peter Coetzee
 */
public class PluginConfig implements Configurator {
    
    private Logger log = Logger.getLogger( PluginConfig.class.getName() );
    private static final File config = new File( 
            ConfigurationManager.config.getParentFile().getAbsolutePath() 
            + File.separator + "plugins.cfg" );

    public void configure() {
        try {
            Scanner s = new Scanner( config );
            // Type.Name Location
            Pattern p = Pattern.compile( "([a-zA-Z1-9]*)\\.([a-zA-Z1-9]*) (.*);" );
            while( s.hasNext() ) {
                String line = s.nextLine();
                if ( line.startsWith( "#" ) )
                    continue;
                Matcher m = p.matcher( line );
                if ( !m.matches() ) {
                    log.warn( "Unable to parse plugin line " + line 
                                + ", skipping." );
                    continue;
                }
                try {
                    File target = new File( PluginManager.searchPath
                            + File.separator + m.group( 1 )  
                            + File.separator + m.group( 2 ) + ".class" );
                    target.getParentFile().mkdirs();
                    File loc = new File( m.group( 3 ) );
                    /*
                     * Can we load a class by name using the system 
                     * classloader? Would be pretty cool!
                     * TODO: Look into how the ClassLoader finds classes on
                     * the classpath, and whether we can interrogate that...
                     * Failing that, perhaps serialise a loaded Class to disk?
                     *
                    if ( !loc.exists() ) {
                        String r = m.group( 3 ).replaceAll( "\\.", 
                                File.separator.equals( "\\" ) ? 
                                    "\\\\" : File.separator );
                        URL url = c.getSystemResource( r );
                        log.info( "Couldn't find at " + loc 
                                + ", ClassLoader found " + url + " from " 
                                + r + " instead." );
                        loc = new File( url.toString() );
                    }*/
                    byte[] b = new byte[(int)loc.length()];
                    new FileInputStream( loc ).read( b );
                    if ( target.exists() )
                        target.delete();
                    new FileOutputStream( target ).write( b );
                    log.debug( "Loaded " + loc + " into " + target );
                } catch( Exception ex ) {
                    log.warn( "Cannot load " + m.group( 1 ) + "." 
                            + m.group( 2 ) + ", skipping.", ex );
                }
            }
            PluginManager.get().refreshPlugins();
        } catch ( FileNotFoundException ex ) {
            log.error( "Cannot load plugins; " + config.getAbsolutePath()
                    + " doesn't exist!" );
        } catch ( IOException ex ) {
            log.error( "Cannot load plugins from file", ex );
        } catch ( PluginLoadException ex ) {
            log.error( "Unable to get PluginManager!", ex );
        }
    }

    public String getName() {
        return "Plugins";
    }

}
