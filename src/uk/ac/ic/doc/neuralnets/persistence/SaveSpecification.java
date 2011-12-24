package uk.ac.ic.doc.neuralnets.persistence;

/**
 * SaveSpecification provide an abstract way of parameterising a SaveService in
 * order to save a network. To save a network a SaveSpecification is created
 * which names the SaveService to use as the save process. The specification is
 * passed to the SaveManager which retrieves the requested SaveService and
 * passes the specification on to it.
 * 
 * @see SaveService
 * @see SaveManager
 * 
 * @author Fred van den Driessche
 * 
 */
public interface SaveSpecification {

	/**
	 * The SaveService used by this specification.
	 * 
	 * @return the save service plugin name.
	 */
	public String getServiceName();

}
