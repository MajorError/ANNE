package uk.ac.ic.doc.neuralnets.tests.persistence;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import uk.ac.ic.doc.neuralnets.graph.neural.manipulation.GraphFactory;
import uk.ac.ic.doc.neuralnets.graph.neural.NeuralNetwork;
import uk.ac.ic.doc.neuralnets.persistence.FileSpecification;
import uk.ac.ic.doc.neuralnets.persistence.LoadException;
import uk.ac.ic.doc.neuralnets.persistence.LoadSerializationService;
import uk.ac.ic.doc.neuralnets.persistence.SaveException;
import uk.ac.ic.doc.neuralnets.persistence.SaveSerializationService;

public class NetworkSerializationTest {
	static Logger log = Logger.getLogger(SaveSerializationTest.class);

	public static void main(String[] args) {
		DOMConfigurator.configure("conf/log-conf.xml");
		
//		String expr = "no  spaces please".replaceAll("\\s", "");
//		log.trace(expr);
		
		NeuralNetwork sn = GraphFactory.get().makeNetwork(5, 1);
		FileSpecification spec = new FileSpecification("test", "Serialization");
		
		SaveSerializationService saver = new SaveSerializationService();
		LoadSerializationService loader= new LoadSerializationService();
		
		try {
			
			saver.save(sn, spec);
			
			NeuralNetwork ln = (NeuralNetwork) loader.load(spec);
			
			log.trace(ln.getNodes().size());
			
		} catch (SaveException e) {
			e.printStackTrace();
		} catch (LoadException e) {
			e.printStackTrace();
		}
		
	}
	
}
