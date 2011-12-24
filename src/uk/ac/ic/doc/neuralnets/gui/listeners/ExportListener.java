package uk.ac.ic.doc.neuralnets.gui.listeners;

import org.eclipse.swt.widgets.Shell;

import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphItem;
import uk.ac.ic.doc.neuralnets.coreui.ZoomingInterfaceManager;
import uk.ac.ic.doc.neuralnets.persistence.FileSpecification;

/**
 * SaveListener that always prompts for a save location and uses SavePlugins
 * @author Fred van den Driessche
 *
 */
public class ExportListener extends SaveListener {

	/**
	 * Create a new export listener
	 * @param shell
	 * @param plugin
	 * @param gm
	 */
	public ExportListener(Shell shell, String plugin, ZoomingInterfaceManager<Graph,GraphItem> gm) {
		super(shell, plugin, gm);
	}
	
	protected FileSpecification getSaveLocation(){
		return super.promptSaveLocation();
	}

}
