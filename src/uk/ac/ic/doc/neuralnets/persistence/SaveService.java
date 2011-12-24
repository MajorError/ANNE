package uk.ac.ic.doc.neuralnets.persistence;

import uk.ac.ic.doc.neuralnets.graph.Saveable;
import uk.ac.ic.doc.neuralnets.util.plugins.PriorityPlugin;

/**
 * Classes that implement this interface should be able to create a persistent 
 * representation of a given neural network in some format. They can be fully
 * parameterised through the use of a SaveSpecification.
 * 
 * @see SaveSpecification
 * @see SaveManager
 * 
 * @author Fred van den Driessche
 * 
 * @param <S> the SaveSpecification type required by the SaveService
 */
public abstract class SaveService<S extends SaveSpecification> extends PriorityPlugin {

	/**
	 * Exports the given neural network to persistent storage in a given format
	 * @param network - the network to save
	 * @param spec - the save service parameters
	 * @throws SaveException in the event of error during saving
	 */
	public abstract void save(Saveable network, S spec) throws SaveException;
    
    /**
     * Get the string form of the file type that this save service should seek
     * e.g. "*.xml"
     * @return The lexical form of the file extension
     */
    public abstract String getFileType();
	
}
