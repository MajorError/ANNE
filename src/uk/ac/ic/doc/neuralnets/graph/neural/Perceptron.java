package uk.ac.ic.doc.neuralnets.graph.neural;

import org.apache.log4j.Logger;
import uk.ac.ic.doc.neuralnets.expressions.ExpressionException;
import uk.ac.ic.doc.neuralnets.graph.Node;

/**
 *
 * @author Peter Coetzee
 */
public class Perceptron extends Neurone {
    
	private static final long serialVersionUID = -5747936004256998726L;
	private static final Logger log = Logger.getLogger( Perceptron.class );
    
    @Override
    public double getCharge() {
        if ( charge == 0 ) // pull charge forward from previous perceptron
            for ( Synapse s : in )
                charge += s.getStart().getCharge() * s.getWeight();
        charge -= trigger;
        try {
            return getSquashFunction().evaluateThis( this );
        } catch ( ExpressionException ex ) {
            log.error( "Couldn't evaluate squash function!", ex );
        }
        return -1;
    }
    
    @Override
    public Node<Synapse> tick() {
        charge = 0; // reset to force it to re-calculate charge
        return this;
    }

    @Override
    public String toString() {
        return "Perceptron " + id;
    }

}
