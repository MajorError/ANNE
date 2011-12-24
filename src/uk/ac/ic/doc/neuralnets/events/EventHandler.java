package uk.ac.ic.doc.neuralnets.events;

import uk.ac.ic.doc.neuralnets.util.plugins.Plugin;

/**
 * Basic interface for EventHandlers
 * 
 * @author Peter Coetzee
 */
public interface EventHandler extends Plugin {
    
    /**
     * Fires an event at this handler
     * @param e The event which has occurred
     */
    public void handle( Event e );
    
    /**
     * Instructs this handler to flush its buffers of data (usually
     * indicating that execution has completed)
     */
    public void flush();
    
    /**
     * Answers whether or not this handler is valid for execution. If not,
     * when a new Neural Network run begins the Statistician may be re-created
     * by the StatisticsManager.
     * @return True iff this Statistician may process new input
     */
    public boolean isValid();

}
