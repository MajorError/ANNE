package uk.ac.ic.doc.neuralnets.tests.graph.neural;


import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import uk.ac.ic.doc.neuralnets.graph.neural.manipulation.GraphFactory;
import uk.ac.ic.doc.neuralnets.graph.neural.manipulation.HomogenousNetworkSpecification;
import uk.ac.ic.doc.neuralnets.graph.neural.manipulation.InhibitoryNodeSpecification;
import uk.ac.ic.doc.neuralnets.graph.neural.NodeSpecification;
import uk.ac.ic.doc.neuralnets.graph.neural.manipulation.SpikingNodeSpecification;
import uk.ac.ic.doc.neuralnets.graph.neural.NeuralNetwork;
import uk.ac.ic.doc.neuralnets.persistence.LoadException;
import uk.ac.ic.doc.neuralnets.persistence.SaveException;
import uk.ac.ic.doc.neuralnets.events.EventManager;
import uk.ac.ic.doc.neuralnets.graph.neural.NodeFired;
import uk.ac.ic.doc.neuralnets.gui.events.RealTimePlotStatistician;
import uk.ac.ic.doc.neuralnets.util.configuration.ConfigurationManager;

public class SpikingNetworkTest {
    
    private Logger log = Logger.getLogger( SpikingNetworkTest.class );

    public void testSimpleNetwork() throws SaveException, LoadException {
        ConfigurationManager.configure();
        
        log.info( "Preparing network" );
        List<NodeSpecification> specs = new ArrayList<NodeSpecification>();
        specs.add( new SpikingNodeSpecification() );
        specs.add( new InhibitoryNodeSpecification() );
        List<Integer> nodes = new ArrayList<Integer>();
        nodes.add( 800 );
        nodes.add( 200 );
        NeuralNetwork n = GraphFactory.get().create( 
                new HomogenousNetworkSpecification( specs, nodes, 1 ) );
        EventManager.get().registerAsync( NodeFired.class, new RealTimePlotStatistician() );
        
        //SaveSpecification spec = new FileSpecification( "n1000.net", "Serialiser" );
        //NeuralNetwork n = LoadManager.getInstance().load( spec );
        log.info( "Simulating network of " + n.getNodes().size() 
                + " neurones and " + n.getEdges().size() + " synapses." );
        
        for ( int i = 0; i < 1000; i++ )
            n.tick();
                
        EventManager.get().flushAll();
        log.info( "Flushed" );
        //SaveManager.getInstance().save( n, spec );
    }

    public static void main( String[] args )  {
        try {
            new SpikingNetworkTest().testSimpleNetwork();
        } catch ( Exception ex ) {
            ex.printStackTrace( System.out );
        }
    }
}
