package uk.ac.ic.doc.neuralnets.tests.persistence;

import org.apache.log4j.Logger;

import uk.ac.ic.doc.neuralnets.events.EventManager;
import uk.ac.ic.doc.neuralnets.persistence.FileSpecification;
import uk.ac.ic.doc.neuralnets.persistence.LoadException;
import uk.ac.ic.doc.neuralnets.persistence.LoadManager;
import uk.ac.ic.doc.neuralnets.persistence.SaveException;
import uk.ac.ic.doc.neuralnets.persistence.SaveManager;
import uk.ac.ic.doc.neuralnets.graph.Saveable;
import uk.ac.ic.doc.neuralnets.util.configuration.ConfigurationManager;

public class PersistencePluginTest {

	static Logger log = Logger.getLogger(PersistencePluginTest.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ConfigurationManager.configure();

		try {
			int input = 1234;
			TestString t = new TestString(input);
			log.trace(t);
			
			FileSpecification spec = new FileSpecification("test", "Serialization");
			
			SaveManager.get().save(t, spec);
			
			Saveable ln = LoadManager.get().load(spec);
			
			TestString l = (TestString) ln;
			
			
			log.trace(l);
			
			log.trace(input == l.getPayload() ? 
							  "Serialisation - Deserialization successful"
							: "Serialisation - Deserialization not successful");
	
			
			
			log.info("Done");

		} catch (SaveException e) {
			e.printStackTrace();
		} catch (LoadException e) {
			e.printStackTrace();
		}
		
		EventManager.get().flushAll();
		System.exit(0);
	}

}
