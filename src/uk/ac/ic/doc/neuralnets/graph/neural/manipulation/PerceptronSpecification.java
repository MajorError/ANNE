package uk.ac.ic.doc.neuralnets.graph.neural.manipulation;

import uk.ac.ic.doc.neuralnets.graph.neural.NodeSpecification;
import org.apache.log4j.Logger;
import uk.ac.ic.doc.neuralnets.expressions.ExpressionException;
import uk.ac.ic.doc.neuralnets.expressions.ast.ASTExpression;
import uk.ac.ic.doc.neuralnets.expressions.ast.ASTExpressionFactory;
import uk.ac.ic.doc.neuralnets.graph.neural.Perceptron;

/**
 * Default NodeSpecification for Perceptrons.
 * 
 * @see NodeSpecification
 * @see NodeFactory
 *
 * @author Peter Coetzee
 */
public class PerceptronSpecification extends NodeSpecification<Perceptron> {
    
    private static final Logger log = Logger.getLogger( PerceptronSpecification.class  );
    
    /**
     * Creates a perceptron specifcation with default sigmoid parameters.
     * 
     * <dl>
     *  <dt>Squash Function</dt><dd> 1 / (1 + e <sup>-charge</sup>) </dd>
     *  <dt>Trigger</dt><dd>1</dd>
     * </dl>
     */
    public PerceptronSpecification() {
        super( Perceptron.class );
        try {
        	ASTExpressionFactory f = ASTExpressionFactory.get();
            //set( "Squash Function", new ASTExpression( "0.1 * charge" ) );
            // Squash is the logistic sigmoid function over charge
            set( "Squash Function", f.getExpression( "1 / (1 + e ^(-charge))" ) );
            //set( "Squash Function", new ASTExpression( "1" ) );
            set( "Trigger", f.getExpression( "1" ) );
        } catch ( ExpressionException ex ) {
            log.error( "", ex );
        }
    }

}
