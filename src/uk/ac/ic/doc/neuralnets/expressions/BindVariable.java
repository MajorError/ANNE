package uk.ac.ic.doc.neuralnets.expressions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Peter Coetzee
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.METHOD )
public @interface BindVariable {
    
    /**
     * The variable name to bind the annotated method to
     */
    String value();
    
    /**
     * Whether or not an Expression should rebind this method each time it is
     * evaluated. Defaults to false.
     */
    boolean rebind() default false;
    
}
