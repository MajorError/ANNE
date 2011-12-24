package uk.ac.ic.doc.neuralnets.util.plugins;

/**
 * PriorityPlugin extends the plugin interface allowing an ordering to be 
 * applied. The ordering can be achieved in two ways: by implementing the
 * <code>getPriority</code> to return the plugin's priority, or by overriding
 * the <code>compareTo</code> method if more detailed comparison is required.
 * @author Fred
 */
public abstract class PriorityPlugin implements Comparable<PriorityPlugin>, Plugin {
	
	/**
	 * The plugin's priority.
	 * @return the priority
	 */
	public abstract int getPriority();
	
	public int compareTo( PriorityPlugin o ) {
		if ( getPriority() == o.getPriority() )
			return getName().compareTo( o.getName() );
		return getPriority() < o.getPriority() ? -1 : 1;
	}

}
