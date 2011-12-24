package uk.ac.ic.doc.neuralnets.gui.statistics;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Shell;
import uk.ac.ic.doc.neuralnets.events.EventHandler;
import uk.ac.ic.doc.neuralnets.events.GraphUpdateEvent;
import uk.ac.ic.doc.neuralnets.gui.events.RealTimePlotStatistician;
import uk.ac.ic.doc.neuralnets.util.plugins.PluginLoadException;
import uk.ac.ic.doc.neuralnets.util.plugins.PluginManager;

/**
 * Plots neurones firings on to a chart as they occur.
 * 
 * @author Peter Coetzee
 */
public class RealTimePlot extends StatisticianConfig {
    
    private static final Logger log = Logger.getLogger( RealTimePlot.class );

    public EventHandler configure( Shell parent ) {
        try {
            return PluginManager.get().getPlugin( getName(), EventHandler.class );
        } catch ( PluginLoadException ex ) {
            log.error( "Couldn't load plot!", ex );
        }
        return null;
    }

    public void disable( EventHandler h ) {
        if ( !(h instanceof RealTimePlotStatistician) )
            return;
        h.handle( new GraphUpdateEvent() );
    }

    public String getName() {
        return "RealTimePlot";
    }

    public int getPriority() {
        return 1;
    }

}
