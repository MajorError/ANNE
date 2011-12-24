package uk.ac.ic.doc.neuralnets.tests.persistence;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import uk.ac.ic.doc.neuralnets.persistence.FileSpecification;
import uk.ac.ic.doc.neuralnets.persistence.LoadException;
import uk.ac.ic.doc.neuralnets.persistence.LoadSerializationService;
import uk.ac.ic.doc.neuralnets.persistence.SaveException;
import uk.ac.ic.doc.neuralnets.persistence.SaveSerializationService;

public class SaveSerializationTest implements Serializable{
	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger(SaveSerializationTest.class);

	/**
	 * @param args
	 */
	public static void main(String[] args){
		DOMConfigurator.configure("conf/log-conf.xml");
		
		int input = 1234;
		
		TestString o = new TestString(1234);
	
		FileSpecification spec = new FileSpecification("test", "Serialization");
		
		SaveSerializationService saver = new SaveSerializationService();
		LoadSerializationService loader = new LoadSerializationService();
		
		try {
			saver.save(o, spec);
			
			TestString i = (TestString) loader.load(spec);
			
			log.trace(i + " " + (input == i.getPayload()));
			
		} catch (SaveException e) {
			e.printStackTrace();
		} catch (LoadException e) {
			e.printStackTrace();
		}
		
		
	}

}
