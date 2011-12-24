package uk.ac.ic.doc.neuralnets.graph.neural.manipulation;

import uk.ac.ic.doc.neuralnets.graph.neural.*;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

import uk.ac.ic.doc.neuralnets.graph.neural.manipulation.EdgeCreatedEvent;
import uk.ac.ic.doc.neuralnets.events.EventManager;
import uk.ac.ic.doc.neuralnets.events.GraphUpdateEvent;
import uk.ac.ic.doc.neuralnets.graph.neural.NeuralNetworkSimulationEvent;
import uk.ac.ic.doc.neuralnets.graph.neural.NeuralNetworkTickEvent;
import uk.ac.ic.doc.neuralnets.graph.Edge;
import uk.ac.ic.doc.neuralnets.graph.Node;
import uk.ac.ic.doc.neuralnets.graph.Graph.Command;
import uk.ac.ic.doc.neuralnets.graph.neural.manipulation.EdgeFactory;
import uk.ac.ic.doc.neuralnets.graph.neural.EdgeSpecification;
import uk.ac.ic.doc.neuralnets.graph.neural.manipulation.GraphFactory;
import uk.ac.ic.doc.neuralnets.graph.neural.manipulation.GraphSpecification;
import uk.ac.ic.doc.neuralnets.graph.neural.NetworkBridge;
import uk.ac.ic.doc.neuralnets.graph.neural.NeuralNetwork;
import uk.ac.ic.doc.neuralnets.graph.neural.Neurone;
import uk.ac.ic.doc.neuralnets.graph.neural.Synapse;
import uk.ac.ic.doc.neuralnets.util.Transformer;

/**
 *
 * @author Peter Coetzee
 */
public class InteractionUtils {
    
    private NeuralNetwork network;
    private NetworkRunner networkRunner = new NetworkRunner();
    private Logger log = Logger.getLogger( InteractionUtils.class );
    
    /**
     * @param n The NeuralNetwork to operate over
     */
    public InteractionUtils( NeuralNetwork n ) {
        setNetwork( n );
        networkRunner.start();
    }
    
    /**
     * @param n The NeuralNetwork to operate over
     */
    public void setNetwork( NeuralNetwork n ) {
        network = n;
    }
    
    /**
     * @return The NeuralNetwork that backs these utils
     */
    public NeuralNetwork getNetwork() {
        return network;
    }
    
    /**
     * Find the network which contains the given node. NB: Our semantics of
     * containment dictate that the root network is contained by itself.
     * @param n The node to seek
     * @return The NeuralNetwork that contains it, or null if such could not
     * be found
     */
    public NeuralNetwork findNetwork( Node n ) {
        return (n == network) ? network : findNetwork( n, network );
    }
    
    /**
     * Helper function, to find the given node in the selected network
     * @param n The node to seek
     * @param curr The network in which to recursively look
     * @return The NeuralNetwork that contains n, or null if such could not 
     * be found
     */
    private NeuralNetwork findNetwork( Node n, NeuralNetwork curr ) {
        if ( curr.getNodes().contains( n ) )
            return curr;
        NeuralNetwork nn = null;
        for ( Node<?> t : curr.getNodes() ) {
            if ( t instanceof NeuralNetwork )
                nn = findNetwork( n, (NeuralNetwork)t );
            if ( nn != null )
                return nn;
        }
        return null;
    }
    
    /**
     * Answers whether network a is a parent of network b
     * @param a The parent node to test
     * @param b The child node to seek
     * @return true iff a is a parent of b
     */
    public boolean isSuper( NeuralNetwork a, NeuralNetwork b ) {
        if ( a == b )
            return true;
        for ( Node<?> n : a.getNodes() )
            if ( n instanceof NeuralNetwork && isSuper( (NeuralNetwork)n, b ) )
                return true;
        return false;
    }
    
    /**
     * Answers whether Node a is a super-node of node b (i.e. a parent)
     * @param a The parent node to test
     * @param b The child node to seek
     * @return true iff a is a parent of b
     */
    public boolean isSuper( Node a, Node b ) {
        return (a instanceof NeuralNetwork && b instanceof NeuralNetwork) ?
            isSuper( (NeuralNetwork)a, (NeuralNetwork)b ) : false;
    }
    
    /**
     * Find the lowest common ancestor of Nodes a and b; i.e. the deepest
     * NeuralNetwork in the tree of networks that contains both a and b.
     * 
     * Algorithm:
     * Iterate up the parents of a and b until an intersection in the sets
     * of their ancestors is found; at that point, we have th lowest common
     * ancestor and can return
     * @param a The first node to seek
     * @param b The second node to seek
     * @return The lowest common ancestor of a and b, or null if it could not
     * be found (in a correct network, this shouldn't be possible)
     */
    public NeuralNetwork lowestCommonAncestor( Node a, Node b ) {
        Set<NeuralNetwork> as = new HashSet<NeuralNetwork>();
        Set<NeuralNetwork> bs = new HashSet<NeuralNetwork>();
        Set<NeuralNetwork> i = new HashSet<NeuralNetwork>();
        NeuralNetwork n1 = findNetwork( a );
        NeuralNetwork n2 = findNetwork( b );
        // while the network will grow
        while( !(as.contains( n1 ) && bs.contains( n2 )) ) {
            as.add( n1 );
            bs.add( n2 );
            i.addAll( as );
            i.retainAll( bs );
            // Is there an intersection in the sets?
            if ( i.size() > 0 )
                return i.iterator().next();
            n1 = findNetwork( n1 );
            n2 = findNetwork( n2 );
        }
        // Should be unreachable; do we want to return 'network' in this case?
        return null;
    }
    
    /**
     * Create some nodes in the network
     * @param nodes The number of nodes to create
     * @param edgeProb The probability a given edge should be made
     * @return The nodes added, as a new network
     */
    public NeuralNetwork createNodes( int nodes, double edgeProb ) {
        NeuralNetwork out = GraphFactory.get().makeNetwork( nodes, edgeProb );
        network.addNode( out );
        return out;
    }
    
    /**
     * Create some nodes in the network
     * @param spec The specification of how to add nodes and edges
     * @return The nodes added, as a new network
     */
    public NeuralNetwork createNodes( GraphSpecification<NeuralNetwork> spec ) {
        NeuralNetwork out = GraphFactory.get().create( spec );
        network.addNode( out );
        return out;
    }
    
    /**
     * Run the network for the given number of ticks
     * @param ticks How long to run for, or < 0 for "forever"
     */
    public void runNetwork( final int ticks ) {
        networkRunner.runNetwork( ticks );
    }
    
    /**
     * Run the network from the last tick state (i.e. resume)
     */
    public void runNetwork() {
        networkRunner.runNetwork();
    }
    
    /**
     * Pause the network from running
     */
    public void pauseNetwork() {
        networkRunner.pauseNetwork();
    }
    
    public void resetNetwork(){
    	resetNetwork(network);
    	EventManager.get().fire(new GraphUpdateEvent());
    	
    }
    
    /*
     * Rerandomise the edge weights
     * Reset neurone weights to initial charges.
     * Reset the neural networks' ticks.
     */
	private void resetNetwork(NeuralNetwork nn) {
		EdgeSpecification<Node<?>, Node<?>> spec = new EdgeSpecification<Node<?>, Node<?>>();
		for(Node<?> n : nn.getNodes()){
			if(n instanceof Neurone){
				((Neurone) n).reset();
				for(Edge<?,?> e : n.getOutgoing()){
					if(e instanceof Synapse){
						((Synapse) e).setWeight(spec.getWeight());
					}
				}
			}
			if(n instanceof NeuralNetwork){
				resetNetwork((NeuralNetwork) n);
			}
		}
		nn.resetTicks();
	}
    
    /**
     * Connect the given nodes in any networks. If the network of f is the
     * same as the network of t, return a synpase in that network. Otherwise,
     * create a bridge from network of f to network of t, and route a synapse
     * through its bundle. If network of f is a super-node of the network of 
     * t, then bridges are still created. Bridges and synapses are always
     * re-used where possible.
     * 
     * Given a network with two sub-networks, n1 and n2, and n2 containing n3,
     * a synapse from a neurone in n1 to a neurone in n3 most route over a
     * network bridge to n2, then a network bridge from n2 to n3, and finally
     * act as a synapse from n3's input to the synapse.
     * 
     * Connecting a network to its parent results in a null connection, as it
     * is not necessary.
     * @param <From> The type of the source node
     * @param <To> The type of the target node
     * @param f The node to connect from
     * @param t The node to connect to
     * @return The edge that connects these nodes, or null if no such
     * connection is possible
     */
    @SuppressWarnings( "unchecked" )
    public <From extends Node<?>, To extends Node<?>> Edge<From,To> 
            connect( From f, To t ) {
        // Does the edge exist already?
        for ( Edge edge : f.getOutgoing() )
            if ( edge.getEnd() == t )
                return edge;
        Edge<From,To> e = EdgeFactory.get().create( f, t );
        NeuralNetwork fn = findNetwork( f );
        NeuralNetwork tn = findNetwork( t );
        // Small sanity checks
        if ( fn == null )
            throw new UnsupportedOperationException( "Node " + f 
                    + " doesn't belong to a network!" );
        if ( tn == null )
            throw new UnsupportedOperationException( "Node " + t 
                    + " doesn't belong to a network!" );
        // Perform the connection
        if ( fn == tn ) {
            fn.addEdge( e );
            // Ensure the nodes know they are connected
            ((Node<Edge<From,To>>)f).connect( e );
            ((Node<Edge<From,To>>)t).connect( e );
        } else {
            NetworkBridge e2 = connectNetworks( fn, tn );
            //log.debug( "Got " + e2 );
            if ( e2 != null )
                e2.connect( e );
        }
        return e;
    }
    
    private NetworkBridge connectNetworks( NeuralNetwork n1, NeuralNetwork n2 ) {
        for ( Edge edge : n1.getOutgoing() )
            if ( edge.getEnd() == n2 )
                return (NetworkBridge)edge;
        NeuralNetwork n1p = findNetwork( n1 );
        NeuralNetwork n2p = findNetwork( n2 );
        //log.trace( "Link " + n1.getID() + " --> " + n2.getID() );
        if ( isSuper( n1, n2 ) )
            return null;
        // Contained within same network
        if ( n1p == n2p ) {
            NetworkBridge nb = (NetworkBridge)EdgeFactory.get().create( n1, n2 );
            n1p.addEdge( nb ); // adds it as an edge to parents of n1 and n2
            return nb;
        }
        //log.trace( "Not in same network, " + n1p.getID() + " --> " + n2p.getID() );
        NetworkBridge nb = null;
        if ( n1p == network )
            nb = connectNetworks( n1, n2p );
        else if ( n2p == network )
            nb = connectNetworks( n1p, n2 );
        else
            nb = connectNetworks( n1p, n2p );
        if ( nb != null )
            nb.connect( EdgeFactory.get().create( n1, n2 ) );
        return nb;
    }
    
    /**
     * Fully connect the given sets of nodes in the network
     * @param <From> The type of the source node
     * @param <To> The type of the target node
     * @param f The source node
     * @param t The target node
     * @return The collection of created edges
     */
    public <From extends Node<?>, To extends Node<?>> Collection<Edge<From,To>>
            connect( Collection<From> f, Collection<To> t ) {
        return connect( f, t, 1 );
    }
    
    /**
     * Connect the given sets of nodes in the network with the chosen 
     * probability of edge creation
     * @param <From> The type of the source node
     * @param <To> The type of the target node
     * @param f The source node
     * @param t The target node
     * @param edgeProb The probability a given edge is created
     * @return The collection of created edges
     */
    public <From extends Node<?>, To extends Node<?>> Collection<Edge<From,To>>
            connect( Collection<From> f, Collection<To> t, double edgeProb ) {
        Collection<Edge<From,To>> out = new ArrayList<Edge<From,To>>();
        int num = 0;
        int approxTotal = (int)Math.ceil( f.size() * t.size() * edgeProb );
        int freq = approxTotal / GraphFactory.EVENT_RESOLUTION;
        freq = freq < 1 ? 1 : freq;
        for ( From fr : f ) {
            for ( To to : t ) {
                Class frc = fr instanceof Neurone ? Neurone.class : NeuralNetwork.class;
                Class toc = to instanceof Neurone ? Neurone.class : NeuralNetwork.class;
                if ( Math.random() < edgeProb  && frc.equals( toc ) ) {
                    out.add( connect( fr, to ) );
                    num++;
                    if ( num % freq == 0 )
                        EventManager.get().fire( 
                                new EdgeCreatedEvent( num, f.size() ) );
                }
            }
        }
        return out;
    }
    
    /**
     * Connect the given sets of nodes in the network with a 1-1 connection
     * mapping (i.e. each node in f connects to one node in t) to as great an
     * extent as possible. If there are insufficient nodes in t, some may be
     * re-used
     * @param <From> The type of the source node
     * @param <To> The type of the target node
     * @param f The source node
     * @param t The target node
     * @return The collection of created edges
     */
    public <From extends Node<?>, To extends Node<?>> Collection<Edge<From,To>>
            connect1to1( Collection<From> f, Collection<To> t ) {
        Collection<Edge<From,To>> out = new ArrayList<Edge<From,To>>();
        Iterator<From> fit = f.iterator();
        Iterator<To> tit = t.iterator();
        int num = 0;
        while( fit.hasNext() ) {
            if ( !tit.hasNext() )
                tit = t.iterator();
            out.add( connect( fit.next(), tit.next() ) );
            EventManager.get().fire( new EdgeCreatedEvent( ++num, f.size() ) );
        }
        return out;
    }
    
    /**
     * Print out the network to the given PrintStream
     * @param out The PrintStream to which to print
     */
    public void prettyPrintNetwork( PrintStream out ) {
        prettyPrintNetwork( network, out, "" );
    }
    
    /**
     * Print out the network to the given PrintStream
     * @param nn The network to print
     * @param out The PrintStream to which to print
     * @param t The tab-level to pre-pend to this level
     */
    private void prettyPrintNetwork( NeuralNetwork nn, PrintStream out, String t ) {
        out.print( "\n" + t + nn.toString() );
        out.print( " has " + nn.getIncoming().size() + " incoming edges, and " );
        out.print( nn.getOutgoing().size() + " outgoing edges," );
        out.print( " with Nodes: {\n\t" + t );
        for ( Node<?> n : nn.getNodes() )
            if ( n instanceof NeuralNetwork )
                prettyPrintNetwork( (NeuralNetwork)n, out, t + "\t" );
            else
                out.print( n.toString() + ", " );
        out.print( "\n" + t + "}\n" + t + "Edges for " + nn.getID() + ": {\n\t" + t );
        for ( Edge<?, ?> e : nn.getEdges() )
            out.print( e + ", " );
        out.println( "\n" + t + "}" );
    }
    
    /**
     * Extract the nodes from n that are selected by the knife, removing them
     * from the network and instead creating a new network.
     * 
     * Any edges in n that are into or out of knife are instead routed via a
     * NetworkBridge.
     * 
     * The resultant network is added to the parent network of n automatically.
     * @param n The network to bifurcate
     * @param knife A transformer to select the nodes to remove
     * @return The resultant (new) bifurcated network
     */
    public NeuralNetwork bifurcate( NeuralNetwork n, 
            final Transformer<Node,Boolean> knife ) {
        // Long winded way of saying new NeuralNetwork(). Might need this
        // logic later though...
        final NeuralNetwork out = GraphFactory.get().create( 
                new GraphSpecification<NeuralNetwork>() {
            @Override
            public Class<NeuralNetwork> getTarget() {
                return NeuralNetwork.class;
            }
            @Override
            public boolean separateNetworks() {
                return false;
            }
        } );
        n.forEachNode( new Command<Node<?>>() {

            public void exec( Node<?> n ) {
                if ( knife.transform( n ) )
                    out.addNode( n );
            }
            
        } );
        findNetwork( n ).addNode( out );
        for ( Node<?> on : out.getNodes() ) {
            // Remove this node and its edges
            n.getNodes().remove( on );
            n.getEdges().removeAll( on.getOutgoing() );
            n.getEdges().removeAll( on.getIncoming() );
            Collection<Edge> oo = new ArrayList<Edge>( on.getOutgoing() );
            Collection<Edge> oi = new ArrayList<Edge>( on.getIncoming() );
            on.getOutgoing().clear();
            on.getIncoming().clear();
            // Remove edges from all relevant NetworkBridge bundles
            for ( NetworkBridge nb : n.getIncoming() )
                nb.getBundle().removeAll( oi );
            for ( NetworkBridge nb : n.getOutgoing() )
                nb.getBundle().removeAll( oo );
            // Reconnect edges
            for ( Edge e : oo )
                connect( on, e.getEnd() );
            for ( Edge e : oi )
                connect( e.getStart(), on );
        }
        return out;
    }
    
    /**
     * Cause this instance to stop any threads it may have spawned, and release
     * its resources. Any further operations have undefined behaviour.
     */
    public void teardown() {
        networkRunner.kill();
        network = null;
    }
        
    /**
     * The thread used to run the network asynchronously with the UI
     */
    protected class NetworkRunner extends Thread {
            
        private int ticks = -1;
        private boolean run = false, alive = true;
        
        public void runNetwork() {
            ticks = -1;
            run = true;
            EventManager.get().fire( new NeuralNetworkSimulationEvent( true ) );
        }
        
        public void runNetwork( int ticks ) {
            if(getRemainingTicks() < 1 || ticks == 0){
            	setTicks( ticks );
            	if(ticks == 0) return;
            	log.trace("Running network for " + ticks + " tick(s)");
            }else{
            	log.info("Running " + getRemainingTicks() + " remaining steps");
            }
            run = true;
            EventManager.get().fire( new NeuralNetworkSimulationEvent( true ) );
        }
        
        public void pauseNetwork() {
            run = false;
        }
        
        public void kill() {
            alive = false;
            if ( run )
                pauseNetwork();
        }
        
        public void setTicks( int ticks ) {
            this.ticks = ticks;
        }
        
        public int getRemainingTicks() {
            return ticks;
        }

        @Override
        public void run() {
            setName( "Neural Network Runner" );
        	boolean ran = false;
            while( alive ) {
                while( run && ticks != 0 ) {
                	ran = true;
                    ticks -= ticks < 0 ? 0 : 1;
                    network.tick();
                    log.trace("Ticked network " + ticks);
                    EventManager.get().fire( new NeuralNetworkTickEvent(ticks));
                }
                if ( ran ) {
                	EventManager.get().fire( new NeuralNetworkSimulationEvent( false ) );
                	ran = false;
                }
                try {
                    Thread.sleep( 1000 );
                } catch ( InterruptedException ex ) { }
            }
        }

    }

}
