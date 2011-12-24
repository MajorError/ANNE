package uk.ac.ic.doc.neuralnets.persistence;

/**
 * LoadSpecifications provide an abstract method for parameterising a
 * LoadService in order to load a neural network in to the program. To load a
 * network a LoadSpecification is created which names the LoadService to use as
 * the load process. The specification is passed to the LoadManager which
 * retrieves the requested LoadService and passes the specification on to it.
 * 
 * @see LoadService
 * @see LoadManager
 * 
 * @author Fred van den Driessche
 * 
 */
public interface LoadSpecification {

	/**
	 * The LoadService used by this specification.
	 * 
	 * @return the load service plugin name.
	 */
	public String getServiceName();

}
