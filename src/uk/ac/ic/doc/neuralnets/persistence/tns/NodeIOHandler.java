package uk.ac.ic.doc.neuralnets.persistence.tns;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import uk.ac.ic.doc.neuralnets.graph.Edge;
import uk.ac.ic.doc.neuralnets.graph.Node;
import uk.ac.ic.doc.neuralnets.graph.neural.NeuralNetwork;

/**
 *
 * @author Peter Coetzee
 */
public class NodeIOHandler extends ObjectIOEventHandler<Node> {
    
    private Map<Node,Node> hasParent = new HashMap<Node,Node>();

    @Override
    @SuppressWarnings( "unchecked" )
    protected void writeSpecialParts( Node t ) {
        if ( t instanceof NeuralNetwork ) {
            NeuralNetwork n = (NeuralNetwork)t;
            for ( Node<?> node : n.getNodes() ) {
                hasParent.put( node, n );
                todo( node );
            }
        }
        for ( Edge edge : (Collection<Edge>)t.getIncoming() ) {
            todo( edge.getStart() );
            todo( edge );
        }
        for ( Edge edge : (Collection<Edge>)t.getOutgoing() ) {
            todo( edge.getEnd() );
            todo( edge );
        }
        if ( hasParent.containsKey( t ) )
            write( String.valueOf( hasParent.get( t ).getID() ) );
    }

    @Override
    protected boolean needsHeader( Node t ) {
        return hasParent.containsKey( t );
    }

    @Override
    protected Node readSpecialParts( String in, Node t ) throws NotYetAvailableException {
        if ( in.length() > 0 ) {
            Integer id = Integer.valueOf( in );
            if ( !cache.containsKey( id ) )
                throw new NotYetAvailableException();
            NeuralNetwork parent = (NeuralNetwork)cache.get( id );
            parent.addNode( t );
        }
        return t;
    }

    public String getName() {
        return "NodeHandler";
    }

    @Override
    protected Class<Node> getTarget() {
        return Node.class;
    }

}
