package uk.ac.ic.doc.neuralnets.expressions.ast;

import java.util.HashSet;
import java.util.Set;
import org.apache.log4j.Logger;
import uk.ac.ic.doc.neuralnets.expressions.ExpressionException;

/**
 * Encodes an operator with two parameters, assumes infix notation when
 * outputting this expression.
 * @author Peter Coetzee
 */
public abstract class BinaryOperator extends Component {
    
    protected Component l, r;
    protected String operation;
    protected Set<Component> variables = new HashSet<Component>();
    protected static final Logger log = Logger.getLogger( BinaryOperator.class );
    
    public BinaryOperator( Component l, Component r, String operation ) {
        this.operation = operation;
        try {
            if ( l.getVariables().size() == 0 )
                l = new Literal( l.evaluate() );
        } catch ( ExpressionException ex ) {
            log.warn( "Unexpected error parsing non-variable expression!", ex );
        }
        this.l = l;
        variables.addAll( l.getVariables() );
        
        try {
            if ( r.getVariables().size() == 0 )
                r = new Literal( r.evaluate() );
        } catch ( ExpressionException ex ) {
            log.warn( "Unexpected error parsing non-variable expression!", ex );
        }
        this.r = r;
        variables.addAll( r.getVariables() );
    }

    public abstract Double evaluate() throws ExpressionException;

    public String getExpression() {
        return bracket( l ) + " " + operation + " " + bracket( r );
    }
    
    /**
     * Answer the operation encoded by this BinaryOperator
     * @return The lexical form of the operation
     */
    public String getOperation() {
        return operation;
    }

    public Set<Component> getVariables() {
        return variables;
    }

}
