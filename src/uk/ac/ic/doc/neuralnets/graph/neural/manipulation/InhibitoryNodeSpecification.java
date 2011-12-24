package uk.ac.ic.doc.neuralnets.graph.neural.manipulation;

import java.util.Random;

import org.apache.log4j.Logger;
import uk.ac.ic.doc.neuralnets.expressions.ExpressionException;
import uk.ac.ic.doc.neuralnets.expressions.ast.ASTExpressionFactory;
import uk.ac.ic.doc.neuralnets.graph.neural.EdgeDecoration;
import uk.ac.ic.doc.neuralnets.graph.neural.NodeSpecification;
import uk.ac.ic.doc.neuralnets.graph.neural.SpikingNeurone;
import uk.ac.ic.doc.neuralnets.util.plugins.PluginLoadException;
import uk.ac.ic.doc.neuralnets.util.plugins.PluginManager;

/**
 * Default NodeSpecification for Inhibitory Spiking neurones.
 * 
 * @see SpikingNeurone
 * @see NodeSpecification
 * 
 * @author Peter Coetzee
 */
public class InhibitoryNodeSpecification extends SpikingNodeSpecification {

	private static final Logger log = Logger
			.getLogger(InhibitoryNodeSpecification.class);

    
    /**
     * Creates a inhibitory spiking neurone specification with default 
     * parameters according to Izhikevich's model.
     * 
     * <dl>
     * 	<dt>Squash Function</dt><dd>-1</dd>
     *  <dt>Trigger</dt><dd>30</dd>
     *  <dt>Initial Charge</dt><dd>-65</dd>
     *  <dt>Recovery Scale</dt><dd>0.02 + 0.08 * RAND()</dd>
     *  <dt>Recovery Sensitivity</dt><dd>0.25 - 0.05 * RAND()</dd>
     *  <dt>Post Spike Reset</dt><dd>-65</dd>
     *  <dt>PSRRecovery</dt><dd>2</dd>
     *  <dt>Thalamic Input</dt><dd>2 * GRAND()</dd>
     *  <dt>Synaptic Delay</dt><dd>20 * RAND()</dd>
     * </dl>
     *  where RAND() is a  uniformly distributed random number between 0 and 1 
     *  and GRAND() is a Gaussian distributed random number.
     *  
     * @see Random
     */
	public InhibitoryNodeSpecification() {
		try {
			ASTExpressionFactory f = ASTExpressionFactory.get();
			set("Squash Function", f.getExpression(-1d));
			set("Recovery Scale", f.getExpression("0.02 + 0.08 * RAND()"));
			set("Recovery Sensitivity", f.getExpression("0.25 - 0.05 * RAND()"));
			set("Post Spike Reset", f.getExpression(-65d));
			set("PSRRecovery", f.getExpression(2d));
			set("Thalamic Input", f.getExpression("2 * GRAND()"));
			setEdgeDecoration(PluginManager.get().getPlugin( "Circle", EdgeDecoration.class ));
		} catch ( PluginLoadException ex ) {
            log.error("Unable to load Circle edge decoration!", ex);
        } catch (ExpressionException ex) {
			log.error("Unexpected expression parse error", ex);
		}
	}

}
