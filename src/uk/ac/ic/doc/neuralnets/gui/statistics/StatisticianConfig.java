package uk.ac.ic.doc.neuralnets.gui.statistics;

import org.eclipse.swt.widgets.Shell;
import uk.ac.ic.doc.neuralnets.events.Event;
import uk.ac.ic.doc.neuralnets.events.EventHandler;
import uk.ac.ic.doc.neuralnets.graph.neural.NodeFired;
import uk.ac.ic.doc.neuralnets.util.plugins.PriorityPlugin;

/**
 * Basic Statistician Configuration interface. Statisticians are EventHandlers 
 * designed to harvest data from events during the running of a neural network.
 * StatisticianConfigs can be used to configure/disable Statisticians.
 * 
 * @author Peter Coetzee
 */
public abstract class StatisticianConfig extends PriorityPlugin {
    
	/**
	 * Perform an operations required to configure a new statistician.
	 * @param parent - shell access, for user interaction
	 * @return the configured event handler
	 */
    public abstract EventHandler configure( Shell parent );
    
    /**
     * Disable a statistician
     * @param h the event handler to disable
     */
    public abstract void disable( EventHandler h );
    
    /**
     * Defines which events this statistician listens for.
     * @return
     */
    @SuppressWarnings( "unchecked" )
    public Class<? extends Event>[] getTargetEvents() {
        return new Class[]{ NodeFired.class };
    }

}
