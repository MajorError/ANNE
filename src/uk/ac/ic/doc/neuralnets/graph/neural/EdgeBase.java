
package uk.ac.ic.doc.neuralnets.graph.neural;

import uk.ac.ic.doc.neuralnets.graph.*;
import uk.ac.ic.doc.neuralnets.events.EventManager;

/**
 *
 * @author Peter Coetzee
 */
public abstract class EdgeBase<From extends Node<?>,To extends Node<?>> implements Edge<From,To> {

    private static final long serialVersionUID = -5002732960999392264L;
    
    protected From start;
    protected To end;
    
    private int id;
    
    public EdgeBase( From start, To end ) {
        this.start = start;
        this.end = end;
        
        getFreshID();
    }
    
    public int getID() {
    	return id;
    }
    
    public void setID( int id ) {
        this.id = id;
    }
    
    public void getFreshID() {
        id = EventManager.get().getUniqueID();
    }

    public From getStart() {
        return start;
    }

    public To getEnd() {
        return end;
    }

    public Edge<From,To> setStart( From start ) {
        this.start = start;
        return this;
    }

    public Edge<From,To> setTo( To end ) {
        this.end = end;
        return this;
    }
    
    public String toString() {
        return "Edge (" + (start == null ? "NONE" : start.getClass().getSimpleName() + start.getID()) +
                " --> " + (end == null ? "NONE" : end.getClass().getSimpleName() + end.getID()) + ") ";
    }

}
