
package uk.ac.ic.doc.neuralnets.graph;

import java.io.Serializable;
import java.util.Collection;

/**
 *
 * @author Peter Coetzee
 */
public interface Node <E extends Edge<?, ?>> extends Serializable, Identifiable {
    
    public void setPos( int x, int y, int z );
    
    public int getX();
    
    public int getY();
    
    public int getZ();
    
    public Collection<E> getOutgoing();
    
    public Collection<E> getIncoming();
    
    public Node<E> connect( E e );
    
    public Node<E> setMetadata( String key, String item );
    
    public String getMetadata( String key );
    
    /**
     * States that this node has advanced one "tick" in time
     */
    public Node<E> tick();

}
