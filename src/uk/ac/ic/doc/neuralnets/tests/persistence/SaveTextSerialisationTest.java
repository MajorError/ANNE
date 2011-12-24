package uk.ac.ic.doc.neuralnets.tests.persistence;

import java.io.Serializable;

import org.apache.log4j.Logger;

import uk.ac.ic.doc.neuralnets.graph.neural.manipulation.GraphFactory;
import uk.ac.ic.doc.neuralnets.graph.neural.NeuralNetwork;
import uk.ac.ic.doc.neuralnets.graph.neural.manipulation.InteractionUtils;
import uk.ac.ic.doc.neuralnets.persistence.FileSpecification;
import uk.ac.ic.doc.neuralnets.persistence.LoadException;
import uk.ac.ic.doc.neuralnets.persistence.LoadManager;
import uk.ac.ic.doc.neuralnets.persistence.SaveException;
import uk.ac.ic.doc.neuralnets.persistence.SaveManager;
import uk.ac.ic.doc.neuralnets.util.configuration.ConfigurationManager;

public class SaveTextSerialisationTest implements Serializable{
	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger(SaveTextSerialisationTest.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) throws SaveException, LoadException{
		ConfigurationManager.configure();
        
        NeuralNetwork nn = GraphFactory.get().makeNetwork( 1000, 1 );
        //new InteractionUtils( nn ).prettyPrintNetwork( System.err );
        log.debug(  "Network of " + nn.getNodes().size() + " nodes and " + nn.getEdges().size() + " edges" );
        long start = System.currentTimeMillis();
        SaveManager.get().save( nn, 
                new FileSpecification( "test.tns", "TextNetworkSerialiser" ) );
        log.info( "Saving took " + (System.currentTimeMillis() - start) + "ms" );
        log.debug( "Done saving, attempting load" );
        
        start = System.currentTimeMillis();
        nn = (NeuralNetwork)LoadManager.get().load( 
                new FileSpecification( "test.tns", "TextNetworkSerialiser" ) );
        log.info( "Loading took " + (System.currentTimeMillis() - start) + "ms" );
        //new InteractionUtils( nn ).prettyPrintNetwork( System.err );
        log.debug( "Network of " + nn.getNodes().size() + " nodes and " + nn.getEdges().size() + " edges" );
        log.debug( "Load complete." );
        System.exit( 0 );
	}

}
