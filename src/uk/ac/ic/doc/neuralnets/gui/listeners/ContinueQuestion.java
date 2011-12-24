package uk.ac.ic.doc.neuralnets.gui.listeners;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

/**
 * Prompts the user for to confirm continuing with an action
 * 
 * @author Fred van den Driessche
 *
 */
public class ContinueQuestion {

	/**
	 * Ask a continue question of the user.
	 * @param parent - root shell
	 * @param desc - question description
	 * @return true to continue, false otherwise
	 */
	public static boolean ask(Shell parent, String desc){
		MessageBox messageBox = new MessageBox(parent, SWT.ICON_QUESTION
				| SWT.YES | SWT.NO);
		messageBox.setMessage("Are you sure you want to continue?");
		messageBox.setText(desc);
		int response = messageBox.open();
		return response == SWT.YES;
	}
	
	/**
	 * Ask a question with the standard description: 
	 * "All unsaved changes will be lost!".
	 * @param parent - root shell
	 * @return true to continue, false otherwise
	 */
	public static boolean ask(Shell parent){
		return ContinueQuestion.ask(parent, "All unsaved changes will be lost!");
	}
	
}
