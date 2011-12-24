package uk.ac.ic.doc.neuralnets.persistence.tns;

import java.util.HashMap;
import java.util.Map;
import uk.ac.ic.doc.neuralnets.graph.Edge;
import uk.ac.ic.doc.neuralnets.graph.Identifiable;
import uk.ac.ic.doc.neuralnets.graph.neural.NeuralNetwork;

/**
 *
 * @author Peter Coetzee
 */
public class EdgeParentInsertionIOHandler extends ObjectIOEventHandler<Identifiable> {
    
    private Map<Edge,NeuralNetwork> inNetwork = new HashMap<Edge,NeuralNetwork>();

    @Override
    protected void writeSpecialParts( Identifiable t ) {
        if ( t instanceof NeuralNetwork ) {
            for ( Edge<?, ?> e : ((NeuralNetwork)t).getEdges() ) {
                inNetwork.put( e, (NeuralNetwork)t );
                todo( e );
            }
        } else if ( t instanceof Edge ) {
            if ( inNetwork.containsKey( t ) ) {
                write( String.valueOf( inNetwork.get( t ).getID() ) );
            } else {
                written.remove( t.getID() );
                todo( t );
            }
        }
    }

    @Override
    protected boolean needsHeader( Identifiable t ) {
        return t instanceof Edge && inNetwork.containsKey( t );
    }

    @Override
    protected Identifiable readSpecialParts( String in, Identifiable t ) throws NotYetAvailableException {
        if ( t instanceof Edge ) {
            if ( in.contains( ":" ) || in.length() < 1 )
                return t;
            Integer id = Integer.valueOf( in );
            if( id < 0 )
                return t; // no parent
            if ( !cache.containsKey( id ) )
                throw new NotYetAvailableException();
            NeuralNetwork parent = (NeuralNetwork)cache.get( id );
            parent.addEdge( (Edge)t );
        }
        return t;
    }

    @Override
    protected Class<Identifiable> getTarget() {
        return Identifiable.class;
    }

    public String getName() {
        return "EdgeParentInserter";
    }

}
