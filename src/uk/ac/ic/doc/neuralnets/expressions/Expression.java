
package uk.ac.ic.doc.neuralnets.expressions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.apache.log4j.Logger;

/**
 *
 * @author Peter Coetzee
 */
public class Expression {
    
    private CalculationParser p;
    private String expr;
    private static final Logger log = Logger.getLogger( Expression.class );
    private Map<String,Method> bindings = new HashMap<String,Method>();
    private Object target;
    
    /**
     * Create an Expression to encode the given value
     * @param value The value returned by this Expression
     */
    public Expression( Double value ) {
        expr = value.toString();
        p = getParser( expr ); // can't not parse!
    }
    
    /**
     * Create an Expression for the given string 
     * @param expr The expression to represent
     */
    public Expression( String expr ) {
        p = getParser( expr );
        this.expr = expr;
    }
    
    /**
     * Evaluate the expression after refreshing its current bindings
     * @return The value this expression evaluates to
     * @throws uk.ac.ic.doc.neuralnets.expressions.ExpressionException
     */
    public Double evaluate() throws ExpressionException {
        // Re-bind variables
        for ( Entry<String,Method> e : bindings.entrySet() )
            bind( e.getKey(), e.getValue() );
        double out = p.evaluate();
        p.reset();
        return out;
    }
    
    /**
     * Re-bind variables, then evaluate the expression
     * @param o The object to bind variables from
     * @return The value this expression evaluates to
     * @throws uk.ac.ic.doc.neuralnets.expressions.ExpressionException
     */
    public Double evaluate( Object o ) throws ExpressionException {
        bind( o );
        return evaluate();
    }
    
    /**
     * Manually bind a variable in the expression
     * @param var The variable to bind
     * @param val The value to bind to
     */
    public void bind( String var, Double val ) {
        p.bind( var, val );
    }
    
    /**
     * Bind variables according to BindVariable annotations present in this
     * object, and all of its super-classes
     * @param o The object to bind variables from
     */
    public void bind( Object o ) {
        bindings.clear();
        target = o;
        for ( Method m : o.getClass().getMethods() ) {
            BindVariable bv = m.getAnnotation( BindVariable.class );
            if ( bv == null )
                continue;
            if ( bv.rebind() )
                bindings.put( bv.value(), m );
            else
                bind( bv.value(), m );
        }
    }
    
    protected void bind( String var, Method m ) {
        try {
            //log.trace(  "Binding " + var + " using " + m.getName() );
            Object d =  m.invoke( target );
            if ( !(d instanceof Double) ) // Try to parse it
                d = Double.valueOf( d.toString() );
            p.bind( var, (Double)d );
        } catch ( IllegalAccessException ex ) {
            log.error( "Error binding variable " + var + "!", ex );
        } catch ( IllegalArgumentException ex ) {
            log.error( "Error binding variable " + var + "!", ex );
        } catch ( InvocationTargetException ex ) {
            log.error( "Error binding variable " + var + "!", ex );
        }
    }
    
    /**
     * Answer the input expression
     * @return The mathematical expression encoded by this object
     */
    public String getExpression() {
        return expr;
    }
    
    @Override
    public String toString() {
        return "Expression( " + getExpression() + " )";
    }
    
    protected CalculationParser getParser( String ex ) {
        log.trace( "Parsing " + ex );
        if ( !ex.endsWith( "\n" ) )
            ex += "\n";
        CalculationLexer lex = new CalculationLexer( new ANTLRStringStream( ex ) );
        return new CalculationParser( new CommonTokenStream( lex ) );
    }
    
}
