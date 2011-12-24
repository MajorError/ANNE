package uk.ac.ic.doc.neuralnets.expressions.ast;

import java.util.HashSet;
import java.util.Set;
import uk.ac.ic.doc.neuralnets.expressions.ExpressionException;

/**
 * Component to be evaluated with no operators
 * @author Peter Coetzee
 */
public abstract class NullaryOperator extends Component {
    
    protected String operation;
    protected Set<Component> variables = new HashSet<Component>();
    
    public NullaryOperator( String operation ) {
        this.operation = operation;
    }

    public abstract Double evaluate() throws ExpressionException;

    public String getExpression() {
        return operation + "()";
    }

    public Set<Component> getVariables() {
        return variables;
    }

}
