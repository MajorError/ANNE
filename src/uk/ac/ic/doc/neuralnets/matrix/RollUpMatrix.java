
package uk.ac.ic.doc.neuralnets.matrix;

/**
 *
 * @author Peter Coetzee
 */
public class RollUpMatrix<T> extends PartitionableMatrix<T> {
    
    public RollUpMatrix( int width, int height ) {
        super( width, height );
    }
    
    public synchronized RollUpMatrix<RollUpMatrix<T>> rollUp( int width, int height ) {
        RollUpMatrix<RollUpMatrix<T>> m = new RollUpMatrix<RollUpMatrix<T>>( width, height );
        int x = this.width > 0 ? this.width : matrix.size();
        int y = this.height > 0 ? this.height : matrix.get( 0 ).size();
        x = (int)Math.ceil( (double)x / (double)width ); // max no items on x axis = oldWidth / newWidth
        y = (int)Math.ceil( (double)y / (double)height ); // max on y, as above
        for ( int i = 0; i < width; i++ ) {
            for ( int j = 0; j < height; j++ ) {
                int w = (i * x + x) > this.width ? this.width : (i * x + x);
                int h = (j * y + y) > this.height ? this.height : (j * y + y);
                partition( i * x, j * y, w, h );
                m.set( (RollUpMatrix<T>)getPartitionedMatrix(), i, j );
            }
        }
        return m;
    }
    
    @Override
    protected PartitionableMatrix<T> newMatrix( int w, int h ) {
        return new RollUpMatrix<T>( w, h );
    }

}
