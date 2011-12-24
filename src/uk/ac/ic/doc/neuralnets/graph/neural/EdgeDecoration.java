package uk.ac.ic.doc.neuralnets.graph.neural;

import java.io.Serializable;

import uk.ac.ic.doc.neuralnets.util.plugins.Plugin;

public abstract class EdgeDecoration<T> implements Plugin, Serializable {

	private static final long serialVersionUID = 1L;

	public EdgeDecoration() {
		
	}
	
	public abstract T getFigure();
	
	public abstract String getName();

}
