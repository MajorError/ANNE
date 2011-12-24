package uk.ac.ic.doc.neuralnets.util;

import java.io.Serializable;

/**
 * General purpose Transformer from one data-type to another
 * 
 * @author Peter Coetzee
 */
public interface Transformer<A, B> extends Serializable {

	/**
	 * Transform input object
	 * 
	 * @param input
	 *            - the object to transform
	 * @return the transformed object
	 */
	public B transform(A input);

}
