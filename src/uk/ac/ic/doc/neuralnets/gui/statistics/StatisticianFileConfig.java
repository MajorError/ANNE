package uk.ac.ic.doc.neuralnets.gui.statistics;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import uk.ac.ic.doc.neuralnets.events.EventHandler;
import uk.ac.ic.doc.neuralnets.events.NumericalStatistician;
import uk.ac.ic.doc.neuralnets.util.plugins.PluginLoadException;
import uk.ac.ic.doc.neuralnets.util.plugins.PluginManager;

/**
 * Specialized Statistician that outputs to a file.
 * @author Peter Coetzee
 */
public abstract class StatisticianFileConfig extends StatisticianConfig {
    
    private static final Logger log = Logger.getLogger( StatisticianFileConfig.class );
    
    /**
     * Requests a save location from the user and configures the statistician
     */
    public EventHandler configure( Shell parent ) {
        FileDialog fd = new FileDialog( parent, SWT.SAVE );
		fd.setText( "Save Statistics To ..." );
		fd.setFilterPath( System.getProperty( "user.dir" ) );
        fd.setFilterExtensions( new String[]{ getExtension() } );
        fd.setFilterNames( new String[]{ getName() } );
        String file = fd.open();
        return file == null ? null : configure( file );
    }
    
    protected EventHandler configure( String filename ) {
        try {
            EventHandler h = PluginManager.get().getPlugin( 
                    getName(), EventHandler.class );
            if ( h instanceof NumericalStatistician )
                ((NumericalStatistician)h).saveAs( filename );
            return h;
        } catch ( PluginLoadException ex ) {
            log.error( "Couldn't load statistician " + getName(), ex );
            return null;
        }
    }

    public void disable( EventHandler h ) {
        h.flush();
    }
    
    /**
     * The file extension for the output format.
     * @return the file extension.
     */
    protected abstract String getExtension();

}
