package uk.ac.ic.doc.neuralnets.gui.listeners;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphItem;
import uk.ac.ic.doc.neuralnets.coreui.ZoomingInterfaceManager;
import uk.ac.ic.doc.neuralnets.events.Event;
import uk.ac.ic.doc.neuralnets.events.EventHandler;
import uk.ac.ic.doc.neuralnets.events.EventManager;
import uk.ac.ic.doc.neuralnets.persistence.FileSpecification;
import uk.ac.ic.doc.neuralnets.persistence.PersistenceProgressEvent;
import uk.ac.ic.doc.neuralnets.persistence.SaveException;
import uk.ac.ic.doc.neuralnets.persistence.SaveManager;

/**
 * SelectionListner that checks the graph manager save location or prompts the 
 * user for the location, then invokes the savemanager.
 * 
 * @author Fred van den Driessche
 *
 */
public class SaveListener extends PersistenceListener {
	static Logger log = Logger.getLogger(SaveListener.class);
	
	String plugin;
	ZoomingInterfaceManager<Graph,GraphItem> gm;
	FileSpecification spec;

	public SaveListener(Shell shell, String plugin, ZoomingInterfaceManager<Graph,GraphItem> gm) {
        super( shell );
		this.plugin = plugin;
		this.gm = gm;
	}

	public void widgetDefaultSelected(SelectionEvent se) {

	}

	public void widgetSelected(SelectionEvent se) {
		log.debug("Save network");
		
		spec = getSaveLocation();
		
		if(spec == null){
			log.info("Save cancelled");
			return;
		}
        
        createProgressView( "Please wait, Saving..." );

        new Thread() {
            public void run() {
                try {
                    SaveManager.get().save(gm.getRootNetwork(), spec);

                    log.info("Save Successful!");
                } catch (SaveException ex) {
                    log.error("Unable to save to " + spec.getSavePath() + "!",
                            ex);
                }
            }
        }.start();
	}
	
	protected FileSpecification getSaveLocation(){
		return gm.getSaveLocation() == null ? promptSaveLocation() : gm.getSaveLocation();
	}
	
	protected FileSpecification promptSaveLocation(){
        return promptForLocation( "Save", SWT.SAVE );
	}
}
