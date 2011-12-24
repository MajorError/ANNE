
package uk.ac.ic.doc.neuralnets.matrix;

import java.util.List;

/**
 *
 * @author Peter Coetzee
 */
public class PartitionableMatrix<T> extends Matrix<T> {
    
    private int pX1 = -1, pY1 = -1, pX2 = -1, pY2 = -1;
    
    public PartitionableMatrix( int width, int height ) {
        super( width, height );
    }
    
    public synchronized PartitionableMatrix<T> partition( int x1, int y1, int x2, int y2 ) {
        pX1 = x1;
        pY1 = y1;
        pX2 = x2;
        pY2 = y2;
        return this;
    }
    
    public synchronized PartitionableMatrix<T> clearPartition() {
        partition( -1, -1, -1, -1 );
        return this;
    }
    
    public synchronized T getPartitioned( int x, int y ) {
        x += (pX1 > -1 ? pX1 : 0);
        y += (pY1 > -1 ? pY1 : 0);
        partitionBounds( x, y );
        return get( x, y );
    }
    
    public synchronized PartitionableMatrix<T> getPartitionedMatrix() {
        if ( pX2 == -1 || pY2 == -1 )
            return this;
        PartitionableMatrix<T> m = newMatrix( pX2 - pX1, pY2 - pY1 );
        for ( int i = pX1; i < pX2; i++ ) {
            List<T> row = matrix.get( i );
            for ( int j = pY1; j < pY2; j++ )
                m.set( row.get( j ), i - pX1, j - pY1 );
        }
        return m;
    }
    
    public synchronized PartitionableMatrix<T> forEachPartitioned( Command<T> c ) {
        for ( int i = pX1; i < pX2; i++ ) {
            List<T> row = matrix.get( i );
            for ( int j = pY1; j < pY2; j++ ) {
                c.exec( i - pX1, j - pY1, row.get( j ) );
            }
        }
        return this;
    }
    
    protected PartitionableMatrix<T> newMatrix( int w, int h ) {
        return new PartitionableMatrix<T>( w, h );
    }
    
    private final void partitionBounds( int x, int y ) {
        if ( pX2 > -1 && x >= pX2 || pY2 > -1 && y >= pY2 )
            throw new ArrayIndexOutOfBoundsException( 
                    "Invalid partition co-ordinates: (" + x + ", " + y 
                    + ") for matrix of size (" + pX2 + ", " + pY2 + ")" );
    }
    
}
