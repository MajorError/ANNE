package uk.ac.ic.doc.neuralnets.persistence;

import uk.ac.ic.doc.neuralnets.graph.Saveable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.apache.log4j.Logger;

/**
 * Saves a neural network through standard Java serialisation.
 * @author Fred van den Driessche
 *
 */
public class SaveSerializationService extends SaveService<FileSpecification> {
	
	static Logger log = Logger.getLogger(SaveSerializationService.class);
    
    private PersistenceAssistance pa;

	public String getName() {
		return "Serialisation";
	}

	public void save(Saveable save, FileSpecification spec) throws SaveException {
		try {
			File out = new File(spec.getSavePath());
			log.info("Writing to '" + out.getCanonicalPath() + "' using service: " + getName());
			
			FileOutputStream fos = new FileOutputStream(out);
			
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			log.trace("Serialising network");
			oos.writeObject(save);
			log.trace("Serialisation complete");
			oos.close();
			fos.close();
			
			log.info("Finished writing to file: "+spec.getSavePath());
		} catch (FileNotFoundException fnfe) {
			log.error("File not found whilst saving network");
			throw new SaveException("File not found", fnfe);
		} catch (IOException ioe) {
			log.error("I/O Exception whilst saving network:", ioe);
			throw new SaveException("I/O Exception", ioe);
		}
	}
    
    public String getFileType() {
        return "*.net";
    }

    @Override
    public int getPriority() {
        return 0;
    }
}