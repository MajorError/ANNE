
package uk.ac.ic.doc.neuralnets.graph.neural.io;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Peter Coetzee
 */
public class ValueReportingOutputNode extends OutputNode {
    
    private List<Double> values;
    
    @Override
    protected void setNodes( int n ) {
        values = new ArrayList<Double>( n );
        for ( int i = 0; i < n; i++ )
            values.add( 0d );
    }

    @Override
    protected void fire( int n, Double amt ) {
        values.set( n, amt );
    }
    
    public List<Double> getValues() {
        return values;
    }

    public String getName() {
        return "ValueReporting";
    }
    
    public void destroy() {
        // no-op
    }
    
    public void recreate() {
        // no-op
    }

}
