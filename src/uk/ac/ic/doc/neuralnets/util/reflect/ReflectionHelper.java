
package uk.ac.ic.doc.neuralnets.util.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import sun.reflect.FieldAccessor;
import sun.reflect.ReflectionFactory;

/**
 * Used to perform potentially unsafe reflection - e.g. setting private fields,
 * or getting Fields that backend to Methods.
 * @author Peter Coetzee
 */
public class ReflectionHelper {
    
    /**
     * Get the Sun-JVM-specific ReflectionFactory object (in an unsafe manner). 
     * This allows us to assign values to and read from private Fields
     * @return the ReflectionFactory
     */
    public static final ReflectionFactory getReflectionFactory() {
        return (ReflectionFactory)AccessController.doPrivileged( 
            new sun.reflect.ReflectionFactory.GetReflectionFactoryAction() );
    }
    
    /**
     * Find the requested Field declared in the target object's class, and set 
     * its value (irrespective of the field's modifiers)
     * @param fi The field name to seek
     * @param target The target object
     * @param v The value to set the field to
     * @throws java.lang.IllegalArgumentException
     * @throws java.lang.IllegalAccessException
     */
    public static final void set( String fi, Object target, Object v )
            throws IllegalArgumentException, IllegalAccessException {
        set( target.getClass(), fi, target, v );
    }
    
    /**
     * Find the requested Field declared in the given class, and set its value
     * (irrespective of the field's modifiers)
     * @param c The Class to look in
     * @param fi The field name to seek
     * @param target The target object
     * @param v The value to set the field to
     * @throws java.lang.IllegalArgumentException
     * @throws java.lang.IllegalAccessException
     */
    public static final void set( Class c, String fi, Object target, Object v )
            throws IllegalArgumentException, IllegalAccessException {
        for ( Field f0 : c.getDeclaredFields() )
            if ( f0.getName().equals( fi ) )
                set( f0, target, v );
    }
    
    /**
     * Set the given field on target to value, irrespective of its modifiers
     * @param f The Field to set
     * @param target The object to set it on
     * @param v The value to set the field to
     * @throws java.lang.IllegalArgumentException
     * @throws java.lang.IllegalAccessException
     */
    public static final void set( Field f, Object target, Object v ) 
            throws IllegalArgumentException, IllegalAccessException {
        FieldAccessor fa = getReflectionFactory().newFieldAccessor( f, false );
        fa.set( target, v );
    }
    
    /**
     * Get a Field object which backends data access to the given method name,
     * from the supplied class
     * @param m The name of the method
     * @param c The class to get the method from
     * @return a Field with an accessor that backends to the requested Method
     * @throws NoSuchMethodException 
     * @throws IllegalArgumentException 
     * @throws IllegalAccessException 
     */
    public static final Field getMethodField( String m, Class c ) 
            throws NoSuchMethodException, IllegalArgumentException, 
                   IllegalAccessException {
        // First, get ANY field at all to use as a base
        Field f = c.getDeclaredFields()[0]; 
        // Now set up the internal structures to 'look like' the given method
        // but chop up the name so that it is prettier for output
        if ( m.startsWith( "get" ) || m.startsWith( "set" ) )
            m = m.substring( 3, 4 ).toLowerCase() + m.substring( 4 );
        set( "name", f, m );
        set( "modifiers", f, Modifier.PUBLIC );
        // Finally, override the default FieldAccesors with our own
        FieldAccessor fa = new MethodPseudoAccessor( c, m );
        set( "fieldAccessor", f, fa );
        set( "overrideFieldAccessor", f, fa );
        return f;
    }

}
