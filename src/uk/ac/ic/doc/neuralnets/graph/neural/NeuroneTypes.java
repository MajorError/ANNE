
package uk.ac.ic.doc.neuralnets.graph.neural;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import uk.ac.ic.doc.neuralnets.expressions.ExpressionException;
import uk.ac.ic.doc.neuralnets.expressions.ast.ASTExpressionFactory;
import uk.ac.ic.doc.neuralnets.graph.neural.NodeSpecification;
import uk.ac.ic.doc.neuralnets.graph.neural.Neurone;
import uk.ac.ic.doc.neuralnets.graph.neural.EdgeDecoration;

/**
 * Container object for the Neurone Types created by NeuroneTypeConfig
 * @author Peter Coetzee
 */
public class NeuroneTypes {
    
    private static final Logger log = Logger.getLogger( NeuroneTypes.class );
    
    /** Magic keyword for edge decoration */
    public static final String EDGE_DECORATION_NAME = "Edge Decoration";
    
    /** Map from node type name to class */
    public static final Map<String,Class<? extends Neurone>> nodeTypes 
            = new HashMap<String,Class<? extends Neurone>>();
    
    /** Map from type name to edge decoration */
    public static final Map<String,EdgeDecoration> nodeDecorations
            = new HashMap<String,EdgeDecoration>();
    
    /** Map from type name to list of the parameters */
    public static final Map<String,List<String>> nodeParams 
            = new HashMap<String,List<String>>();
    
    /** Map from type name to list of the default parameter values */
    public static final Map<String,List<String>> paramValues
            = new HashMap<String,List<String>>();
    
    /**
     * Build a NodeSpecification for the specified Neurone type
     * @param <T> The Class of the node type
     * @param name The name of the Neurone (assumed to exist in nodeTypes)
     * @return The NodeSpecification for the given Neurone type
     */
    public static <T extends Neurone> NodeSpecification<T> specFor( String name ) {
        @SuppressWarnings( "unchecked" )
        NodeSpecification<T> n = new NodeSpecification<T>( (Class<T>)nodeTypes.get( name ) );
        n.setName( name );
        Iterator<String> params = nodeParams.get( name ).iterator();
        for ( String val : paramValues.get( name ) ) {
            try {
                n.set( params.next(), ASTExpressionFactory.get().getExpression( val ) );
            } catch ( ExpressionException ex ) {
                log.error( "Couldn't parse expression!", ex );
            }
        }
        n.setEdgeDecoration( nodeDecorations.get( name ) );
        return n;
    }

}
