package uk.ac.ic.doc.neuralnets.coreui;

import org.apache.log4j.Logger;

import uk.ac.ic.doc.neuralnets.commands.CommandControl;
import uk.ac.ic.doc.neuralnets.events.EventManager;
import uk.ac.ic.doc.neuralnets.events.GraphUpdateEvent;
import uk.ac.ic.doc.neuralnets.graph.Edge;
import uk.ac.ic.doc.neuralnets.graph.Node;
import uk.ac.ic.doc.neuralnets.graph.neural.NeuralNetwork;
import uk.ac.ic.doc.neuralnets.graph.neural.Neurone;
import uk.ac.ic.doc.neuralnets.graph.neural.NodeSpecification;
import uk.ac.ic.doc.neuralnets.graph.neural.manipulation.InteractionUtils;
import uk.ac.ic.doc.neuralnets.graph.neural.manipulation.NodeFactory;
import uk.ac.ic.doc.neuralnets.persistence.FileSpecification;

/**
 *
 * @author Peter Coetzee
 */
public abstract class InterfaceManager<GraphRepresentation,ItemRepresentation> {

    private static final Logger log = Logger.getLogger( InterfaceManager.class );
    protected NeuralNetwork network;
    protected InteractionUtils utils;
    protected CommandControl commandControl;
    protected FileSpecification saveLocation;

    /**
     * Loads the given neural network into the GUIManager, from the given
     * location.
     * 
     * @param network
     *            the network to be loaded into the GUIManager
     * @param location
     *            the location to load the network from
     */
    public void setNetwork( NeuralNetwork network, FileSpecification location ) {
        commandControl.reset();

        this.network = network;
        if ( utils == null )
            utils = new InteractionUtils( network );
        else
            utils.setNetwork( network );

        saveLocation = location;

        reset();
    }

    /**
     * Gets the location to save the network to, or null if no such location
     * exists.
     * 
     * @return the network's save location, or null if none exists
     */
    public FileSpecification getSaveLocation() {
        return saveLocation;
    }

    /**
     * Sets the network's save location.
     * 
     * @param saveLoc
     */
    public void setSaveLocation( FileSpecification saveLoc ) {
        this.saveLocation = saveLoc;
    }

    /**
     * Gets the root of the layered neural network stored in the GUIManager.
     * 
     * @return the root of the main neural network
     */
    public NeuralNetwork getRootNetwork() {
        return network;
    }

    /**
     * Adds the given neural network to the current view, and redraws the screen
     * as necessary.
     * 
     * @param n
     *            the neural network to add to the current section of the neural
     *            network
     */
    public void addNetwork( NeuralNetwork n ) {
        addNode( n );
    }

    /**
     * Adds the given neurone to the current view, and redraws the screen
     * as necessary.
     * 
     * @param n
     *            the neurone to add to the current section of the neural
     *            network
     */
    public void addNeurone( Neurone n ){
    	addNode( n );
    }
    
    /**
     * Creates a node from the give specification, adds to the current view, and
     * redraws the screen as necessary.
     * 
     * @param spec
     *            the specification of the node to add to the current section of
     *            the neural network
     */
    public void addNode( NodeSpecification<Node<?>> spec){
    	addNode(NodeFactory.get().create(spec));
    }
    
    /**
     * Adds the given node to the current view, and redraws the screen
     * as necessary.
     * 
     * @param n
     *            the node to add to the current section of the neural
     *            network
     */
    public void addNode( Node<?> n ){
    	getCurrentNetwork().addNode( n );
        redrawCurrentView();
        EventManager.get().fire( new GraphUpdateEvent() );	
    }
    
    /**
     * Removes the given neural network from the current view, and redraws the
     * screen as necessary.
     * 
     * @param n
     *            the neural network to remove from the current section of the neural
     *            network
     */
    public void removeNetwork( NeuralNetwork n ) {
        getCurrentNetwork().getNodes().remove( n );
        redrawCurrentView();
        EventManager.get().fire( new GraphUpdateEvent() );
    }

    /**
     * Adds the given edge to the current view, and redraws the screen as
     * necessary.
     *
     * @param e
     */
    public void addConnection( Edge<?, ?> e ) {
        getCurrentNetwork().addEdge( e );
    }

    /**
     * Gets the command control used by the GUIManager. This object handles the
     * undo and redo stacks as commands are executed and undone.
     * 
     * @return the CommandControl object used by the GUIManager
     */
    public CommandControl getCommandControl() {
        return commandControl;
    }

    /**
     * Returns the GUIManager's interaction utilities.
     * 
     * @return the InteractionUtils object used by the GUIManager
     */
    public InteractionUtils getUtils() {
        return utils;
    }

    /**
     * Draws the current view of the graph. Imports the current network layer
     * from the internal model and applies the current layout.
     */
    public abstract void redrawCurrentView();

    /**
     * Returns the Graph representation used by this UI Manager.
     * 
     * @return the Graph that the Manager draws onto
     */
    public abstract GraphRepresentation getGraph();

    /**
     * Pushes down the locations of all Nodes to the model. Allows positions
     * to be persisted to storage and reloaded.
     */
    public abstract void persistLocations();

    /**
     * Updates the tooltips or other UI hints of all graph elements 
     * in the current view.
     */
    public abstract void updateInterfaceHints();
    
    /**
    * Finds the GUINode in the GUI corresponding to the given Neurone and
    * returns it. Returns null if the given Neurone is not loaded in the GUI.
    *
    * @param n
    *            the Neurone to look up in the GUI
    * @return the GUINode in the GUI corresponding to the given Neurone
    */
    public abstract ItemRepresentation getNode(Neurone n);
    
    /**
     * Removes the given GraphItem from the view.
     *
     * @param i
     *            the graphitem to be removed from the view
     */
    public abstract void remove(ItemRepresentation i);

    /**
     * Reset the current manager, e.g. when a new network is loaded
     */
    protected abstract void reset();

	/**
	 * Returns the neural network layer currently being viewed in the
	 * GUIManager.
	 * 
	 * @return the current neural network layer
	 */
    public abstract NeuralNetwork getCurrentNetwork();
    
    
}
