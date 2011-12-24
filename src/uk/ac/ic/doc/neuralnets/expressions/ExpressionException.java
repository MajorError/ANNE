
package uk.ac.ic.doc.neuralnets.expressions;

/**
 *
 * @author Peter Coetzee
 */
public class ExpressionException extends Exception {

    public ExpressionException( Exception e ) {
        super( e );
    }
    
    public ExpressionException( String msg ) {
        super( msg );
    }

}
