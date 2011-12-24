package uk.ac.ic.doc.neuralnets.graph.neural;

import java.io.Serializable;

import uk.ac.ic.doc.neuralnets.graph.Node;

/**
 * Default EdgeSpecification
 * 
 * @author Peter Coetzee
 */
public class EdgeSpecification<From extends Node<?>, To extends Node<?>>
		implements Serializable {

	private static final long serialVersionUID = 7456633093239192207L;

	/**
	 * Get the start of the edge.
	 * 
	 * @return The start.
	 */
	public From getStart() {
		return null;
	}

	/**
	 * Get the end of the edge.
	 * 
	 * @return The end.
	 */
	public To getEnd() {
		return null;
	}

	/**
	 * Returns a random weight.
	 * 
	 * @return Random weight: 0 < w < 1
	 */
	public double getWeight() {
		return Math.random();
	}

}
