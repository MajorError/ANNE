/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ic.doc.neuralnets.tests.util.plugins;

import java.util.Arrays;
import org.apache.log4j.xml.DOMConfigurator;
import uk.ac.ic.doc.neuralnets.persistence.SaveService;
import uk.ac.ic.doc.neuralnets.util.plugins.PluginLoadException;
import uk.ac.ic.doc.neuralnets.util.plugins.PluginManager;

/**
 *
 * @author Peter Coetzee
 */
public class PluginManagerTest {
    
    public static void main( String[] args ) throws PluginLoadException {
        DOMConfigurator.configure( "conf/log-conf.xml" );
        SaveService s = PluginManager.get().getPlugin( "Serialiser", SaveService.class );
        System.out.println( "I've got me a " + s.getName() + ": " + s );
        System.out.println( Arrays.toString( PluginManager.get().getPluginsOftype( SaveService.class ).toArray() ) );
    }

}
