
package uk.ac.ic.doc.neuralnets.tests.expressions;

import org.apache.log4j.xml.DOMConfigurator;
import uk.ac.ic.doc.neuralnets.expressions.BindVariable;
import uk.ac.ic.doc.neuralnets.expressions.Expression;
import uk.ac.ic.doc.neuralnets.expressions.ExpressionException;
import uk.ac.ic.doc.neuralnets.util.configuration.ConfigurationManager;

/**
 *
 * @author Peter Coetzee
 */
public class VariableExpressionTest {
    
    public VariableExpressionTest() throws ExpressionException {
        Expression e = new Expression( "RAND() * -myVariable + x" );
        e.bind( new VariableBindingTest() );
        System.out.println( e + " parsed to " + e.evaluate() 
                + " then " + e.evaluate() + " then " + e.evaluate() );
    }
    
    public static void main( String[] args ) throws ExpressionException {
        DOMConfigurator.configure( "conf/log-conf.xml");
        new VariableExpressionTest();
        System.exit( 0 );
    }
    
    public class VariableBindingTest {
        
        @BindVariable( "myVariable" )
        public Double getX() {
            return 5d;
        }
        
        @BindVariable( "x" )
        public Double getY() {
            return 1000d;
        }
    }

}
