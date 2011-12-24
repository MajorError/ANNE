package uk.ac.ic.doc.neuralnets.tests.util;

import uk.ac.ic.doc.neuralnets.events.EventManager;
import uk.ac.ic.doc.neuralnets.util.configuration.ConfigurationManager;

/**
 *
 * @author Peter Coetzee
 */
public class ConfiguratorTest {
    
    public static void main( String[] args ) {
        ConfigurationManager.configure();
        EventManager.get().flushAll();
    }

}
