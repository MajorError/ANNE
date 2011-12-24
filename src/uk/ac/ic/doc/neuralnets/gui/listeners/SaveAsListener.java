package uk.ac.ic.doc.neuralnets.gui.listeners;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphItem;
import uk.ac.ic.doc.neuralnets.coreui.ZoomingInterfaceManager;
import uk.ac.ic.doc.neuralnets.persistence.FileSpecification;

/**
 * SaveListener that always prompts for location
 * 
 * @author Fred van den Driessche
 *
 */
public class SaveAsListener extends SaveListener {

	public SaveAsListener(Shell shell, String plugin, ZoomingInterfaceManager<Graph,GraphItem> gm) {
		super(shell, plugin, gm);
	}
	
    @Override
	public void widgetSelected(SelectionEvent se) {
		super.widgetSelected(se);
		gm.setSaveLocation(spec);
	}
	
    @Override
	protected FileSpecification getSaveLocation(){
		return promptSaveLocation();
	}

}
