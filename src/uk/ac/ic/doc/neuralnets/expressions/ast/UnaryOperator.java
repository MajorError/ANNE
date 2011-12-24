package uk.ac.ic.doc.neuralnets.expressions.ast;

import java.util.HashSet;
import java.util.Set;
import org.apache.log4j.Logger;
import uk.ac.ic.doc.neuralnets.expressions.ExpressionException;

/**
 * Component that is evaluated with one operator only
 * @author Peter Coetzee
 */
public abstract class UnaryOperator extends Component {
    
    protected Component c;
    protected String operation;
    protected Set<Component> variables = new HashSet<Component>();
    protected static final Logger log = Logger.getLogger( UnaryOperator.class );
    
    public UnaryOperator( Component c, String operation ) {
        this.operation = operation;
        try {
            if ( c.getVariables().size() == 0 )
                c = new Literal( c.evaluate() );
        } catch ( ExpressionException ex ) {
            log.warn( "Unexpected error parsing non-variable expression!", ex );
        }
        this.c = c;
        variables.addAll( c.getVariables() );
    }

    public abstract Double evaluate() throws ExpressionException;

    public String getExpression() {
        return operation + (operation.equals( "-" ) ?
            c.getExpression() : "(" + c.getExpression() + ")");
    }

    public Set<Component> getVariables() {
        return variables;
    }

}
