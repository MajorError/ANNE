package uk.ac.ic.doc.neuralnets.util.plugins;

/**
 * Throw when there are unrecoverable errors whilst attempting to instantiate a 
 * plugin.
 * @author Peter Coetzee
 */
public class PluginLoadException extends Exception {
    
    public static final long serialVersionUID = -2394878932l;
    
    public PluginLoadException( String m ) {
        super( m );
    }
    
    public PluginLoadException( Throwable e ) {
        super( e );
    }
    
    public PluginLoadException( String m, Throwable e ) {
        super( m, e );
    }

}
