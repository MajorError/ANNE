package uk.ac.ic.doc.neuralnets.gui.listeners;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.Semaphore;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import uk.ac.ic.doc.neuralnets.events.Event;
import uk.ac.ic.doc.neuralnets.events.EventHandler;
import uk.ac.ic.doc.neuralnets.events.EventManager;
import uk.ac.ic.doc.neuralnets.persistence.FileSpecification;
import uk.ac.ic.doc.neuralnets.persistence.LoadService;
import uk.ac.ic.doc.neuralnets.persistence.PersistenceProgressEvent;
import uk.ac.ic.doc.neuralnets.persistence.SaveService;
import uk.ac.ic.doc.neuralnets.util.plugins.Plugin;
import uk.ac.ic.doc.neuralnets.util.plugins.PluginLoadException;
import uk.ac.ic.doc.neuralnets.util.plugins.PluginManager;

/**
 * Abstract listener that can prompt for a user location, and show progress.
 *
 * @author Peter Coetzee
 */
public abstract class PersistenceListener implements SelectionListener,
                                                     EventHandler {

    protected Shell parent;
    private static final Logger log = Logger.getLogger( 
            PersistenceListener.class );
    private Image throbber; // TODO - Make it throb!!
    private Label progress;
    private ProgressBar progressBar;
    private Shell indicator;

    public PersistenceListener( Shell parent ) {
        this.parent = parent;
    }

    protected void createProgressView( String message ) {
        log.debug( "Creating progress view" );
        EventManager.get().registerSynchro( PersistenceProgressEvent.class, this );

        indicator = new Shell( parent, SWT.APPLICATION_MODAL );
        GridLayout l = new GridLayout( 1, false );
        l.marginBottom = l.marginTop = l.marginLeft = l.marginRight = 50;
        indicator.setLayout( l );

        Label msg = new Label( indicator, SWT.NONE );
        msg.setText( message );
        msg.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_CENTER ) );

        createThrobber();

        updateIndicator();

        indicator.setVisible( true );
        indicator.setActive();
    }

    private ImageLoader loader;
    private int imageNumber;
    
    protected void createThrobber() {
        loader = new ImageLoader();
        try {
            loader.load( new FileInputStream( new File( "res/throbber.gif" ) ) );
        } catch ( FileNotFoundException ex ) {
            log.error( "Could not load throbber image! Continuing anyway.", ex );
            return;
        }
        final Canvas canvas = new Canvas( indicator, SWT.NONE );
        canvas.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_CENTER ) );
        
        throbber = new Image( Display.getCurrent(), loader.data[0] );
        final GC gc = new GC( throbber );
        canvas.addPaintListener( new PaintListener() {

             public void paintControl( PaintEvent event ) {
                 event.gc.drawImage( throbber, 0, 0 );
             }
        } );

        log.debug( "Created threader, spawning draw thread" );
        new Thread() {
            public void run() {
                while( throbber != null ) {
                    long endDelay = System.currentTimeMillis() 
                            + loader.data[imageNumber].delayTime * 10;
                    while ( endDelay > System.currentTimeMillis() );
                        // busy-wait
                    // Increase the variable holding the frame number
                    imageNumber = imageNumber == loader.data.length - 1 ? 
                       0 : imageNumber + 1;
                    // Draw the new data onto the image
                    final ImageData nf = loader.data[imageNumber];
                    Display.getDefault().asyncExec( new Runnable() {

                       public void run() {
                           if ( indicator == null )
                               return;
                           synchronized( indicator ) {
                                if ( throbber == null || throbber.isDisposed() )
                                    return;
                               Image img = new Image( Display.getCurrent(), nf );
                               gc.drawImage( img, nf.x, nf.y );
                               img.dispose();
                               canvas.redraw();
                           }
                       }
                   } );
                }
            }
        }.start();
        updateIndicator();
    }

    protected void createProgressIndicators() {
        progress = new Label( indicator, SWT.NONE );
        progress.setText( "0% " );
        progress.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_END ) );

        progressBar = new ProgressBar( indicator, SWT.NONE );
        progressBar.setMinimum( 0 );
        progressBar.setMaximum( 100 );
        progressBar.setSelection( 0 );
        updateIndicator();
    }

    protected void updateIndicator() {
        indicator.pack();
        Rectangle b = Display.getCurrent().getBounds();
        indicator.setLocation( b.x + b.width / 2 - indicator.getSize().x / 2,
                               b.y + b.height / 2 - indicator.getSize().y / 2 );
    }

    protected FileSpecification promptForLocation( String title, int type ) {
        FileDialog fd = new FileDialog( parent, type );
        fd.setText( title );
        fd.setFilterPath( System.getProperty( "user.dir" ) );
        List<String> extensions = new ArrayList<String>();
        List<String> descriptions = new ArrayList<String>();
        Map<String, String> services = new HashMap<String, String>();

        SortedSet<Plugin> plugins = new TreeSet<Plugin>();
        try {
            Class<? extends Plugin> c = type == SWT.OPEN ? LoadService.class : SaveService.class;
            for ( String p : PluginManager.get().getPluginsOftype( c ) )
                plugins.add( PluginManager.get().getPlugin( p, c ) );
            Method m = c.getMethod( "getFileType" );
            for ( Plugin p : plugins ) {
                String fileType = (String) m.invoke( p );
                extensions.add( fileType );
                String desc = p.getName() + " (" + fileType + ")";
                descriptions.add( desc );
                services.put( desc, p.getName() );
                services.put( fileType.toLowerCase(), p.getName() );
            }
        } catch ( IllegalArgumentException ex ) {
            log.error( "Invalid plugin; wrong arguments for file type!", ex );
        } catch ( InvocationTargetException ex ) {
            log.error( "Invalid plugin; unable to get file type!", ex );
        } catch ( IllegalAccessException ex ) {
            log.error( "Invalid plugin; couldn't get file type!", ex );
        } catch ( NoSuchMethodException ex ) {
            log.error( "Invalid plugin type; no file type!", ex );
        } catch ( SecurityException ex ) {
            log.error( "Not permitted to access file type!", ex );
        } catch ( PluginLoadException ex ) {
            log.error( "Cannot get list of persistence plugins", ex );
        }
        fd.setFilterExtensions( extensions.toArray( new String[]{} ) );
        fd.setFilterNames( descriptions.toArray( new String[]{} ) );
        String select = fd.open();
        String plugin = services.get( fd.getFilterNames()[fd.getFilterIndex()] );
        if ( select != null && System.getProperty( "os.name" ).toLowerCase().
                startsWith( "mac" ) && fd.getFilterIndex() == 0 ) {
            String[] parts = select.split( "\\." );
            String ext = "*." + parts[parts.length - 1].toLowerCase();
            if ( services.containsKey( ext ) )
                plugin = services.get( ext );
            log.debug( "Seeking plugin for extension " + ext + " rendered " + plugin );
        }
        return select == null ? null : new FileSpecification( select, plugin );
    }

    public void handle( Event e ) {
        final PersistenceProgressEvent p = (PersistenceProgressEvent) e;
        log.debug( "Persistence progress: " + p.getProgress() );
        Display.getDefault().syncExec( new Runnable() {

           public void run() {
               if ( indicator == null )
                   return;
               synchronized( indicator ) {
                   if ( p.getProgressPercentage() >= 100 ) {
                        if ( indicator != null ) {
                            indicator.dispose();
                            // Null out our old references for next execution
                            indicator = null;
                            progress = null;
                            progressBar = null;
                            throbber = null;
                        }
                        return;
                   }
                   if ( progress == null )
                       createProgressIndicators();
               }
               progress.setText( p.getProgressPercentage() + "%" );
               progressBar.setSelection( p.getProgressPercentage() );
               indicator.pack();
           }
       } );
    }

    public void flush() {
        // no-op
    }

    public boolean isValid() {
        return true;
    }

    public String getName() {
        return "PersistenceListener";
    }
}
