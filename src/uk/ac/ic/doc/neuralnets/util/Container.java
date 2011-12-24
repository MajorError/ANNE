package uk.ac.ic.doc.neuralnets.util;

/**
 * Simple container for another object, for use when a final object is required
 * but cannot be furnished yet
 * 
 * @author Peter Coetzee
 */
public class Container<T> {

	private T contents;

	/**
	 * Create an empty container
	 */
	public Container() {
		// no-op; contents == null
	}

	/**
	 * Create a container with contents of type T.
	 * 
	 * @param contents
	 */
	public Container(T contents) {
		set(contents);
	}

	/**
	 * Set the content of the container.
	 * 
	 * @param t
	 *            - the object to store in the container
	 */
	public void set(T t) {
		contents = t;
	}

	/**
	 * Get the content of the container.
	 * 
	 * @return the container contents
	 */
	public T get() {
		return contents;
	}

}
