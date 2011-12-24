package uk.ac.ic.doc.neuralnets.persistence;

import uk.ac.ic.doc.neuralnets.graph.Saveable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import org.apache.log4j.Logger;

/**
 * Loads a neural network through standard Java serialisation.
 * @author Fred van den Driessche
 *
 */
public class LoadSerializationService extends LoadService<FileSpecification> {

	static Logger log = Logger.getLogger(LoadSerializationService.class);
	
	public String getName() {
		return "Serialisation";
	}
	
	public Saveable load(FileSpecification spec) throws LoadException {
		try {
			File in = new File(spec.getSavePath());
			log.info("Reading from: '" + in.getCanonicalPath() + "' using service: " + getName());
			
			FileInputStream fis = new FileInputStream(in);
			ObjectInputStream ois = new ObjectInputStream(fis);
			
			Saveable network = (Saveable) ois.readObject();
			
			log.trace("Finished reading from file: " + spec.getSavePath());
			PersistenceAssistance.newInstance().fixIDs( network );
            
			return network;
		} catch (FileNotFoundException fnfe) {
			log.error("File not found at: " + spec.getSavePath());
			throw new LoadException(fnfe);
		} catch (IOException ioe) {
			log.error("Error reading file at: " + spec.getSavePath(), ioe);
			throw new LoadException(ioe);
		} catch (ClassNotFoundException cnfe) {
			//AFAIK, this could be thrown deserialising a network when there
			//are custom plugin neurones which the system doesn't know about,
			//for instance.
			log.error("Class not found deserialising network", cnfe);
			throw new LoadException(cnfe);
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
