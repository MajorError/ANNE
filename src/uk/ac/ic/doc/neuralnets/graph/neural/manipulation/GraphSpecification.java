package uk.ac.ic.doc.neuralnets.graph.neural.manipulation;

import uk.ac.ic.doc.neuralnets.graph.neural.NodeSpecification;
import java.util.ArrayList;
import java.util.List;
import uk.ac.ic.doc.neuralnets.graph.Graph;
import uk.ac.ic.doc.neuralnets.util.Transformer;

/**
 * Encodes the details of the Graph to be created
 * @author Peter Coetzee
 */
public abstract class GraphSpecification<T extends Graph> {
    
    private List<Integer> nodes;
    private Transformer<T,T> edgeBuilder;
    private List<NodeSpecification> specs;

    /**
     * Create a default, empty graph.
     */
    public GraphSpecification() {
        nodes = new ArrayList<Integer>();
        edgeBuilder = new Transformer<T, T>() {
            public T transform( T input ) {
                return input;
            }
        };
        specs = new ArrayList<NodeSpecification>();
    }
    
    /**
     * Create a graph of the default node type, in the supplied quantity
     * @param nodes The number of nodes to creaet
     */
    public GraphSpecification( List<Integer> nodes ) {
        this( null, nodes, null );
    }
    
    /**
     * Create a default empty graph, with the supplied edge builder
     * @param builder The edge builder to use to transform the graph
     */
    public GraphSpecification( Transformer<T,T> builder ) {
        this( null, null, builder );
    }
    
    /**
     * Create a graph with the given node types and quantities, and use
     * the supplied transformer to build edges
     * @param s The list of node types (indices map to ns)
     * @param ns The list of quantities of node (indices map to s)
     * @param builder The edge building transformer
     */
    public GraphSpecification( List<NodeSpecification> s, List<Integer> ns, 
            Transformer<T,T> builder ) {
        nodes = ns == null ? new ArrayList<Integer>() : ns;
        edgeBuilder = builder == null ? 
            new Transformer<T,T>() {
                public T transform( T in ) {
                    return in;
                }
            } : builder;
        specs = s == null ? new ArrayList<NodeSpecification>() : s;
        if ( specs.size() == 0 && ns.size() > 0 )
            for ( Integer i : ns )
                specs.add( new SpikingNodeSpecification() );
        else if ( nodes.size() == 0 )
            for ( NodeSpecification n : specs)
                nodes.add( 0 );
    }
    
    /**
     * Answer the quantities of nodes in this specification
     * @return The list of integer values. Modifications to this list are
     * retained in the specification
     */
    public List<Integer> getNodes() {
        return nodes;
    }
    
    /**
     * Get the edge building transformer for this specification
     * @return A transformer used to build edges
     */
    public Transformer<T,T> getEdgeBuilder() {
        return edgeBuilder;
    }
    
    /**
     * Return the list of node types in this specification
     * @return The list of node types. Modifications to this list are
     * retained in the specification
     */
    public List<NodeSpecification> getSpecifications() {
        return specs;
    }
    
    /**
     * Answers whether or not the node types in this specification should be
     * separated into their own sub-networks
     * @return True iff nodes are to be separated
     */
    public abstract boolean separateNetworks();
    
    /**
     * Stores the type of graph to create
     * @return The Class of the Graph encoded by this specification
     */
    public abstract Class<T> getTarget();

}
