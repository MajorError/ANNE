package uk.ac.ic.doc.neuralnets.tests.persistence;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import uk.ac.ic.doc.neuralnets.graph.neural.manipulation.GraphFactory;
import uk.ac.ic.doc.neuralnets.graph.neural.NeuralNetwork;
import uk.ac.ic.doc.neuralnets.persistence.FileSpecification;
import uk.ac.ic.doc.neuralnets.persistence.xml.LoadXMLService;
import uk.ac.ic.doc.neuralnets.persistence.SaveException;
import uk.ac.ic.doc.neuralnets.persistence.xml.SaveXMLService;
import uk.ac.ic.doc.neuralnets.util.configuration.ConfigurationManager;

public class SaveXMLTest {
	static Logger log = Logger.getLogger("uk.ac.ic.doc.neuralnets.persistence");

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DOMConfigurator.configure("conf/log-conf.xml");
		ConfigurationManager.configure();

		try {
			int nodes = 3;
			double edgeProb = 0.5;
			log.trace("Creating network with " + nodes + " nodes, edgeProb " + edgeProb);
			NeuralNetwork sn1 = GraphFactory.get().makeNetwork(nodes, edgeProb);
			
			log.trace("Created network");
			
			FileSpecification spec = new FileSpecification("test.xml", "XMLService");
			
			SaveXMLService saver = new SaveXMLService();
			LoadXMLService loader = new LoadXMLService();
			
			log.info("Saving network...");
			saver.save(sn1, spec);
			log.info("...Network saved.");
			
			log.info("Loading network...");
			//NeuralNetwork ln = (NeuralNetwork) loader.load(spec);
			log.info("...Network loaded.");

		//} catch (LoadException e) {
		//	e.printStackTrace();
		} catch (SaveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}