
package uk.ac.ic.doc.neuralnets.graph.neural.io;

import uk.ac.ic.doc.neuralnets.expressions.ExpressionException;
import uk.ac.ic.doc.neuralnets.graph.neural.Neurone;

/**
 * Purely a class to "mark" a neurone as being for I/O purposes.
 * @author Peter Coetzee
 */
public class IONeurone extends Neurone {
    
    private boolean concrete = true; 
    
    @Override
    public double getCharge() {
        try {
            return getSquashFunction() == null ? charge : getSquashFunction().evaluateThis( this );
        } catch ( ExpressionException ex ) { /* swallow */ }
        return 0;
    }

    @Override
    public String toString() {
        return "IONeurone " + id;
    }
}
