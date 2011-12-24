package uk.ac.ic.doc.neuralnets.expressions.ast;

import java.util.HashSet;
import java.util.Set;
import uk.ac.ic.doc.neuralnets.expressions.ExpressionException;

/**
 * A named variable Component, capable of being bound to any Double value.
 * @author Peter Coetzee
 */
public class Variable extends Component {
    
    protected String var;
    protected Set<Component> variables = new HashSet<Component>();
    protected boolean bound = false;
    protected Double binding;
    
    public Variable( String name ) {
        var = name;
        variables.add( this );
    }

    public String getExpression() {
        return var;//bound ? var + "=" + binding : var;
    }
    
    public Double evaluate() throws ExpressionException {
        if ( !bound )
            throw new ExpressionException( "No binding for variable '" + var + "'" );
        return binding;
    }

    public Set<Component> getVariables() {
        return variables;
    }

    /**
     * Bind this variable to the given value
     * @param val The value to bind this Variable component to
     */
    public void bind( Double val ) {
        bound = true;
        binding = val;
    }

}
