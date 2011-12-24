/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ic.doc.neuralnets.graph.neural.manipulation;

import uk.ac.ic.doc.neuralnets.graph.neural.NodeSpecification;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;
import uk.ac.ic.doc.neuralnets.graph.Node;
import uk.ac.ic.doc.neuralnets.graph.neural.NeuralNetwork;
import uk.ac.ic.doc.neuralnets.graph.neural.manipulation.EdgeCreatedEvent;
import uk.ac.ic.doc.neuralnets.events.EventManager;
import uk.ac.ic.doc.neuralnets.graph.Edge;
import uk.ac.ic.doc.neuralnets.util.Transformer;

/**
 *
 * @author Peter Coetzee
 */
public class HomogenousNetworkSpecification extends GraphSpecification<NeuralNetwork> {
    
    public HomogenousNetworkSpecification( NodeSpecification spec, Integer nodes ) {
        this( spec, nodes, 1 );
    }
    
    public HomogenousNetworkSpecification( Integer nodes, double edgeProb ) {
        this( null, Collections.singletonList( nodes ), edgeProb );
    }
    
    public HomogenousNetworkSpecification( NodeSpecification spec, double edgeProb ) {
        this( Collections.singletonList( spec ), null, edgeProb );
    }
    
    public HomogenousNetworkSpecification( NodeSpecification spec, Integer nodes, double edgeProb ) {
        this( Collections.singletonList( spec ), Collections.singletonList( nodes ), edgeProb );
    }
    
    public HomogenousNetworkSpecification( List<NodeSpecification> specs, List<Integer> nodes ) {
        this( specs, nodes, 1 );
    }
    
    public HomogenousNetworkSpecification( List<Integer> nodes, double edgeProb ) {
        this( null, nodes, edgeProb );
    }
    
    public HomogenousNetworkSpecification( List<NodeSpecification> specs, 
            List<Integer> nodes, final double edgeProb ) {
        super( specs, nodes, new Transformer<NeuralNetwork,NeuralNetwork>() {
            
            @SuppressWarnings( "unchecked" )
            public NeuralNetwork transform( final NeuralNetwork g ) {
                int approxNumNodes = (int)Math.ceil( 
                        edgeProb * g.getNodes().size() * g.getNodes().size() );
                int num = 0;
                int freq = approxNumNodes / GraphFactory.EVENT_RESOLUTION;
                freq = freq < 1 ? 1 : freq;
                EdgeFactory ef = EdgeFactory.get();
                for ( Node<?> n1 : g.getNodes() ) {
                    for ( Node<?> n2 : g.getNodes() ) {
                        if ( Math.random() <= edgeProb ) {
                            g.addEdge( ef.create( n1, n2 ) );
                            num++;
                            if ( num % freq == 0 )
                                EventManager.get().fire( 
                                        new EdgeCreatedEvent( num, approxNumNodes ) );
                        }
                    }
                }
                return g;
            }
            
        } );
    }
    
    @Override
    public Class<NeuralNetwork> getTarget() {
        return NeuralNetwork.class;
    }

    @Override
    public boolean separateNetworks() {
        return false;
    }

}
