
package uk.ac.ic.doc.neuralnets.tests.expressions;

import uk.ac.ic.doc.neuralnets.expressions.Expression;
import uk.ac.ic.doc.neuralnets.expressions.ExpressionException;

/**
 *
 * @author Peter Coetzee
 */
public class ExpressionTest {
    
    public static void main( String[] args ) throws ExpressionException {
        // -COS(((2 + 8) / 2)) = -0.28366218546322625
        String ex = "-COS(((2 + 8) / 2))";
        Expression e = new Expression( ex );
        System.out.println( "Expression " + ex + " parsed to " + e.evaluate() 
                + " then " + e.evaluate() + " then " + e.evaluate() );
    }

}
