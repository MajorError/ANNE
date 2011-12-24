package uk.ac.ic.doc.neuralnets.graph.neural.manipulation;

import uk.ac.ic.doc.neuralnets.graph.neural.NodeSpecification;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import uk.ac.ic.doc.neuralnets.graph.Graph;
import uk.ac.ic.doc.neuralnets.graph.Node;
import uk.ac.ic.doc.neuralnets.graph.neural.NeuralNetwork;
import uk.ac.ic.doc.neuralnets.events.EventManager;

/**
 * GraphFactory makes Graphs from GraphSpecifications
 * 
 * @see Graph
 * @see GraphSpecification
 * 
 * @author Peter Coetzee
 */
public class GraphFactory {
    
    private static final GraphFactory g = new GraphFactory();
    private static final Logger log = Logger.getLogger( GraphFactory.class );
    public static final int EVENT_RESOLUTION = 200;

    /**
     * Get the instance of this factory
     * @return The GraphFactory.
     */
    public static GraphFactory get() {
        return g;
    }

    /**
     * Make a homogeneous network of n nodes, connected with edgeProb 
     * probability. Utilises the default node type.
     * @param n the number of nodes to create
     * @param edgeProb The probability of edge created
     * @return The NeuralNetwork created
     */
    public NeuralNetwork makeNetwork( int n, double edgeProb ) {
        List<Integer> nodes = new ArrayList<Integer>();
        nodes.add( n );
        return create( new HomogenousNetworkSpecification( nodes, edgeProb ) );
    }
    
    /**
     * Create a Graph conforming to the given GraphSpecification.
     * @param <T> The type of Graph to create
     * @param spec The specification of the graph. Supports some specialisation
     * for homogeneous networks
     * @return The created Graph
     * @see uk.ac.ic.doc.neuralnets.graph.neural.manipulation.HomogenousNetworkSpecification
     */
    public <T extends Graph> T create( GraphSpecification<T> spec ) {
        try {
            T out = spec.getTarget().newInstance();
            Iterator<Integer> it = spec.getNodes().iterator();
            int totalNodes = 0;
            while( it.hasNext() )
                totalNodes += it.next();
            it = spec.getNodes().iterator();
            NodeFactory nf = NodeFactory.get();
            // Add the correct number of nodes for each specification
            for ( NodeSpecification<? extends Node> s : spec.getSpecifications()) {
                Integer num = it.next();
                if ( spec.separateNetworks() ) {
                    out.addNode( (Node<?>)create( spec.getTarget(), s, num, totalNodes ) );
                } else {
                    int freq = num / EVENT_RESOLUTION;
                    freq = freq < 1 ? 1 : freq;
                    for ( int i = 0; i < num; i++ ) {
                        out.addNode( nf.create( s ) );
                        if ( i % freq == 0 )
                            EventManager.get().fire( 
                                    new NodeCreatedEvent( i+1, totalNodes ) );
                    }
                }
            }
            // Now build our edges
            log.debug( "Built nodes, moving on to edges" );
            spec.getEdgeBuilder().transform( out );
            EventManager.get().fire( new EdgeCreatedEvent( 1, 1 ) );
            log.debug( "Finished building network" );
            return out;
        } catch ( InstantiationException ex ) {
            throw new RuntimeException( "Couldn't instantiate Graph", ex );
        } catch ( IllegalAccessException ex ) {
            throw new RuntimeException( "Not permitted to create Graph", ex );
        }
    }
    
    /**
     * Create a Graph of the given type, with the supplied quantity and type of nodes
     * @param <T> The type of graph to create
     * @param type the Class of graph to create
     * @param ntype The NodeSpecification encoding the type of node to include
     * @param quantity The quantity of nodes to produce
     * @return The given neural network
     */
    public <T extends Graph> T create( Class<T> type, NodeSpecification<? extends Node> ntype, int quantity ) {
        return create( type, ntype, quantity, quantity );
    }
        
    /**
     * Create a Graph of the given type, with the supplied quantity and type of nodes
     * @param <T> The type of graph to create
     * @param type the Class of graph to create
     * @param ntype The NodeSpecification encoding the type of node to include
     * @param quantity The quantity of nodes to produce
     * @param totalNodes The total number of nodes that will be produced, for
     * NodeCreatedEvent accuracy.
     * @return The given neural network
     * @see uk.ac.ic.doc.neuralnets.graph.neural.manipulation.NodeCreatedEvent
     */
    private <T extends Graph> T create( Class<T> type, NodeSpecification<? extends Node> ntype, int quantity, int totalNodes ) {
        try {
            T out = type.newInstance();
            int freq = totalNodes / EVENT_RESOLUTION;
            freq = freq < 1 ? 1 : freq;
            NodeFactory nf = NodeFactory.get();
            for ( int i = 0; i < quantity; i++ ) {
                if ( i % freq == 0 )
                    EventManager.get().fire( 
                            new NodeCreatedEvent( i+1, totalNodes ) );
                out.addNode( nf.create( ntype ) );
            }
            return out;
        } catch ( InstantiationException ex ) {
            throw new RuntimeException( "Couldn't instantiate Graph", ex );
        } catch ( IllegalAccessException ex ) {
            throw new RuntimeException( "Not permitted to create Graph", ex );
        }
    }
}
