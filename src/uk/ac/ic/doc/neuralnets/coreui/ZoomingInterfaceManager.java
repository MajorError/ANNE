package uk.ac.ic.doc.neuralnets.coreui;

import java.util.Stack;
import uk.ac.ic.doc.neuralnets.graph.neural.NeuralNetwork;

/**
 *
 * @author Peter Coetzee
 */
public abstract class ZoomingInterfaceManager<GraphRepresentation,ItemRepresentation> 
        extends InterfaceManager<GraphRepresentation,ItemRepresentation> {

    /**
     * Returns a stack containing the IDs of each network layer that has
     * currently been zoomed into. This can be used to trace the current zoom
     * path from the root of the neural network.
     * 
     * @return a stack of IDs of each network layer that is currently zoomed
     *         into
     */
    public abstract Stack<Integer> getZoomIDs();

    /**
     * Returns a stack containing each network layer that has currently been
     * zoomed into, starting with the root network.
     * 
     * @return a stack containing each network layer that has currently been
     *         zoomed into.
     */
    public abstract Stack<NeuralNetwork> getZoomLevels();

    /**
     * Zooms into the selected network layer. Clears the current view, and
     * instead shows the contents of the selected network layer.
     * 
     * @param n
     *            the network to zoom into.
     */
    public abstract void zoomIn( NeuralNetwork n );

    /**
     * Zooms out one layer. Clears the current view, and instead shows the
     * contents of the current layer's parent. If the current view is the root
     * network, then nothing happens as it is not possible to zoom out further.
     * 
     */
    public abstract void zoomOut();	
    
	/**
	 * Checks whether or not it is possible to zoom in. It is only possible to
	 * zoom in if exactly one internal network layer is selected.
	 * 
	 * @return whether or not it is possible to zoom in
	 */
    public abstract boolean canZoomIn();

	/**
	 * Checks whether or not it is possible to zoom out. It is always possible
	 * to zoom out unless the current view is the root network.
	 * 
	 * @return whether or not it is possible to zoom out
	 */
	public abstract boolean canZoomOut();

}
