
package uk.ac.ic.doc.neuralnets.gui.graph.ionodes;

import uk.ac.ic.doc.neuralnets.graph.neural.io.*;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 *
 * @author Peter Coetzee
 */
public class ValueListingOutputNode extends OutputNode implements Runnable {
    
    private Shell shell;
    private Display display;
    private List<Label> labels;
    private List<Double> values;
    private int nodes;
    private long next = 0;
    private boolean destroyed = false;
    
    @Override
    protected void setNodes( int n ) {
        if ( shell != null )
            shell.dispose();
        labels = new ArrayList<Label>( n );
        values = new ArrayList<Double>( n );
        display = Display.getDefault();
        nodes = n;
        display.syncExec( this );
    }
    
    public void run() {
        if ( shell == null && !destroyed ) {
        	labels.clear();
        	values.clear();
            shell = new Shell( display );
            shell.setLayout( new FillLayout() );
            Composite c = new Composite( shell, SWT.NONE );
            GridLayout l = new GridLayout();
            l.numColumns = 2;
            c.setLayout( l );
            for ( int i = 0; i < nodes; i++ ) {
                new Label( c, SWT.NONE ).setText( "Output " + (i+1) + ": " );
                labels.add( new Label( c, SWT.NONE ) );
                labels.get( i ).setText( "          " );
                values.add( 0.0d );
            }
            shell.pack();
            shell.open();
            shell.forceActive();
        } else if ( !destroyed ) {
            if ( next < System.currentTimeMillis() ) {
                if ( shell.isDisposed() )
                    return;
                for ( int i = 0; i < labels.size(); i++ )
                    labels.get( i ).setText( values.get( i ).toString() );
                shell.pack();
                next = System.currentTimeMillis() + 100;
            }
        }
        if ( !destroyed )
            display.asyncExec( this );
    }

    @Override
    protected void fire( int n, Double amt ) {
        values.set( n, amt );
    }

    public String getName() {
        return "ValueListing";
    }
    
    public void destroy() {
        destroyed = true;
        shell.dispose();
        shell = null;
    }
    
    public void recreate() {
    	destroyed = false;
        display.syncExec( this );
    	
    }

}
