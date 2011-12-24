package uk.ac.ic.doc.neuralnets.graph;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import uk.ac.ic.doc.neuralnets.events.EventManager;

/**
 * 
 * @author Peter Coetzee
 */
public class Graph implements Serializable, Identifiable {

	private static final long serialVersionUID = 4355711146490144156L;

	protected Set<Node<?>> nodes = new HashSet<Node<?>>();
	protected Set<Edge<?, ?>> edges = new HashSet<Edge<?, ?>>();
	private int id;

	public Graph() {
		getFreshID();
	}

	/**
	 * Sets the id of the object to a new fresh id.
	 */
	public void getFreshID() {
		id = EventManager.get().getUniqueID();
	}

	/**
	 * Sets the id of the object to parameter.
	 * 
	 * @param int New id.
	 */
	public void setID(int id) {
		this.id = id;
	}

	/**
	 * Adds input node to the graph as long as input node is not itself, returns
	 * itself.
	 * 
	 * @param n
	 *            Node to add.
	 * @return Itself with the node added or not added.
	 */
	public Graph addNode(Node<?> n) {
		if (n != this)
			nodes.add(n);
		return this;
	}

	/**
	 * Adds a collection of nodes to the graph, only if that collection doesn't
	 * contain itself.
	 * 
	 * @param ns
	 *            Collection of nodes to add.
	 * @return Itself with the nodes added or not added.
	 */
	public Graph addAllNodes(Collection<Node<?>> ns) {
		// Only add the nodes if it does not contain itself.
		if (!ns.contains(this))
			nodes.addAll(ns);
		return this;
	}

	/**
	 * Adds an edge to the graph and adds its start and end nodes to the graph.
	 * 
	 * @param e
	 *            Edge to add.
	 * @return Itself
	 */
	public Graph addEdge(Edge<?, ?> e) {
		edges.add(e);
		addNode(e.getStart());
		addNode(e.getEnd());
		return this;
	}

	/**
	 * Merges one graph with its self, as all the edges and nodes.
	 * 
	 * @param o
	 *            Graph to merge with.
	 * @return Itself
	 */
	public Graph merge(Graph o) {
		nodes.addAll(o.getNodes());
		edges.addAll(o.getEdges());
		return this;
	}

	/**
	 * Conducts a command on each node within the graph.
	 * 
	 * @param c
	 *            Command to execute.
	 * @return Itself.
	 */
	public Graph forEachNode(Command<Node<?>> c) {
		Iterator<Node<?>> it = nodes.iterator();
		while (it.hasNext())
			c.exec(it.next());
		return this;
	}

	/**
	 * Conducts a command on each edge within the graph.
	 * 
	 * @param c
	 *            Command to execute.
	 * @return Itself.
	 */
	public Graph forEachEdge(Command<Edge<?, ?>> c) {
		Iterator<Edge<?, ?>> it = edges.iterator();
		while (it.hasNext())
			c.exec(it.next());
		return this;
	}

	/**
	 * Gets the nodes from within.
	 * 
	 * @return The nodes.
	 */
	public Collection<Node<?>> getNodes() {
		return nodes;
	}

	/**
	 * Gets the edges from within.
	 * 
	 * @return The edges.
	 */
	public Collection<Edge<?, ?>> getEdges() {
		return edges;
	}

	/**
	 * Gets the id of the object.
	 * 
	 * @return The id.
	 */
	public int getID() {
		return id;
	}

	public String toString() {
		return type() + " " + id;
		/*
		 * final StringBuilder out = new StringBuilder(); out.append( type() +
		 * " " + id + " with Nodes: {\n" ); forEachNode( new Command<Node<?>>()
		 * { public void exec( Node<?> input ) { out.append( input.toString() +
		 * ", " ); } } ); out.append( "}\nEdges for " + id + ": {\n" );
		 * forEachEdge( new Command<Edge<?,?>>() { public void exec( Edge<?,?>
		 * input ) { out.append( input.toString() + ", " ); } } ); out.append(
		 * "}\n" ); return out.toString();
		 */
	}

	/**
	 * Returns the object type.
	 * @return Object type.
	 */
	protected String type() {
		return "Graph";
	}

	public interface Command<T> {

		public void exec(T input);

	}

}
