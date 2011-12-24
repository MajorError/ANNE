package uk.ac.ic.doc.neuralnets.gui.graph;

import uk.ac.ic.doc.neuralnets.graph.Node;

/**
 * Objects of this type contain a model Node.
 * @author Peter Coetzee
 */
public interface NodeContainer {
    
	/**
	 * Set the node contained in the container.
	 * @param n
	 */
    public void setNode( Node<?> n );
    
    /**
     * Get the node contained in the container.
     * @return the contained node
     */
    public Node<?> getNode();

}
