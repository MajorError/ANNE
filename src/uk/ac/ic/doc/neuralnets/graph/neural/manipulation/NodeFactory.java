package uk.ac.ic.doc.neuralnets.graph.neural.manipulation;

import uk.ac.ic.doc.neuralnets.graph.neural.NodeSpecification;
import java.io.Serializable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.log4j.Logger;
import uk.ac.ic.doc.neuralnets.expressions.ast.ASTExpression;
import uk.ac.ic.doc.neuralnets.graph.Node;
import uk.ac.ic.doc.neuralnets.graph.neural.Neurone;

/**
 * NodeFactory creates Node objects from NodeSpecifications.
 * 
 * @see Node
 * @see NodeSpecification
 * 
 * @author Peter Coetzee
 *
 */
public class NodeFactory implements Serializable {

    private static final long serialVersionUID = 4842534494115202458L;
    private static final Logger log = Logger.getLogger( NodeFactory.class );
    private static final NodeFactory n = new NodeFactory();

    /**
     * Get the factory instance.
     * @return the NodeFactory
     */
    public static NodeFactory get() {
        return n;
    }
    
    /**
     * Create a default neurone
     * @return a neurone with default spiking neurone parameters.
     */
    public Neurone create() {
        return create( new SpikingNodeSpecification() );
    }

    /**
     * 
     * @param <T> the type of the Node to be created
     * @param s the specification of the node
     * @return node with parameters conforming to the specification.
     */
    public <T extends Node> T create( NodeSpecification<T> s ) {
        try {
            T out = s.getTarget().newInstance();
            for ( String p : s.getParameters() ) {
                Method m = getSetter( out, p );
                m.invoke( out, s.get( p ) );
            }
            if ( out instanceof Neurone )
                ((Neurone)out).setEdgeDecoration( s.getEdgeDecoration() );
            return out;
        } catch ( IllegalAccessException ex ) {
            log.error( null, ex );
        } catch ( IllegalArgumentException ex ) {
            log.error( null, ex );
        } catch ( InvocationTargetException ex ) {
            log.error( null, ex );
        } catch ( InstantiationException ex ) {
            log.error( null, ex );
        }
        return null;
    }
    
    private Method getSetter( Object o, String param ) {
        try {
            param = "set" + param.replaceAll( " ", "" );
            return o.getClass().getMethod( param, ASTExpression.class );
        } catch ( NoSuchMethodException ex ) {
            log.error( null, ex );
        } catch ( SecurityException ex ) {
            log.error( null, ex );
        }
        return null;
    }
}
