
package uk.ac.ic.doc.neuralnets.graph.neural.manipulation;

import java.util.Random;

import uk.ac.ic.doc.neuralnets.graph.neural.NodeSpecification;
import org.apache.log4j.Logger;
import uk.ac.ic.doc.neuralnets.expressions.ExpressionException;
import uk.ac.ic.doc.neuralnets.expressions.ast.ASTExpressionFactory;
import uk.ac.ic.doc.neuralnets.graph.neural.SpikingNeurone;

/**
 * Default NodeSpecification for SpikingNeurones
 * 
 * @see SpikingNeurone
 * @see NodeSpecification
 * @see NodeFactory
 * 
 * @author Peter Coetzee
 */
public class SpikingNodeSpecification extends NodeSpecification<SpikingNeurone> {
    
    private static final Logger log = Logger.getLogger( SpikingNodeSpecification.class );
    
    /**
     * Creates a spiking neurone specification with default parameters according
     * to Izhikevich's model.
     * 
     * <dl>
     * 	<dt>Squash Function</dt><dd>0.5</dd>
     *  <dt>Trigger</dt><dd>30</dd>
     *  <dt>Initial Charge</dt><dd>-65</dd>
     *  <dt>Recovery Scale</dt><dd>0.02</dd>
     *  <dt>Recovery Sensitivity</dt><dd>0.2</dd>
     *  <dt>Post Spike Reset</dt><dd>-65 + 15 * RAND()<sup>2</sup></dd>
     *  <dt>PSRRecovery</dt><dd>8 - 6 * RAND()<sup>2</sup></dd>
     *  <dt>Thalamic Input</dt><dd>5 * GRAND()</dd>
     *  <dt>Synaptic Delay</dt><dd>20 * RAND()</dd>
     * </dl>
     *  where RAND() is a  uniformly distributed random number between 0 and 1 
     *  and GRAND() is a Gaussian distributed random number.
     *  
     * @see Random
     */
    public SpikingNodeSpecification() {
        super( SpikingNeurone.class );
        try {
        	ASTExpressionFactory f = ASTExpressionFactory.get();
            set( "Squash Function", f.getExpression( 0.5d ) );
            set( "Trigger", f.getExpression( 30d ) );
            set( "Initial Charge", f.getExpression( -65d ) );
            set( "Recovery Scale", f.getExpression( 0.02d ) );
            set( "Recovery Sensitivity", f.getExpression( 0.2d ) );
            set( "Post Spike Reset", f.getExpression( "-65 + 15 * RAND() ^ 2" ) );
            set( "PSRRecovery", f.getExpression( "8 - 6 * RAND() ^ 2" ) );
            set( "Thalamic Input", f.getExpression( "5 * GRAND()" ) );
            set( "Synaptic Delay", f.getExpression( "20 * RAND()" ) );
        } catch ( ExpressionException ex ) {
            log.error( "Unexpected expression parse error", ex );
        }
    }

}
