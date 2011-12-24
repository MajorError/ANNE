package uk.ac.ic.doc.neuralnets.expressions.ast;

import java.util.Set;
import org.apache.log4j.Logger;
import uk.ac.ic.doc.neuralnets.expressions.ExpressionException;

/**
 * Simple Component to perform no operation at all. Must have a sub-component
 * under it in order to be evaluated.
 * @author Peter Coetzee
 */
public class NoOpComponent extends Component {
    
    protected Component sub;
    protected static final Logger log = Logger.getLogger( NoOpComponent.class );
    
    public NoOpComponent( Component sub ) {
        try {
            this.sub = sub.getVariables().size() > 0 ? 
                sub : new Literal( sub.evaluate() );
        } catch ( ExpressionException ex ) {
            log.warn( "Unexpected error parsing non-variable expression!", ex );
            this.sub = sub;
        }
    }

    public Double evaluate() throws ExpressionException {
        return sub.evaluate();
    }

    public String getExpression() {
        return sub.getExpression();
    }

    public Set<Component> getVariables() {
        return sub.getVariables();
    }

}
