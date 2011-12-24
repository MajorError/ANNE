
package uk.ac.ic.doc.neuralnets.tests.util;

import java.lang.reflect.Field;
import sun.reflect.FieldAccessor;
import uk.ac.ic.doc.neuralnets.tests.util.reflection.ReflectionTestHelper;
import uk.ac.ic.doc.neuralnets.util.reflect.MethodPseudoAccessor;
import uk.ac.ic.doc.neuralnets.util.reflect.ReflectionHelper;

/**
 *
 * @author Peter Coetzee
 */
public class ReflectionTest {
    
    public static void main( String[] args ) throws IllegalArgumentException, 
            IllegalAccessException, ClassNotFoundException, 
            InstantiationException, NoSuchMethodException {
        ReflectionTestHelper h = new ReflectionTestHelper();
        System.out.println( h );
        ReflectionHelper.set( "s", h, "New Value" );
        System.out.println( h );
        Field fi = ReflectionHelper.getMethodField( "toString", h.getClass() );
        System.out.println( "Field 'toString':: " + fi.get( h ) );
    }

}
