package uk.ac.ic.doc.neuralnets.persistence.tns;

import uk.ac.ic.doc.neuralnets.graph.Edge;
import uk.ac.ic.doc.neuralnets.graph.Node;
import uk.ac.ic.doc.neuralnets.graph.neural.NetworkBridge;

/**
 *
 * @author Peter Coetzee
 */
public class NetworkBridgeIOHandler extends ObjectIOEventHandler<NetworkBridge> {

    @Override
    protected void writeSpecialParts( NetworkBridge t ) {
        for ( Edge<? extends Node, ? extends Node> edge : t.getBundle() )
            todo( edge );
    }

    @Override
    protected boolean needsHeader( NetworkBridge t ) {
        return false;
    }

    @Override
    protected NetworkBridge readSpecialParts( String in, NetworkBridge t ) {
        return t;
    }

    public String getName() {
        return "NetworkBridgeHandler";
    }

    @Override
    protected Class<NetworkBridge> getTarget() {
        return NetworkBridge.class;
    }

}
