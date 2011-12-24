
package uk.ac.ic.doc.neuralnets.persistence;

import uk.ac.ic.doc.neuralnets.graph.neural.Persistable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import uk.ac.ic.doc.neuralnets.util.reflect.ReflectionHelper;

/**
 *
 * @author Peter Coetzee
 */
public class MethodSelector {
    
    private Logger log = Logger.getLogger( MethodSelector.class );
    
    private Map<Class,Set<Field>> pmafCache = new HashMap<Class,Set<Field>>();
    
    public Set<Method> getPersistableMethods( Class c ) {
        Set<Method> out = new HashSet<Method>();
        for ( Method m : c.getMethods() )
            if ( m.getAnnotation( Persistable.class ) != null )
                out.add( m );
        return out;
    }
    
    public Set<Field> getPersistableFields( Class c ) {
        Set<Field> out = new HashSet<Field>();
        for ( Field f : c.getFields() )
            if ( f.getAnnotation( Persistable.class ) != null )
                out.add( f );
        return out;
    }
    
    public Set<Field> getPersistableMethodsAndFields( Class c ) {
        if ( pmafCache.containsKey( c ) )
            return pmafCache.get( c );
        Set<Field> out = getPersistableFields( c );
        for ( Method m : getPersistableMethods( c ) ) {
            try {
                out.add( ReflectionHelper.getMethodField( m.getName(), c ) );
            } catch ( NoSuchMethodException ex ) {
                log.error( null, ex );
            } catch ( IllegalArgumentException ex ) {
                log.error( null, ex );
            } catch ( IllegalAccessException ex ) {
                log.error( null, ex );
            }
        }
        pmafCache.put( c, out );
        return out;
    }

}
