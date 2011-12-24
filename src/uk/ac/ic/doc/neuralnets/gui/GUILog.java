package uk.ac.ic.doc.neuralnets.gui;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * Creates the log box in the bottom bar
 * 
 * @author Fred van den Driessche
 * 
 */
public class GUILog {

	public GUILog(Composite container) {
		container.setLayout(new FillLayout());
		StyledText logbox = new StyledText(container, SWT.MULTI | SWT.V_SCROLL);
		logbox.setBounds(20, 20, 10, 10);
		logbox.setEditable(false);
		logbox.setBackground(ColorConstants.white);
		ScrollingTextAppender.setText(logbox);
	}

}
