package uk.ac.ic.doc.neuralnets.graph.neural.train;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import uk.ac.ic.doc.neuralnets.events.Event;
import uk.ac.ic.doc.neuralnets.events.EventHandler;
import uk.ac.ic.doc.neuralnets.events.EventManager;
import uk.ac.ic.doc.neuralnets.graph.neural.NodeFired;
import uk.ac.ic.doc.neuralnets.expressions.ExpressionException;
import uk.ac.ic.doc.neuralnets.graph.Edge;
import uk.ac.ic.doc.neuralnets.graph.Node;
import uk.ac.ic.doc.neuralnets.graph.neural.io.IONeurone;
import uk.ac.ic.doc.neuralnets.graph.neural.io.InputNode;
import uk.ac.ic.doc.neuralnets.graph.neural.NeuralNetwork;
import uk.ac.ic.doc.neuralnets.graph.neural.SpikingNeurone;
import uk.ac.ic.doc.neuralnets.graph.neural.Synapse;
import uk.ac.ic.doc.neuralnets.util.configuration.ConfigurationManager;

/**
 *
 * @author Peter Coetzee
 */
public class STDPTrainer implements Trainer, EventHandler {
    
    private int testLength = 1000;
    protected Collection<InputNode> inputNodes;
    /** Synaptic weight derivatives */
    private Map<Synapse,Double> sd = new HashMap<Synapse,Double>();
    private Map<SpikingNeurone,Integer> lastFiring
            = new HashMap<SpikingNeurone,Integer>();
    private final double fireRatio = 0.95, fireReset = 0.1;
    
    private static final Logger log = Logger.getLogger( STDPTrainer.class  );
    
    public void setTestLength( int it ) {
        testLength = it;
    }
    
    public void setInputs( InputNode in ) {
        inputNodes = new ArrayList<InputNode>( 1 );
        inputNodes.add( in );
    }
    
    public void setInputs( Collection<InputNode> in ) {
        inputNodes = in;
    }

    public double trainOnce( NeuralNetwork n ) {
        return trainOnce( n, true );
    }
    
    private double trainOnce( NeuralNetwork n, boolean external ) {
        if ( external )
            setup( n );
        
        runNetwork( n );
        Map<Synapse,Double> nextSD = new HashMap<Synapse,Double>();
        for ( Map.Entry<Synapse, Double> e : sd.entrySet() ) {
            Synapse s = e.getKey();
            double old = s.getWeight();
            s.setWeight( Math.max( 0d, Math.min( 10d, 
                    0.01d + s.getWeight() + e.getValue() ) ) );
            if ( e.getValue() != 0d ) {
                //log.trace( s + " from " + old );
                nextSD.put( s, 0.9d * e.getValue() );
            }
        }
        sd = nextSD;
        
        if ( external )
            teardown();
        return Double.MAX_VALUE;
    }

    public double trainFully( NeuralNetwork n, double errorTarget, int maxIt ) {
        setup( n );
        double last = Double.MAX_VALUE;
        for ( int i = 0; i < maxIt && last > errorTarget; i++ )
            last = trainOnce( n, false );
        teardown();
        return last;
    }
    
    private void setup( NeuralNetwork n ) {
        for ( Node<?> node : n.getNodes() ) {
            if ( node instanceof SpikingNeurone ) {
                List<Double> l = new ArrayList<Double>( testLength );
                for ( int i = 0; i < testLength; i++ )
                    l.add( 0d );
            }
        }
        EventManager.get().registerSynchro( NodeFired.class, this );
    }
    
    private void teardown() {
        lastFiring.clear(); // drop old pointers to let GC occur
        EventManager.get().deregisterSynchro( NodeFired.class, this );
    }

    public String getName() {
        return "STDP";
    }

    private void runNetwork( NeuralNetwork n ) {
        int h = 0;
        for ( InputNode in : inputNodes )
            h = Math.max( h, in.getData().getHeight() );
        int row = 0;
        for ( int i = 0; i < testLength * h; i++ ) {
            n.tick();
            if ( i % testLength == 0 )
                for ( InputNode in : inputNodes )
                    in.setRow( row++ % in.getData().getHeight() );
        }
    }
    
    /**
     * Calculate STDP
     * STDP starts at 0.1 (fireReset) and then decays by factor of 0.95
     * (fireRatio) each tick after that until the node firing reaches the target
     * This method just collapses the progression into a Math.pow() operation
     */
    private final double stdp( SpikingNeurone from, int tick ) {
        return lastFiring.containsKey( from ) ? 
            fireReset * Math.pow( fireRatio, 
                    Math.max( 0, tick - lastFiring.get( from ) - 1 ) )
            : 0;
    }

    public void handle( Event e ) {
        if ( !(e instanceof NodeFired) )
            return;
        NodeFired nf = (NodeFired)e;
        if ( nf.getNode() instanceof SpikingNeurone ) {
            SpikingNeurone n = (SpikingNeurone)nf.getNode();
            lastFiring.put( n, nf.getTick() );
            // Work out SD for outgoing and incoming synapses
            for ( Synapse s : n.getOutgoing() )
                sd.put( s, (sd.containsKey( s ) ? sd.get( s ) : 0d) 
                        - 0.8 * stdp( n, nf.getTick() + s.getDelay() ) );
            for ( Synapse s : n.getIncoming() )
                if ( s.getStart() instanceof SpikingNeurone )
                    sd.put( s, (sd.containsKey( s ) ? sd.get( s ) : 0d) 
                            + stdp( (SpikingNeurone)s.getStart(), 
                                nf.getTick() + s.getDelay() ) );
        }
    }

    public void flush() {
        // no-op
    }

    public boolean isValid() {
        return true;
    }


}
