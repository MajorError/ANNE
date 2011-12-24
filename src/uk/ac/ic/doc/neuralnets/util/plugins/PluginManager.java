package uk.ac.ic.doc.neuralnets.util.plugins;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * The PluginManager is responsible for managing the class loading and 
 * instantiation of plugins from the plugins directory. Plugins are loaded and 
 * cached by the PluginLoader.
 * @see PluginLoader
 * @author Peter Coetzee
 */
public class PluginManager {
    
    private static PluginManager m;
    
    /**
     * Path to plugin directory
     */
    public static final File searchPath = new File( "plugins" );
    
    // Mapping from plugin type to name
    private Map<String,Set<String>> plugins = new HashMap<String,Set<String>>();
    private Logger log = Logger.getLogger( PluginManager.class.getName() );
    private ClassLoader cl = new PluginLoader( searchPath.getAbsolutePath() );
    
    /**
     * Retrieve the instance of the PluginManager.
     * @return the PluginManager instance
     * @throws PluginLoadException
     */
    public static PluginManager get() throws PluginLoadException {
        if ( m == null )
            m = new PluginManager();
        return m;
    }
    
    private PluginManager() throws PluginLoadException {
        // Not externally instantiable
        if ( !searchPath.exists() || !searchPath.isDirectory() )
            throw new PluginLoadException( "Plugin directory " 
                    + searchPath.getAbsolutePath() + " doesn't exist!" );
        refreshPlugins();
    }
    
    public void refreshPlugins() {
        loadPlugins( searchPath );
    }
    
    private void loadPlugins( File d ) {
        for( File f : d.listFiles() ) {
            if ( f.isDirectory() ) {
                loadPlugins( f );
            } else {
                String name = f.getName();
                if ( !name.endsWith( ".class" ) )
                    continue; // skip non-class files silently
                name = name.substring( 0, name.length() - 6 );
                String type = f.getParentFile().getAbsolutePath().substring( 
                            searchPath.getAbsolutePath().length() + 1
                        ).replaceAll( File.pathSeparator, "." );
                Set<String> ps;
                if ( plugins.containsKey( type ) ) {
                    ps = plugins.get( type );
                } else {
                    ps = new HashSet<String>();
                    plugins.put( type, ps );
                }
                ps.add( name );
            }
        }
    }
    
    /**
     * Load the requested plugin and cast it to the given class
     * @param name The name of the plugin
     * @param type The type of the plugin to fetch
     * @return A Plugin object of the given name and type
     */
    public Plugin getPlugin( String name, String type ) throws PluginLoadException {
        try {
            Class c = cl.loadClass( type + "." + name );
            if ( !Plugin.class.isAssignableFrom( c ) )
                throw new PluginLoadException( "Unable to load plugin " + name 
                        + ", as it is not a Plugin!" );
            Plugin p = Plugin.class.cast( c.newInstance() );
            if ( p.getName().equals( name ) )
                return p;
            else
                throw new PluginLoadException( "Invalid plugin name! Loaded " 
                        + p.getName() + ", but was looking for " + name );
        } catch ( InstantiationException ex ) {
            log.log( Level.ERROR, null, ex );
            throw new PluginLoadException( ex );
        } catch ( IllegalAccessException ex ) {
            log.log( Level.ERROR, null, ex );
            throw new PluginLoadException( ex );
        } catch ( ClassNotFoundException ex ) {
            log.log( Level.ERROR, null, ex );
            throw new PluginLoadException( ex );
        }
    }    
    
    /**
     * Load the requested plugin and cast it to the given class
     * @param <T> The requested type of plugin
     * @param name The name of the plugin
     * @param clazz The class to which it must be cast
     * @return A Plugin object of type T
     */
    public <T extends Plugin> T getPlugin( String name, Class<T> clazz ) throws PluginLoadException {
        try {
            log.debug( "Loading " + clazz.getSimpleName() + " :: " + name );
            return clazz.cast( getPlugin( name, clazz.getSimpleName() ) );
        } catch ( IllegalArgumentException ex ) {
            log.log( Level.ERROR, null, ex );
            throw new PluginLoadException( ex );
        } catch ( SecurityException ex ) {
            log.log( Level.ERROR, null, ex );
            throw new PluginLoadException( ex );
        }
    }
    
    /**
     * Answer all the plugins of the given type
     * @param type The type of the plugin to find
     * @return A set of plugin names
     */
    public Set<String> getPluginsOfType( String type ) {
        return plugins.containsKey( type ) ? 
            plugins.get( type ) : new HashSet<String>();
    }
    
    /**
     * Answer all the plugins of the given type
     * @param <T> The type of the plugin to find
     * @param clazz The class of the type of plugin to find
     * @return A set of plugin names
     */
    public <T extends Plugin> Set<String> getPluginsOftype( Class<T> clazz ) {
        return getPluginsOfType( clazz.getSimpleName() );
    }
    
    /**
     * Check the validity of all the plugins in this PluginManager. If any have
     * been loaded that are invalid, remove them from this PluginManager
     */
    public void checkValidity() {
        for ( String type : plugins.keySet() )
            checkValidity( type );
    }
   
    /**
     * Check the validity of all the plugins of the given type. If any have 
     * been loaded that are invalid, remove them from this PluginManager
     * @param clazz The class of the plugin type
     */
    public <T extends Plugin> void checkValidity( Class<T> clazz ) {
        checkValidity( clazz.getSimpleName() );
    }
    
    /**
     * Check the validity of all the plugins of the given type. If any have
     * been loaded that are invalid, remove them from this PluginManager
     * @param type The type name of the plugin
     */
    public void checkValidity( String type ) {
        if ( !plugins.containsKey( type ) )
            return;
        for ( String name : plugins.get( type )) {
            try {
                if ( getPlugin( name, type ) == null )
                    plugins.get( type ).remove( name );
            } catch ( Exception e ) {
                plugins.get( type ).remove( name );
            }
        }
    }

}
