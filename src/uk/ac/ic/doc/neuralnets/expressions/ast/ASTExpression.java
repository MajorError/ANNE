
package uk.ac.ic.doc.neuralnets.expressions.ast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.apache.log4j.Logger;
import uk.ac.ic.doc.neuralnets.expressions.BindVariable;
import uk.ac.ic.doc.neuralnets.expressions.ExpressionException;

/**
 * An expression object with support for dynamically bound variables, parsing
 * its contents into an abstract syntax tree.
 * @author Peter Coetzee
 */
public class ASTExpression {
    
    private Component tree;
    private static final Logger log = Logger.getLogger( ASTExpression.class );
    private static final Map<Method,BindVariable> varCache = new HashMap<Method,BindVariable>();
    private Map<String,Method> bindings = new HashMap<String,Method>();
    private Map<String,Variable> variables = new HashMap<String,Variable>();
    private Object target;
    
    /**
     * Create an Expression to encode the given value
     * @param value The value returned by this Expression
     */
    public ASTExpression( Double value ) {
        tree = new Literal( value.toString() ); // can't not parse!
    }
    
    /**
     * Create an Expression for the given string 
     * @param expr The expression to represent
     */
    public ASTExpression( String expr ) throws ExpressionException {
        tree = parse( expr );
    }
    
    /**
     * Evaluate the expression after refreshing its current bindings
     * @return The value this expression evaluates to
     * @throws uk.ac.ic.doc.neuralnets.expressions.ExpressionException
     */
    public Double evaluate() throws ExpressionException {
        return evaluateThis( target );
    }
    
    /**
     * Evaluate the expression after refreshing its current bindings from the
     * supplied object. Will not seek new annotations.
     * @param o The object to bind on to
     * @return The value this expression evaluates to
     * @throws uk.ac.ic.doc.neuralnets.expressions.ExpressionException
     */
    public Double evaluateThis( Object o ) throws ExpressionException {
        // Re-bind variables
        for ( Entry<String, Method> e : bindings.entrySet() ) {
            if ( e.getKey().startsWith( o.getClass().getCanonicalName() + "." ) )
                bind( e.getKey().substring( o.getClass().getCanonicalName().length() + 1 ), 
                        e.getValue(), o );
        }
        //log.trace( this + " = " + tree.evaluate() );
        return tree.evaluate();
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
        Variable v = variables.get( var );
        if ( v != null )
            v.bind( val );
    }
    
    /**
     * Bind variables according to BindVariable annotations present in this
     * object, and all of its super-classes
     * @param o The object to bind variables from
     */
    public void bind( Object o ) {
        target = o;
        for ( Method m : o.getClass().getMethods() ) {
            BindVariable bv = getVariable( m );
            if ( bv == null )
                continue;
            if ( bv.rebind() )
                bindings.put( o.getClass().getCanonicalName() + "." + bv.value(), m );
            else
                bind( bv.value(), m, target );
        }
    }
    
    private BindVariable getVariable( Method m ) {
        if ( varCache.containsKey( m ) )
            return varCache.get( m );
        BindVariable out = m.getAnnotation( BindVariable.class );
        varCache.put( m, out );
        return out;
    }
    
    protected void bind( String var, Method m, Object o ) {
        try {
            //log.trace(  "Binding " + var + " using " + m.getName() + " on " + o );
            Object d =  m.invoke( o );
            if ( !(d instanceof Double) ) // Try to parse it
                d = Double.valueOf( d.toString() );
            bind( var, (Double)d );
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
        return tree.getExpression();
    }
    
    @Override
    public String toString() {
        return getExpression();
    }
    
    protected Component parse( String ex ) throws ExpressionException {
        try {
            if ( !ex.endsWith( "\n" ) ) ex += "\n";
            ExpressionASTLexer lex = new ExpressionASTLexer( new ANTLRStringStream( ex ) );
            ExpressionASTParser p = new ExpressionASTParser( new CommonTokenStream( lex ) );
            Component t = p.getTree();
            variables = p.getVariables();
            bindSpecialVars();
            log.trace( "Parsed " + ex.substring( 0, ex.length() - 1) + " to " + t.getExpression() );
            return t;
        } catch ( Exception e ) {
            throw new ExpressionException( e );
        }
    }

    /**
     * Bind all possible variants of 'e' and 'pi' in the expression.
     */
    private void bindSpecialVars() {
        if ( variables.containsKey( "e" ) )
            variables.get( "e" ).bind( Math.E );
        if ( variables.containsKey( "E" ) )
            variables.get( "E" ).bind( Math.E );
        
        if ( variables.containsKey( "pi" ) )
            variables.get( "pi" ).bind( Math.PI );
        if ( variables.containsKey( "Pi" ) )
            variables.get( "Pi" ).bind( Math.PI );
        if ( variables.containsKey( "PI" ) )
            variables.get( "PI" ).bind( Math.PI );
        if ( variables.containsKey( "pI" ) )
            variables.get( "pI" ).bind( Math.PI );
    }
    
}
