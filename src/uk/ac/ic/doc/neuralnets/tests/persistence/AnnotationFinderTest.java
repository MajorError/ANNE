
package uk.ac.ic.doc.neuralnets.tests.persistence;

import java.lang.reflect.Field;
import java.util.Set;
import uk.ac.ic.doc.neuralnets.persistence.MethodSelector;
import uk.ac.ic.doc.neuralnets.graph.neural.Persistable;
import uk.ac.ic.doc.neuralnets.util.configuration.ConfigurationManager;

/**
 *
 * @author Peter Coetzee
 */
public class AnnotationFinderTest {
    
    public static void main( String[] args ) throws IllegalArgumentException, IllegalAccessException {
        new AnnotationFinderTest().test();
    }

    private void test() throws IllegalArgumentException, IllegalAccessException {
        ConfigurationManager.configure();
        AnnotationTestHelper th = new AnnotationTestHelper();
        MethodSelector ms = new MethodSelector();
        Set<Field> persistable = ms.getPersistableMethodsAndFields( AnnotationTestHelper.class );
        for ( Field f : persistable )
            System.out.println( ">> Persist " + f.getName() + ", with value " + f.get( th ) );
    }

    
    public class AnnotationTestHelper {
        @Persistable
        public String something = "This is persistable";
        
        public String hidden = "This field can't be persisted";
        
        @Persistable
        public Integer getValue() {
            return 7;
        }
        
        @Persistable
        public Integer getOtherValue() {
            return 3;
        }
    }
}
