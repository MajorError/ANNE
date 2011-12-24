package uk.ac.ic.doc.neuralnets.util.plugins;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * The PluginLoader is responsible for loading plugin class files from the 
 * /plugin  directory into the virtual machine.
 * @author Peter Coetzee
 */
public class PluginLoader extends ClassLoader {
    
    private Logger log = Logger.getLogger( PluginLoader.class.getCanonicalName() );
    private Map<String,Class> cache = new HashMap<String,Class>();
    private String searchPath;
    
    public PluginLoader( String searchPath ) {
        this.searchPath = searchPath;
    }

    @Override
    public Class findClass( String name ) throws ClassNotFoundException {
        log.debug( name );
        if ( cache.containsKey( name ) )
            return cache.get( name );
        String sep = File.separator.equals( "\\" ) ? 
            "\\\\" : File.separator;
        byte[] b = loadClassData( name.replaceAll( "\\.", sep ) );
        if ( b == null )
            throw new ClassNotFoundException( name );
        Class c = null;
        try {
            c = defineClass( null, b, 0, b.length );
            c = defineClass( c.getCanonicalName(), b, 0, b.length );
        } catch ( LinkageError ex ) {
            String msg = ex.getMessage();
            Pattern p = Pattern.compile( "^.* [\"]?([^ \"]+)[\"]?$" );
            Matcher m = p.matcher( msg );
            if ( m.matches() ) {
                String n = m.group( 1 ).replaceAll( "/", "." );
                log.debug( ex.getMessage() + "  -->  '" + n + "'" );
                c = cache.get( n );
                if ( c == null )
                    c = Class.forName( n );
            } else {
                throw ex;
            }
        }
        cache( name.substring( name.indexOf( '.' ) + 1 ), c, c );
        return c;
    }
    
    private void cache( String name, Class c, Class type ) {
        log.debug( "Caching " + c + " as " + name + " in " + type );
        cache.put( name, c );
        cache.put( type.getCanonicalName() + "." + name, c );
        for ( Class s : type.getInterfaces() )
            if ( Plugin.class.isAssignableFrom( s ) )
                cache( name, c, s );
    }

    private byte[] loadClassData( String name ) {
        try {
            File f = new File( searchPath + File.separator + name + ".class" );
            log.debug( "Seeking for " + f.getAbsolutePath() );
            if ( !f.exists() ) 
                return null;
            FileInputStream fi = new FileInputStream( f );
            byte[] b = new byte[(int)f.length()];
            fi.read( b );
            return b;
        } catch ( IOException ex ) {
            log.log( Level.ERROR, null, ex );
        }
        return null;
    }
}
