package uk.ac.ic.doc.neuralnets.gui.listeners;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * SelectionListener shows the 'about' screen when the widget the listener is 
 * added to is selected.
 * 
 * @author Fred van den Driessche
 *
 */
public class AboutListener implements SelectionListener {
	static Logger log = Logger.getLogger(AboutListener.class);
	
	private Shell parent;
	private static AboutListener instance;

	private AboutListener(Shell rootShell) {
		this.parent = rootShell;
	}
	
	/**
	 * Get the listener instance
	 * @param parent - root shell
	 * @return listener instance
	 */
	public static AboutListener get(Shell parent){
		if(instance == null){
			instance = new AboutListener(parent);
		}
		return instance;
	}

	public void widgetDefaultSelected(SelectionEvent se) {
	}

	public void widgetSelected(SelectionEvent se) {
		log.debug("Show about");
		Shell dialog = new Shell(parent, SWT.DIALOG_TRIM
				| SWT.APPLICATION_MODAL);
		FillLayout fl = new FillLayout();
		fl.marginHeight = 25;
		fl.marginWidth = 25;
		dialog.setLayout(fl);
		Label aboutLabel = new Label(dialog, SWT.WRAP);
		aboutLabel.setText("ANNE created by "
				+ "\n\tPete 'Major Error' Coetzee,"
				+ "\n\tFred 'Colonel Panic' van den Driessche,"
				+ "\n\tSteve 'Rear-Admiral Runtime' Wray,"
				+ "\n\tChris 'General Exception' Matthews,"
				+ "\n\tIsmail 'Private Class' Gunsaya");
		aboutLabel.setSize(400, 400);
		aboutLabel.pack();
		dialog.pack();
		dialog.open();
	}

}
