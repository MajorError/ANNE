/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ic.doc.neuralnets.tests.util.plugins;

/**
 *
 * @author Peter Coetzee
 */
public class MethodInvocationTest {
    
    public static void main( String[] args ) {
        new MethodInvocationTest();
    }
    
    public MethodInvocationTest() {
        X x = new X();
        Y y = new Y();
        
        invoke( x ); // calls invoke( X x )
        invoke( y ); // calls invoke( Y y )
        invoke( (X)y ); // calls invoke( Y y )
        
        X z = y;
        invoke( z ); // calls invoke( Y y )
        
        invoke( X.class.cast( z ) ); // calls invoke( Y y )
        invoke( X.class.cast( y ) ); // calls invoke( Y y )
    }

    private void invoke( X x ) {
        System.out.println( "Invoked over " + x.getClass().getSimpleName() );
    }
    
    private void invoke( Y y ) {
        System.out.println( "Invoked over " + y.getClass().getSimpleName() );
    }
    
    
    private class X { }
    
    private class Y extends X { }

}
