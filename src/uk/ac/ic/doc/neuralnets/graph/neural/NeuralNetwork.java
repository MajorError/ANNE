
package uk.ac.ic.doc.neuralnets.graph.neural;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import uk.ac.ic.doc.neuralnets.graph.Graph;
import uk.ac.ic.doc.neuralnets.graph.Node;
import uk.ac.ic.doc.neuralnets.graph.Saveable;

/**
 *
 * @author Peter Coetzee
 */
public class NeuralNetwork extends Graph implements Node<NetworkBridge>, Saveable {
    
	private static final long serialVersionUID = 441914212077428988L;
	
	private Set<NetworkBridge> in = new HashSet<NetworkBridge>(), 
                out = new HashSet<NetworkBridge>();
    private Map<String,String> metadata = new HashMap<String,String>();
    private int xpos = 0, ypos = 0, zpos = 0, ticks = 0;
    
    public void setPos( int x, int y, int z ) {
    	xpos = x;
    	ypos = y;
    	zpos = z;
    }
    
    public int getTicks() {
        return ticks;
    }
    
    public void resetTicks() {
        ticks = 0;
    }
    
    public int getX() {
        return xpos;
    }
    
    public int getY() {
        return ypos;
    }
    
    public int getZ() {
        return zpos;
    }
    
    public Collection<NetworkBridge> getOutgoing() {
        return out;
    }
    
    public Collection<NetworkBridge> getIncoming() {
        return in;
    }

    public Node<NetworkBridge> connect( NetworkBridge e ) {
        (e.getStart().equals( this ) ? out : in).add( e );
        return this;
    }
    
    public Node<NetworkBridge> tick() {
        ticks++;
        for ( Node<?> n : nodes )
            n.tick();
        return this;
    }
    
    public Node<NetworkBridge> setMetadata( String key, String item ) {
        metadata.put( key, item );
        return this;
    }
    
    public String getMetadata( String key ) {
        if ( !metadata.containsKey( key ) )
            throw new IllegalArgumentException( "No such metadata: " + key );
        return metadata.get( key );
    }
    
    @Override
    protected String type() {
        return "Neural Network";
    }

}
