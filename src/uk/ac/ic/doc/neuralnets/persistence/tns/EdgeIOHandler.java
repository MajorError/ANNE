package uk.ac.ic.doc.neuralnets.persistence.tns;

import java.util.HashMap;
import java.util.Map;
import uk.ac.ic.doc.neuralnets.graph.Edge;
import uk.ac.ic.doc.neuralnets.graph.Node;
import uk.ac.ic.doc.neuralnets.graph.neural.NetworkBridge;

/**
 *
 * @author Peter Coetzee
 */
public class EdgeIOHandler extends ObjectIOEventHandler<Edge> {
    
    private Map<Edge,NetworkBridge> containedIn = new HashMap<Edge,NetworkBridge>();

    @Override
    protected void writeSpecialParts( Edge t ) {
        if ( t instanceof NetworkBridge )
            for ( Edge<? extends Node, ? extends Node> e : ((NetworkBridge)t).getBundle() )
                containedIn.put( e, (NetworkBridge)t );
        todo( t.getStart() );
        todo( t.getEnd() );
        write( String.valueOf( t.getStart().getID() ), ":", String.valueOf( t.getEnd().getID() ), ":" );
        write( containedIn.containsKey( t ) ? 
            String.valueOf( containedIn.get( t ).getID() ) : "-1" );
    }

    @Override
    protected boolean needsHeader( Edge t ) {
        return true;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    protected Edge readSpecialParts( String in, Edge t ) throws NotYetAvailableException {
        String[] parts = in.split( ":" );
        if ( parts.length == 3 ) {
            Integer startID = Integer.valueOf( parts[0] );
            if ( !cache.containsKey( startID ) )
                throw new NotYetAvailableException();
            Integer toID = Integer.valueOf( parts[1] );
            if ( !cache.containsKey( toID ) )
                throw new NotYetAvailableException();
            t.setStart( (Node)cache.get( startID ) );
            t.setTo( (Node)cache.get( toID ) );
            Integer parentID = Integer.valueOf( parts[2] );
            if ( parentID >= 0 ) 
                if ( !cache.containsKey( parentID ) )
                    throw new NotYetAvailableException();
                else
                    ((NetworkBridge)cache.get( parentID )).connect( t );
        }
        return t;
    }

    public String getName() {
        return "EdgeHandler";
    }

    @Override
    protected Class<Edge> getTarget() {
        return Edge.class;
    }

}
