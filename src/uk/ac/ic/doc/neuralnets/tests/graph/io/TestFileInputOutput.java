package uk.ac.ic.doc.neuralnets.tests.graph.io;

import java.util.Collection;
import java.util.Iterator;
import uk.ac.ic.doc.neuralnets.graph.Node;
import uk.ac.ic.doc.neuralnets.gui.graph.ionodes.DATInputNode;
import uk.ac.ic.doc.neuralnets.gui.graph.ionodes.ValueListingOutputNode;
import uk.ac.ic.doc.neuralnets.graph.neural.NeuralNetwork;
import uk.ac.ic.doc.neuralnets.graph.neural.Synapse;
import uk.ac.ic.doc.neuralnets.gui.GUIMain;
import uk.ac.ic.doc.neuralnets.graph.neural.manipulation.InteractionUtils;

/**
 *
 * @author Peter Coetzee
 */
public class TestFileInputOutput {
    
    public static void main( String[] args ) {
        try {
            new Thread( new Runnable() {
                public void run() {
                    // Just to get the Display started, configured etc.
                    GUIMain.main( null );
                }
            } ).start();
            Thread.sleep( 5000 );
        } catch( InterruptedException ex ) { }
        NeuralNetwork in = new DATInputNode().toNetwork();
        NeuralNetwork on = new ValueListingOutputNode().toNetwork( 10 );
        Iterator<Node<?>> iit = in.getNodes().iterator();
        Iterator<Node<?>> oit = on.getNodes().iterator();
        NeuralNetwork n = new NeuralNetwork();
        n.addNode( in );
        n.addNode( on );
        InteractionUtils util = new InteractionUtils( n );
        Collection es = util.connect1to1( in.getNodes(), on.getNodes() );
        for ( Object e : es )
            if ( e instanceof Synapse )
                ((Synapse)e).setWeight( 1d );
        while( true )
            n.tick();
    }

}
