
package uk.ac.ic.doc.neuralnets.tests.util;

import java.io.IOException;
import uk.ac.ic.doc.neuralnets.util.configuration.ConfigurationManager;
import uk.ac.ic.doc.neuralnets.graph.neural.NeuroneTypeConfig;

/**
 *
 * @author Peter Coetzee
 */
public class NeuroneTypeConfigCommitTest {
    
    public static void main( String[] args ) throws IOException {
        ConfigurationManager.configure();
        new NeuroneTypeConfig().commitConfiguration();
        System.exit( 0 );
    }

}
