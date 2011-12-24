package uk.ac.ic.doc.neuralnets.persistence;

/**
 * Denotes there was an error whilst attempting to save a network.
 * @author Fred van den Driessche
 *
 */
public class SaveException extends Exception {

	private static final long serialVersionUID = 1L;

	public SaveException() {
		
	}

	public SaveException(String message) {
		super(message);
	}

	public SaveException(Throwable cause) {
		super(cause);
	}

	public SaveException(String message, Throwable cause) {
		super(message, cause);
	}

}
