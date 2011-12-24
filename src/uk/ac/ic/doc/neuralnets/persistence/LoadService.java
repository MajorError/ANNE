package uk.ac.ic.doc.neuralnets.persistence;

import uk.ac.ic.doc.neuralnets.graph.Saveable;
import uk.ac.ic.doc.neuralnets.util.plugins.PriorityPlugin;

/**
 * Classes that implement this interface should be able to create 
 * neural networks for use in the application from data in persistable storage.
 * They can be fully parameterised through the use of a LoadSpecification.
 * 
 * @see LoadSpecification
 * @see LoadManager
 * 
 * @author Fred van den Driessche
 *
 * @param <L> the LoadSpecification type required by the LoadService.
 */
public abstract class LoadService<L extends LoadSpecification> extends PriorityPlugin {

	/**
	 * Imports a neural network from persistent storage.
	 * 
	 * @param spec - the load service parameters
	 * @return the loaded network
	 * @throws LoadException in event of error during loading.
	 */
	public abstract Saveable load(L spec) throws LoadException;
    
    /**
     * Get the string form of the file type that this load service should seek
     * e.g. "*.xml"
     * @return The lexical form of the file extension
     */
    public abstract String getFileType();
	
}
