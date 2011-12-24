package uk.ac.ic.doc.neuralnets.graph.neural.manipulation;

import uk.ac.ic.doc.neuralnets.graph.neural.EdgeSpecification;
import java.io.Serializable;

import org.apache.log4j.Logger;
import uk.ac.ic.doc.neuralnets.graph.Edge;
import uk.ac.ic.doc.neuralnets.graph.Node;
import uk.ac.ic.doc.neuralnets.graph.neural.NetworkBridge;
import uk.ac.ic.doc.neuralnets.graph.neural.NeuralNetwork;
import uk.ac.ic.doc.neuralnets.graph.neural.Neurone;
import uk.ac.ic.doc.neuralnets.graph.neural.Synapse;

/**
 * EdgeFactory creates Edges from EdgeSpecifications
 * 
 * @see Edge
 * @see EdgeSpecification
 * 
 * @author Peter Coetzee
 *
 */
public class EdgeFactory implements Serializable{
    
    private static final long serialVersionUID = 1L;
    private static final Logger log = Logger.getLogger( EdgeFactory.class );
    private static final EdgeFactory e = new EdgeFactory();

    /**
     * Get the factory instance.
     * @return the EdgeFactory
     */
    public static EdgeFactory get() {
        return e;
    }

    /**
     * Create an edge between the supplied nodes
     * @param <From> The type of the start node
     * @param <To> The type of the terminus of this edge
     * @param f The start node
     * @param t The end node
     * @return The created edge
     * @see uk.ac.ic.doc.neuralnets.graph.Edge
     * @see uk.ac.ic.doc.neuralnets.graph.Node
     */
    public <From extends Node<?>, To extends Node<?>> Edge<From,To> create( final From f, final To t ) {
        return create( new EdgeSpecification<From,To>() {
            
            @Override
            public From getStart() {
                return f;
            }
            
            @Override
            public To getEnd() {
                return t;
            }
            
        } );
    }

    /**
     * Create an edge conforming to the given EdgeSpecification. Currently it
     * is required that <From> and <To> are the same type. If they are both
     * Neurones, a Synapse is created. If they are both NeuralNetworks, a
     * NetworkBridge is constructed.
     * @throws UnsupportedOperationException When the types of the nodes are
     * unsupported in this version of the factory.
     * @param <From> The type of the source node
     * @param <To> The type of the destination node
     * @param s The EdgeSpecification to use
     * @return The created edge
     * @see uk.ac.ic.doc.neuralnets.graph.neural.Neurone
     * @see uk.ac.ic.doc.neuralnets.graph.neural.NeuralNetwork
     * @see uk.ac.ic.doc.neuralnets.graph.Edge
     * @see uk.ac.ic.doc.neuralnets.graph.Node
     * @see uk.ac.ic.doc.neuralnets.graph.EdgeSpecification
     */
    @SuppressWarnings("unchecked")
	public <From extends Node<?>, To extends Node<?>> Edge<From,To> create( EdgeSpecification<From,To> s ) {
        if ( s.getStart() instanceof Neurone && s.getEnd() instanceof Neurone )
            return (Edge<From, To>) new Synapse( s.getWeight(), (Neurone)s.getStart(), (Neurone)s.getEnd() );
        if ( s.getStart() instanceof NeuralNetwork && s.getEnd() instanceof NeuralNetwork )
            return (Edge<From, To>) new NetworkBridge( (NeuralNetwork)s.getStart(), (NeuralNetwork)s.getEnd() );
        log.error( "Couldn't link " + s.getStart() + " --> " + s.getEnd() );
        throw new UnsupportedOperationException( "Can only link Nodes of the same type currently" );
    }
    
}
