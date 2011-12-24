package uk.ac.ic.doc.neuralnets.tests.plugins;

import java.lang.reflect.Constructor;
import uk.ac.ic.doc.neuralnets.util.configuration.ConfigurationManager;
import uk.ac.ic.doc.neuralnets.util.plugins.PluginManager;

/**
 *
 * @author Peter Coetzee
 */
public class TestPluginWithInnerClasses {
    
    public static void main( String[] args ) throws Exception {
        ConfigurationManager.configure();
        System.err.println( PluginManager.get()
                .getPlugin( "PluginWithInnerClasses", TestPluginType.class ) );
        System.err.println( PluginManager.get()
                .getPlugin( "PluginWithInnerClasses", AnotherTestPluginType.class ) );
        System.exit( 0 );
    }

}
