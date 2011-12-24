package uk.ac.ic.doc.neuralnets.gui.listeners;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphItem;
import uk.ac.ic.doc.neuralnets.coreui.ZoomingInterfaceManager;
import uk.ac.ic.doc.neuralnets.graph.neural.NeuralNetwork;
import uk.ac.ic.doc.neuralnets.persistence.FileSpecification;
import uk.ac.ic.doc.neuralnets.persistence.LoadException;
import uk.ac.ic.doc.neuralnets.persistence.LoadManager;
import uk.ac.ic.doc.neuralnets.graph.Saveable;

/**
 * Prompts the user for a location to open a network from when selected
 * 
 * @author Fred van den Driessche
 *
 */
public class OpenListener extends PersistenceListener {
	static Logger log = Logger.getLogger(OpenListener.class);

	private String plugin;
	private ZoomingInterfaceManager<Graph,GraphItem> gm;
    private boolean replace;

    /**
     * Create the new Listener instance
     * @param rootShell - root shell
     * @param loadService - load service to use
     * @param gm - graph manager
     * @param replace - replace or import into current network
     */
	public OpenListener(Shell rootShell, String loadService, ZoomingInterfaceManager<Graph,GraphItem> gm, boolean replace) {
        super( rootShell );
		this.plugin = loadService;
		this.gm = gm;
        this.replace = replace;
	}

	public void widgetDefaultSelected(SelectionEvent se) {
	}

	public void widgetSelected(SelectionEvent se) {
		log.debug("Opening network");
		if(replace && !ContinueQuestion.ask(parent)){
			log.info("Open network cancelled");
			return;
		}
		// Invoke open dialog
        FileSpecification fs = promptOpenLocation();
		if (fs != null) {
			log.info("Load from: " + fs.getSavePath() + " using " + fs.getServiceName());
            
            createProgressView( "Please wait, Loading..." );
			load( fs );
		} else {
			log.info("Open network cancelled");
		}
	}
    
    protected void load( final FileSpecification fs ) {
        Display.getDefault().syncExec( new Runnable() {
            public void run() {
                try {
                    Saveable s = LoadManager.get().load(fs);
                    if (replace)
                        gm.setNetwork((NeuralNetwork) s, fs);
                    else
                        gm.addNetwork((NeuralNetwork) s);
                    log.info("Opened network");
                } catch (LoadException e) {
                    log.error("Unable to load from " + fs + "!", e);
                }
            }
        } );
    }
    
    protected FileSpecification promptOpenLocation() {
        return promptForLocation( "Open", SWT.OPEN );
    }

}
