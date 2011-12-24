package uk.ac.ic.doc.neuralnets.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.log4j.Logger;
import uk.ac.ic.doc.neuralnets.util.plugins.PluginLoadException;
import uk.ac.ic.doc.neuralnets.util.plugins.PluginManager;

/**
 *
 * @author Peter Coetzee
 */
public class EventManager {

    private static EventManager s = new EventManager();
    private Logger log = Logger.getLogger( EventManager.class  );

    public static EventManager get() {
        return s;
    }
    private Map<Class<? extends Event>, List<EventHandler>> asyncHandlers;
    private Map<Class<? extends Event>, List<EventHandler>> synchroHandlers;
    private BlockingQueue<Event> events;
    private boolean run = true;

    private EventManager() {
        // Not publically instantiable
        asyncHandlers = new HashMap<Class<? extends Event>, List<EventHandler>>();
        synchroHandlers = new HashMap<Class<? extends Event>, List<EventHandler>>();
        events = new LinkedBlockingQueue<Event>();
        spawnDispatcherThread();
        createStatisticianRefresher();
    }

    public void fire( Event e ) {
        if ( e instanceof SingletonEvent && events.contains( e ) )
            return;
        events.add( e );
        if ( e instanceof GraphUpdateEvent )
            log.trace( synchroHandlers.entrySet() );
        handle( e.getClass(), e, synchroHandlers );
    }

    public boolean flush( Class<? extends Event> e ) {
        boolean hasEvents = true;
        boolean needed = false;
        while( events.size() > 0 && hasEvents ) {
            for ( Event e2 : events ) {
                if ( e2 != null && e.isAssignableFrom( e2.getClass() ) ) {
                    hasEvents = true;
                    needed = true;
                    try {
                        Thread.sleep( 50 );
                    } catch ( InterruptedException ex ) { }
                    break;
                }
            }
        }
        return needed;
    }

    public void flushAll() {
        // let events finish processing
        while ( events.size() > 0 ) {
            try {
                Thread.sleep( 100 );
            } catch ( InterruptedException ex ) { }
        }
        run = false;
        synchronized( asyncHandlers ) {
            for ( List<EventHandler> stats : asyncHandlers.values() )
                for ( EventHandler s : stats )
                    s.flush();
        }
    }

    public void registerAsync( Class<? extends Event> c, EventHandler s ) {
        List<EventHandler> stats;
        synchronized( asyncHandlers ) {
            if ( asyncHandlers.containsKey( c ) ) {
                stats = asyncHandlers.get( c );
            } else {
                stats = new ArrayList<EventHandler>();
                asyncHandlers.put( c, stats );
            }
            stats.add( s );
        }
    }

    public void registerSynchro( Class<? extends Event> c, EventHandler s ) {
        List<EventHandler> stats;
        synchronized( synchroHandlers ) {
            if ( synchroHandlers.containsKey( c ) ) {
                stats = synchroHandlers.get( c );
            } else {
                stats = new ArrayList<EventHandler>();
                synchroHandlers.put( c, stats );
            }
            stats.add( s );
        }
    }
    
    public void deregisterAsync( final Class<? extends Event> c, final EventHandler s ) {
        // Let the event queue clear
        boolean work = true;
        while( work ) {
            work = false;
            for ( Event e : events ) {
                if ( c.isAssignableFrom( e.getClass() ) ) {
                    work = true;
                    break;
                }
            }
            if ( work ) { // Let the event queue dispatch before trying again
                try {
                    Thread.sleep( 1000 );
                } catch ( InterruptedException e ) {}
            }
        }
        // Now spawn a new thread (to prevent deadlock) to remove s
        new Thread() {
            @Override
            public void run() {
                synchronized( asyncHandlers ) {
                    if ( asyncHandlers.containsKey( c ) )
                        asyncHandlers.get( c ).remove( s );
                    log.debug( "Removed " + c + " :: " + s );
                }
            }
        }.start();
    }
    
    public void deregisterSynchro( final Class<? extends Event> c, final EventHandler s ) {
        synchronized( synchroHandlers ) {
            if ( synchroHandlers.containsKey( c ) )
                synchroHandlers.get( c ).remove( s );
        }
    }
    
    private int id = 0;

    public synchronized int getUniqueID() {
        return id++;
    }
            
    protected void handle( Class c, Event e, Map<Class<? extends Event>, List<EventHandler>> handlers ) {
        synchronized( handlers ) {
            if ( handlers.containsKey( c ) )
                for ( EventHandler s : handlers.get( c ) )
                    if ( s.isValid() )
                        s.handle( e );
        }
        c = c.getSuperclass();
        // Now try and propagate the event up the class hierarchy,
        // until the superclass is no longer an Event
        if ( c != null && Event.class.isAssignableFrom( c ) )
            handle( c, e, handlers );
    }

    private void spawnDispatcherThread() {
        new Thread( new Runnable() {

            public void run() {
                Thread.currentThread().setName( "EventManager Async Dispatcher" );
                while ( run ) {
                    try {
                        Event e = events.take();
                        handle( e.getClass(), e, asyncHandlers );
                    } catch ( InterruptedException ex ) {
                        // try again!
                    } catch ( Throwable t ) {
                        log.error( "Error running statisticians: ", t );
                    }
                }
            }
        } ).start();
    }

    private void createStatisticianRefresher() {
        registerAsync( RevalidateStatisticiansEvent.class, new EventHandler() {

            public void handle( Event ev ) {
                // Re-create statisticians as needed
                for ( Map.Entry<Class<? extends Event>, List<EventHandler>> e 
                        : asyncHandlers.entrySet() ) {
                    for ( EventHandler s : e.getValue() ) {
                        if ( !s.isValid() ) {
                            e.getValue().remove( s );
                            try {
                                s = PluginManager.get().getPlugin( 
                                        s.getName(), EventHandler.class );
                                log.debug( "Re-Creating statistician " 
                                        + s.getName() );
                                registerAsync( e.getKey(), s );
                            } catch ( PluginLoadException ex ) {
                                log.error( "Couldn't re-create Statistician " 
                                        + s.getName(), ex );
                            }
                        }
                    }
                }
            }

            public void flush() {
                // no-op
            }

            public boolean isValid() {
                return true;
            }

            public String getName() {
                return "StatisticianRefresher";
            }
            
        } );
    }
}
