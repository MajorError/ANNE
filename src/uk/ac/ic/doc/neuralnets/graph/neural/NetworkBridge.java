
package uk.ac.ic.doc.neuralnets.graph.neural;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import uk.ac.ic.doc.neuralnets.graph.Edge;
import uk.ac.ic.doc.neuralnets.graph.Node;

/**
 * Models a connection between two NeuralNetworks as a bundle of synapses
 * @author Peter Coetzee
 */
public class NetworkBridge extends EdgeBase<NeuralNetwork,NeuralNetwork> {

    private static final long serialVersionUID = 5002732960499392264L;
    
    private Set<Edge<? extends Node,? extends Node>> bundle 
            = new HashSet<Edge<? extends Node,? extends Node>>();
    
    public NetworkBridge() {
        this( null, null );
    }
    
    public NetworkBridge( NeuralNetwork start, NeuralNetwork end ) {
        super( start, end );
        if ( start != null )
            start.connect( this );
        if ( end != null )
            end.connect( this );
    }
    
    @SuppressWarnings( "unchecked" )
    public <From extends Node<?>, To extends Node<?>> Edge<From,To> connect( Edge<From,To> e ) {
        bundle.add( e );
        ((Node)e.getStart()).connect( e );
        ((Node)e.getEnd()).connect( e );
        return e;
    }
    
    public Collection<Edge<? extends Node,? extends Node>> getBundle() {
        return bundle;
    }
    
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append( "Network Bridge " + (start == null ? "NONE" : start.getID()) + " --> " 
                + (end == null ? "NONE" : end.getID()) + " (" + bundle.size() + " edges)" );
        /* + "[\n" );
        for ( Edge e : bundle)
            s.append( "\t" + e + "\n" );
        s.append( "]" );*/
        return s.toString();
    }

}
