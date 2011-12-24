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
public class CSVStatistician extends NumericalStatistician {
    
    public void flush() {
        try {
            File f = saveTo == null ? 
                File.createTempFile( "NetworkValues", ".csv" ) : 
                new File( saveTo );

            log.info( "Saving to " + f.getAbsolutePath() );
            Writer w = new FileWriter( f );
            for ( List<Integer> i : values ) {
                for ( Integer v : i )
                    w.write( v + "," );
                w.write(  "\n" );
            }
            w.close();

            log.info( "Save complete - wrote out " + values.size() + " rows." );
        } catch ( IOException ex ) {
            log.error( ex );
        }
    }

    public String getName() {
        return "CSV";
    }

}
