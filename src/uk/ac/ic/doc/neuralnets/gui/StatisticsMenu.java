package uk.ac.ic.doc.neuralnets.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import org.apache.log4j.Logger;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MenuItem;

import org.eclipse.swt.widgets.Shell;
import uk.ac.ic.doc.neuralnets.events.Event;
import uk.ac.ic.doc.neuralnets.events.EventHandler;
import uk.ac.ic.doc.neuralnets.events.EventManager;
import uk.ac.ic.doc.neuralnets.graph.neural.NeuralNetworkSimulationEvent;
import uk.ac.ic.doc.neuralnets.gui.GUIMenu;
import uk.ac.ic.doc.neuralnets.gui.statistics.StatisticianConfig;
import uk.ac.ic.doc.neuralnets.gui.ImageHandler;
import uk.ac.ic.doc.neuralnets.util.plugins.PluginLoadException;
import uk.ac.ic.doc.neuralnets.util.plugins.PluginManager;

public class StatisticsMenu extends MenuPlugin implements SelectionListener, EventHandler, Runnable {

    static Logger log = Logger.getLogger( StatisticsMenu.class );
    private MenuItem disableAll, flushAll;
    private Shell parent;
    private Map<String,StatisticianConfig> configurators = new HashMap<String,StatisticianConfig>();
    private Map<String,EventHandler> statisticians = new HashMap<String,EventHandler>();
    private Image add = ImageHandler.get().getIcon( "chart_line_add" ), 
            delete = ImageHandler.get().getIcon( "chart_line_delete" );
    private boolean enabled = true;

    @Override
    public void load( GUIMenu menu ) {
        parent = menu.getShell();

        menu.addSubMenu( "root", "&Statistics" );

        disableAll = menu.addMenuItem( "&Statistics", "Disable All" );
        disableAll.setImage( ImageHandler.get().getIcon( "delete" ) );
        disableAll.addSelectionListener( this );

        flushAll = menu.addMenuItem( "&Statistics", "Flush to Disk" );
        flushAll.setImage( ImageHandler.get().getIcon( "save_document" ) );
        flushAll.addSelectionListener( this );

        menu.addMenuSeparator( "&Statistics" );
        try {
            SortedSet<StatisticianConfig> plugins = new TreeSet<StatisticianConfig>();
            // sort plugins first
            for ( String p : PluginManager.get().getPluginsOftype( 
                    StatisticianConfig.class ) )
                plugins.add( PluginManager.get().getPlugin( p, 
                    StatisticianConfig.class ) );
            // now add to menu
            for ( StatisticianConfig p : plugins )
                add( menu, p );
        } catch ( PluginLoadException ex ) {
            log.error( "Couldn't enumerate statisticians!", ex );
        }
        EventManager.get().registerSynchro( NeuralNetworkSimulationEvent.class, this );
    }
    
    private void add( GUIMenu menu, StatisticianConfig p ) throws PluginLoadException {
        configurators.put( p.getName(), p );
        MenuItem curr = menu.addMenuItem( "&Statistics", p.getName() );
        curr.setImage( add );
        curr.addSelectionListener( this );
    }

    @Override
    public int getPriority() {
        return 3;
    }

    public String getName() {
        return "StatisticsMenu";
    }

    public void widgetSelected( SelectionEvent e ) {
        MenuItem m = ((MenuItem)e.widget);
        log.debug( "Clicked " + m );
        if ( e.widget.equals( disableAll ) ) {
            log.debug( "Disabling all statisticians" );
            for ( EventHandler h : statisticians.values() )
                disable( h, false );
            statisticians.clear();
            for ( MenuItem i : m.getParent().getItems() )
                if ( i.equals(  flushAll ) || i.equals(  disableAll ) )
                    continue;
                else 
                    i.setImage( add );
        } else if ( e.widget.equals(  flushAll ) ) {
            log.debug( "Flushing All" );
            for ( EventHandler h : statisticians.values() ) {
                StatisticianConfig c = configurators.get( h.getName() );
                for ( Class<? extends Event> cls : c.getTargetEvents() )
                    EventManager.get().flush( cls );
                h.flush();
            }
        } else {
            log.debug( "Selected " + m.getText() );
            if ( m.getImage().equals( add ) ) {
                enable( m.getText() );
                m.setImage( delete );
            } else {
                disable( m.getText() );
                m.setImage( add );
            }
        }
    }
    
    private void enable( String stat ) {
        StatisticianConfig c = configurators.get( stat );
        EventHandler h = c.configure( parent );
        for ( Class<? extends Event> cls : c.getTargetEvents() )
            EventManager.get().registerAsync( cls, h );
        statisticians.put( stat, h );
        log.debug( "Loaded " + h.getName() );
    }
    
    private void disable( String stat ) {
        log.debug( "Disable \"" + stat + "\"" );
        disable( statisticians.get( stat ), true );
    }

    private void disable( EventHandler h, boolean remove ) {
        log.debug( "Disable " + h );
        StatisticianConfig c = configurators.get( h.getName() );
        for ( Class<? extends Event> cls : c.getTargetEvents() )
            EventManager.get().deregisterAsync( cls, h );
        c.disable( h );
        if ( remove )
            statisticians.remove( h.getName() );
    }

    public void widgetDefaultSelected( SelectionEvent e ) {
        widgetSelected( e );
    }

    public void handle( Event e ) {
        enabled = !((NeuralNetworkSimulationEvent)e).started();
        Display.getDefault().syncExec( this );
    }

    public void run() {
        for ( MenuItem m : disableAll.getParent().getItems() )
            m.setEnabled( enabled );
    }

    public void flush() {
        // no-op
    }

    public boolean isValid() {
        return true;
    }
}
