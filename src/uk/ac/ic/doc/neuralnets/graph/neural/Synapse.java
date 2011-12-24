
package uk.ac.ic.doc.neuralnets.graph.neural;

import uk.ac.ic.doc.neuralnets.graph.neural.EdgeBase;
import uk.ac.ic.doc.neuralnets.graph.neural.Persistable;

/**
 *
 * @author Peter Coetzee
 */
public class Synapse extends EdgeBase<Neurone,Neurone> {

    private static final long serialVersionUID = -5002732960999392564L;
    
    @Persistable
    private double weight = 1;
    
    private int delay = 0;
    
    public Synapse() {
        this( null, null );
    }
    
    public Synapse( Neurone start, Neurone end ) {
        super( start, end );
    }
    
    public Synapse( double weight, Neurone start, Neurone end ) {
        super( start, end );
        this.weight = weight;
    }
    
    public Synapse fire( double amt ) {
        end.charge( amt * weight );
        return this;
    }
    
    public Synapse setDelay( int d ) {
        delay = d;
        return this;
    }
    
    public int getDelay() {
        return delay;
    }
    
    public Synapse setWeight( double weight ) {
        this.weight = weight;
        return this;
    }
    
    public double getWeight() {
        return weight;
    }
    
    public String toString() {
        return super.toString() + " with weight " + weight;
    }

}
