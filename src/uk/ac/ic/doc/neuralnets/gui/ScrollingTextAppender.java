package uk.ac.ic.doc.neuralnets.gui;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

/**
 * 
 *
 * @author Peter Coetzee
 */
public class ScrollingTextAppender extends AppenderSkeleton {
    
    private static StyledText logbox = null;

    private Color   info = ColorConstants.gray,
                    warn = ColorConstants.black,
                    error = ColorConstants.red;
    
    public static void setText( StyledText t ) {
        logbox = t;
    }

    protected void append( final LoggingEvent e ) {
        if ( logbox != null )
            Display.getDefault().syncExec( new Runnable() {
                public void run() {
                    String s = layout.format( e );
                    StyleRange sr = new StyleRange();
                    sr.start = logbox.getText().length();
                    sr.length = s.length();
                    sr.foreground = info;
                    if ( e.getLevel().equals( Level.WARN ) ) {
                        sr.foreground = warn;
                        sr.fontStyle = SWT.ITALIC;
                    } else if ( e.getLevel().equals(  Level.ERROR ) ) {
                        sr.foreground = error;
                        sr.fontStyle = SWT.BOLD;
                    }
                    logbox.append( s );
                    logbox.setStyleRange( sr );
                }
            } );
    }

    public void close() {
        if ( logbox != null )
            Display.getDefault().asyncExec( new Runnable() {
                public void run() {
                    logbox.dispose();
                }
            } );
    }

    public boolean requiresLayout() {
        return true;
    }

}
