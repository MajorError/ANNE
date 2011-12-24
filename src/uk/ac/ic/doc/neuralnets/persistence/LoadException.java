package uk.ac.ic.doc.neuralnets.persistence;

/**
 * Denotes an error whilst attempting to load a network.
 * @author Fred van den Driessche
 *
 */
public class LoadException extends Exception {

	private static final long serialVersionUID = 1L;

	public LoadException() {
		
	}

	public LoadException(String message) {
		super(message);
	}

	public LoadException(Throwable cause) {
		super(cause);
	}

	public LoadException(String message, Throwable cause) {
		super(message, cause);
	}

}
