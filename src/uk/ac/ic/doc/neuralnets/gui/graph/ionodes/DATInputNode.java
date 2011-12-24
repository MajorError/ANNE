
package uk.ac.ic.doc.neuralnets.gui.graph.ionodes;

import uk.ac.ic.doc.neuralnets.graph.neural.io.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Semaphore;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;



import uk.ac.ic.doc.neuralnets.matrix.PartitionableMatrix;

/**
 * Relies on an input file of the format:
 * inputRowWidth numRows outputRowWidth
 * target1Value1 target1Value2 ... target1Value[outputRowWidth]
 * row1Item1 row1Item2 ... row1Item[inputRowWidth]
 * .......
 * target[numRows]Value1 target[numRows]Value2 ... target[numRows]Value[outputRowWidth] 
 * row[numRows]Item1 row[numRows]Item2 ... row[numRows]Item[inputRowWidth]
 * @author Peter Coetzee
 */
public class DATInputNode extends InputNode implements Runnable {
    
    private static final Logger log = Logger.getLogger( DATInputNode.class  );
    private Semaphore working;
    
    public void configure() {
        working = new Semaphore( 0 );
        Display.getDefault().asyncExec( this );
        working.acquireUninterruptibly(); // block until ready
    }
    
    public void run() {
        Shell dialog = new Shell( Display.getDefault(), 
                SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL );
        FileDialog fd = new FileDialog( dialog, SWT.OPEN );
        fd.setText( "Open" );
        fd.setFilterPath( "/" );
        fd.setFilterExtensions( new String[]{ "*.dat" } );
        fd.setFilterNames( new String[]{ "Data File (*.dat)" } );
        String file = fd.open();
        dialog.dispose();
        if ( file == null ) {
            log.info( "Cancelled DATInputNode" );
            data = new PartitionableMatrix<Double>(0,0);
            targets = new PartitionableMatrix<Double>(0,0);
            working.release();
            return;
        }
        
        File f = new File( file );
        if ( !f.exists() ) {
            log.error( "No such file, " + f );
            return;
        }
        try {
            Scanner s = new Scanner( f );
            data = new PartitionableMatrix<Double>( s.nextInt(), s.nextInt() );
            targets = new PartitionableMatrix<Double>( s.nextInt(), data.getHeight() );
            for ( int y = 0; y < data.getHeight() && s.hasNext(); y++ ) {
                for ( int x = 0; x < targets.getWidth() && s.hasNext(); x++ )
                    targets.set( s.nextDouble(), x, y );
                for ( int x = 0; x < data.getWidth() && s.hasNext(); x++ )
                    data.set( s.nextDouble(), x, y );
            }
            log.info( "Loaded data from " + f );
            log.debug( "(Rows are shown in columns) " + data  + "Targets:" + targets );
            s.close();
        } catch ( FileNotFoundException ex ) {
            log.error( null, ex );
        } finally {
            working.release();
        }
    }
    
    public String getName() {
        return "DATInput";
    }
    
    public void destroy() {
    	// no-op
    }
    
    public void recreate() {
    	// no-op
    }

}
