package uk.ac.ic.doc.neuralnets.events;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 *
 * @author Peter Coetzee
 */
public class MatlabMatrixStatistician extends NumericalStatistician {
    
    public void flush() {
        if ( values.size() == 0 ) {
            log.info( "No values to write from MatlabMatrix Statistician" );
            return;
        }
            
        try {
            File f = saveTo == null ? 
                File.createTempFile( "NetworkValues", ".m" ) : 
                new File( saveTo );

            log.info( "Saving to " + f.getAbsolutePath() );
            Writer w = new FileWriter( f );
            if ( values.size() > 0 )
                w.write( "values = zeros( " + values.size() + ", " 
                        + values.get( 0 ).size() + " );\n" );
            for ( List<Integer> i : values )
                w.write( "values=[values; " + i + "];\n" );
            w.write( "plot(values(:,1),values(:,2),'.');\n" );
            w.write( "pause;\n" );
            w.close();

            log.info( "Save complete - wrote out " + values.size() + " rows." );
        } catch ( IOException ex ) {
            log.error( ex );
        }
    }

    public String getName() {
        return "MatlabMatrix";
    }

}
