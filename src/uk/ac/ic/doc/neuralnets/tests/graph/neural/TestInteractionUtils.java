package uk.ac.ic.doc.neuralnets.tests.graph.neural;

import org.apache.log4j.Logger;
import uk.ac.ic.doc.neuralnets.graph.Node;
import uk.ac.ic.doc.neuralnets.graph.neural.manipulation.GraphFactory;
import uk.ac.ic.doc.neuralnets.graph.neural.NeuralNetwork;
import uk.ac.ic.doc.neuralnets.graph.neural.manipulation.InteractionUtils;
import uk.ac.ic.doc.neuralnets.util.Transformer;
import uk.ac.ic.doc.neuralnets.util.configuration.ConfigurationManager;

/**
 *
 * @author Peter Coetzee
 */
public class TestInteractionUtils {

    
    public static void main( String[] args ) {
        Logger log = Logger.getLogger( TestInteractionUtils.class );
        try {
            ConfigurationManager.configure();
            NeuralNetwork parent = new NeuralNetwork(); // 0
            NeuralNetwork n1 = GraphFactory.get().makeNetwork( 10, 0.2 ); // 1
            NeuralNetwork n2 = GraphFactory.get().makeNetwork( 10, 0.2 ); // 12
            NeuralNetwork n3 = GraphFactory.get().makeNetwork( 10, 0.2 ); // 23
            n2.addNode( n3 );
            parent.addNode( n1 );
            parent.addNode( n2 );
            log.debug( "Parent contains " + parent.getNodes().size() + " nodes." );
            InteractionUtils util = new InteractionUtils( parent );
            /*log.debug( util.isSuper( n2, n3 ) ? "n2 super n3" : "n2 not super n3" );
            log.debug( util.isSuper( n1, n3 ) ? "n1 super n3" : "n1 not super n3" );
            log.debug( util.isSuper( parent, n3 ) ? "parent super n3" : "parent not super n3" );
            log.debug( util.isSuper( (Node)parent, (Node)n2 ) ? "parent super n2" : "parent not super n2" );
            log.debug( util.lowestCommonAncestor( n3, n3 ) );*/
            log.debug( "\nInput of " + parent + "\n" );
            //util.connect( n1.getNodes(), n2.getNodes(), 0.2 );
            util.connect( n1.getNodes(), n3.getNodes(), 0.2 );
            //util.connect( n1.getNodes().iterator().next(), n3.getNodes().iterator().next() );
            log.debug( "\nBecame " + parent + "\n" );
            /*util.bifurcate( n3, new Transformer<Node, Boolean>() {
                public Boolean transform( Node in ) {
                    return in.getID() % 2 == 0;
                }
            } );
            log.debug( "\nBecame " + parent + "\n" );*/
            log.info( "Operation COMPLETED" );
        } catch ( Throwable t ) {
            log.error( null, t );
        }
    }
}
