package uk.ac.ic.doc.neuralnets.graph.neural;

import uk.ac.ic.doc.neuralnets.graph.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import uk.ac.ic.doc.neuralnets.graph.neural.Persistable;

/**
 * Basic Node implementation; should suffice for most Node purposes
 * 
 * @author Peter Coetzee
 */
public abstract class NodeBase<E extends Edge<?, ?>> implements Node<E> {

	private static final long serialVersionUID = -5002732960999392261L;

	protected Set<E> in, out;
	private Map<String, String> metadata = new HashMap<String, String>();

	private int xpos = 0, ypos = 0, zpos = 0;

	protected NodeBase() {
		// Comparator<E> byid = new NodeComparator();
		in = new HashSet<E>();
		out = new HashSet<E>();
	}

	protected NodeBase(Set<E> in, Set<E> out) {
		this.in = in;
		this.out = out;
	}

	/**
	 * Sets the position of the node.
	 * 
	 * @param x
	 *            Position on x axis.
	 * @param y
	 *            Position on y axis.
	 * @param z
	 *            Position on z axis.
	 */
	public void setPos(int x, int y, int z) {
		xpos = x;
		ypos = y;
		zpos = z;
	}

	/**
	 * Sets the position of the node on the x axis.
	 * 
	 * @param x
	 *            Position on x axis.
	 */
	public void setX(int x) {
		xpos = x;
	}

	/**
	 * Sets the position of the node on the y axis.
	 * 
	 * @param y
	 *            Position on y axis.
	 */
	public void setY(int y) {
		ypos = y;
	}

	/**
	 * Sets the position of the node on the z axis.
	 * 
	 * @param z
	 *            Position on z axis.
	 */
	public void setZ(int z) {
		zpos = z;
	}

	/**
	 * Returns the position of the node on the x axis.
	 * 
	 * @return x axis position.
	 */
	@Persistable
	public int getX() {
		return xpos;
	}

	/**
	 * Returns the position of the node on the y axis.
	 * 
	 * @return y axis position.
	 */
	@Persistable
	public int getY() {
		return ypos;
	}

	/**
	 * Returns the position of the node on the z axis.
	 * 
	 * @return z axis position.
	 */
	@Persistable
	public int getZ() {
		return zpos;
	}

	/**
	 * Get outgoing edges.
	 */
	public Collection<E> getOutgoing() {
		return out;
	}

	/**
	 * Get incoming edges.
	 */
	public Collection<E> getIncoming() {
		return in;
	}

	/**
	 * Connect this node up with the input edge.
	 */
	public Node<E> connect(E e) {
		(e.getStart().equals(this) ? out : in).add(e);
		return this;
	}

	/**
	 * Set meta data for the object.
	 * 
	 * @param key
	 *            String key
	 * @param item
	 *            String item
	 */
	public Node<E> setMetadata(String key, String item) {
		metadata.put(key, item);
		return this;
	}

	/**
	 * Returns the meta data for the key input.
	 * 
	 * @param key
	 *            To look for.
	 * @return item Found.
	 */
	public String getMetadata(String key) {
		if (!metadata.containsKey(key))
			throw new IllegalArgumentException("No such metadata: " + key);
		return metadata.get(key);
	}

	public abstract Node<E> tick();

	@Override
	public abstract String toString();

	private class NodeComparator implements Comparator<E>, Serializable {

		public int compare(E o1, E o2) {
			return o1.getID() == o2.getID() ? 0 : (o1.getID() < o2.getID() ? -1
					: 1);
		}

	};

}
