package uk.ac.ic.doc.neuralnets.gui.listeners;

import org.apache.log4j.Logger;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Shell;

/**
 * SelectionListener that exits the program when selected.
 * 
 * @author Fred van den Driessche
 *
 */
public class ExitListener implements SelectionListener {

	static Logger log = Logger.getLogger(ExitListener.class);
	
	private Shell parent;
	private static ExitListener instance; 

	private ExitListener(Shell rootShell) {
		this.parent = rootShell;
	}
	
	public static ExitListener get(Shell parent){
		if(instance == null){
			instance = new ExitListener(parent);
		}
		return instance;
	}

	public void widgetDefaultSelected(SelectionEvent se) {
	}

	public void widgetSelected(SelectionEvent se) {
		log.debug("Exiting");
		if (ContinueQuestion.ask(parent))
			System.exit(0); // Should probably start an exit routine.
	}

}
