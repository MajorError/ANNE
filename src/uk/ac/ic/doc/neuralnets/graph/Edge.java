
package uk.ac.ic.doc.neuralnets.graph;

import java.io.Serializable;

/**
 *
 * @author Peter Coetzee
 */
public interface Edge<From extends Node<?>, To extends Node<?>> extends Serializable, Identifiable {
    
    public From getStart();
    
    public Edge<From,To> setStart( From start );
    
    public To getEnd();
    
    public Edge<From,To> setTo( To end );

}
