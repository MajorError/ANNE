
package uk.ac.ic.doc.neuralnets.tests.expressions;

import org.apache.log4j.xml.DOMConfigurator;
import uk.ac.ic.doc.neuralnets.expressions.BindVariable;
import uk.ac.ic.doc.neuralnets.expressions.ExpressionException;
import uk.ac.ic.doc.neuralnets.expressions.ast.ASTExpression;

/**
 *
 * @author Peter Coetzee
 */
public class ASTExpressionTest {
    
    public ASTExpressionTest() throws ExpressionException {
        //ASTExpression e = new ASTExpression( "RAND() * -myVariable + x" );
        //ASTExpression e = new ASTExpression( "(4 + RAND()) * TANH((RAND() + (6 * y + 5)) / -x)" );
        //ASTExpression e = new ASTExpression( "(10 * SQRT(1 + 100 ^ 2)) / (100 * SQRT(100 + 100^2) * SQRT(50 ^ 2 + 100 ^ 2))" );
        //e.bind( new VariableBindingTest() );
        ASTExpression e = new ASTExpression( "(PI * (15^2) * 3)" );
        System.out.println( e + " parsed to " + e.evaluate() 
                + " then " + e.evaluate() + " then " + e.evaluate() );
        /*System.out.println( "Programmatic eval: " + Math.tanh( 65 * -5 ) );*/
    }
    
    public static void main( String[] args ) throws ExpressionException {
        DOMConfigurator.configure( "conf/log-conf.xml");
        new ASTExpressionTest();
        System.exit( 0 );
    }
    
    public class VariableBindingTest {
        
        @BindVariable( "x" )
        public Double getX() {
            return 5d;
        }
        
        @BindVariable( "y" )
        public Double getY() {
            return 1000d;
        }
    }

}
