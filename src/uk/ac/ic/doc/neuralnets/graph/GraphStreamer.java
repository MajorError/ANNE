package uk.ac.ic.doc.neuralnets.graph;

import java.util.Iterator;
import uk.ac.ic.doc.neuralnets.util.Transformer;

/**
 * 
 * @author Peter Coetzee
 */
public class GraphStreamer<E, N> {

	private final Transformer<Edge<?, ?>, E> edgeMaker;
	private final Transformer<Node<?>, N> nodeMaker;
	private final Graph g;

	public GraphStreamer(Graph g, Transformer<Edge<?, ?>, E> edgeMaker,
			Transformer<Node<?>, N> nodeMaker) {
		this.g = g;
		this.edgeMaker = edgeMaker;
		this.nodeMaker = nodeMaker;
	}

	/**
	 * Returns an iterator for the edges that are contained in the GraphStreamer
	 * 
	 * @return Iterator of edges.
	 */
	public Iterator<E> getEdgeIterator() {
		final Iterator<Edge<?, ?>> it = g.getEdges().iterator();
		return new Iterator<E>() {

			public boolean hasNext() {
				return it.hasNext();
			}

			public E next() {
				return edgeMaker.transform(it.next());
			}

			public void remove() {
				it.remove();
			}

		};
	}

	/**
	 * Returns an iterator for the nodes that are contained in the GraphStreamer
	 * 
	 * @return Iterator of nodes.
	 */
	public Iterator<N> getNodeIterator() {
		final Iterator<Node<?>> it = g.getNodes().iterator();
		return new Iterator<N>() {

			public boolean hasNext() {
				return it.hasNext();
			}

			public N next() {
				return nodeMaker.transform(it.next());
			}

			public void remove() {
				it.remove();
			}

		};
	}

}
