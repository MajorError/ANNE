package uk.ac.ic.doc.neuralnets.expressions.ast;

import java.util.Set;
import uk.ac.ic.doc.neuralnets.expressions.ExpressionException;

/**
 * The abstract super-type of all components of the abstract syntax tree.
 * @author Peter Coetzee
 */
public abstract class Component {
    
    /**
     * Calculate the value of this expression sub-tree in its current bindings
     * (if applicable)
     * @return A Double value of the output of evaluating this tree
     * @throws uk.ac.ic.doc.neuralnets.expressions.ExpressionException
     */
    public abstract Double evaluate() throws ExpressionException;
    
    /**
     * Retrieve the original expression, re-formatted for user friendly output
     * @return A String representation of this expression tree; must be
     * re-parsable by the ASTExpressionFactory.
     */
    public abstract String getExpression();
    
    /**
     * Answer a set of the variable objects in this tree; this may include
     * any instances of the Variable class, or any operations that return a
     * different value for each evaluation, e.g. random operators, counters etc
     * @see uk.ac.ic.doc.neuralnets.expressions.ast.Variable
     * @return A Set of the variable components
     */
    public abstract Set<Component> getVariables();
    
    /**
     * A meethod to parenthesise the given child expression in the context of
     * the current operation; applies mathematical order of operations rules.
     * @param c The child component to parenthesise
     * @return A String representation of the child, with or without
     * parentheses, as deemed necessary.
     */
    public String bracket( Component c ) {
        if ( c instanceof BinaryOperator ) {
            BinaryOperator child = (BinaryOperator)c;
            if ( this instanceof BinaryOperator )
                if ( order( child.getOperation() ) <= 
                        order( ((BinaryOperator)this).getOperation() ) )
                    return child.getExpression();
            return "(" + c.getExpression() + ")";
        }
        return c.getExpression();
    }
    
    /**
     * Decide the internal ordering of the supplied operation; higher numbers
     * represent a lower importance. Defaults to Integer.MAX_VALUE if the
     * operator is not recognised.
     * @param op The operator to decide precedence of
     * @return An integer value; lower values for greater precedence
     */
    public int order( String op ) {
        switch( op.charAt( 0 ) ) {
            case '+' : return 5;
            case '-' : return 5;
            case '*' : return 3;
            case '/' : return 3;
            case '%' : return 3;
            case '^' : return 1;
            default : return Integer.MAX_VALUE;
        }
    }

}
