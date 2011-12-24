package uk.ac.ic.doc.neuralnets.expressions.ast;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Peter Coetzee
 */
public class Literal extends Component {
    
    protected Double value;
    protected Set<Component> variables = new HashSet<Component>();
    
    public Literal( String val ) {
        this( Double.parseDouble( val ) );
    }
    
    public Literal( Double d ) {
        value = d;
    }

    public Double evaluate() {
        return value;
    }

    public String getExpression() {
        return value.toString();
    }

    public Set<Component> getVariables() {
        return variables;
    }

}
