
package uk.ac.ic.doc.neuralnets.matrix;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Matrix class that almost supports dynamic resizing
 * May not be needed for our use cases, so didn't invest any more effort
 * Resizing half-works (specify no-bound with width or height == 0), can
 * put effort in if it's needed
 * Wherever possible, instead of returning void from a public method,
 * returns itself instead to permit chaining of calls
 * @author Peter Coetzee
 */
public class Matrix<T> implements Serializable {
    
    protected List<List<T>> matrix;
    protected int width, height;

    public Matrix( int width, int height ) {
        matrix = new ArrayList<List<T>>();
        for ( int i = 0; i < width; i++ )
            matrix.add( new ArrayList<T>() );
        this.width = width;
        this.height = height;
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public synchronized Matrix<T> set( T item, int x, int y ) {
        bounds( x, y );
        matrix.get( x ).add( y, item );
        return this;
    }
    
    public synchronized Matrix<T> add( T item, int x ) {
        boundsX( x );
        boundsY( matrix.get( x ).size() );
        matrix.get( x ).add( item );
        return this;
    }
    
    public synchronized Matrix<T> add( T item ) {
        int i = 0;
        if ( height == 0 ) {
            while ( true ) {
                try {
                    add( item, i );
                    break;
                } catch ( ArrayIndexOutOfBoundsException e ) { /* swallow */ }
                i++;
            }
            return this;
        }
        for ( i = 0; i < height; i++ ) {
            try {
                add( item, i );
                break;
            } catch ( ArrayIndexOutOfBoundsException e ) { /* swallow */ }
        }
        if ( i > height )
            throw new ArrayIndexOutOfBoundsException( "No more space in matrix!" );
        return this;
    }
    
    public synchronized T get( int x, int y ) {
        bounds( x, y );
        return matrix.get( x ).get( y );
    }
    
    public synchronized Matrix<T> forEach( Command<T> c ) {
        Iterator<List<T>> rit = matrix.iterator();
        int i = 0, j = 0;
        while( rit.hasNext() ) {
            Iterator<T> cit = rit.next().iterator();
            while( cit.hasNext() ) {
                c.exec( i, j, cit.next() );
                j++;
            }
            i++;
        }
        return this;
    }
    
    protected final void bounds( int x, int y ) {
        if ( width > 0 && width <= x || height > 0 && height <= y )
            throw new ArrayIndexOutOfBoundsException( 
                    "Invalid matrix co-ordinates: (" + x + ", " + y 
                    + ") for matrix of size (" + width + ", " 
                    + height + ")" );
    }
    
    protected final void boundsX( int x ) {
        if (  width > 0 && width <= x )
            throw new ArrayIndexOutOfBoundsException( 
                    "Invalid x co-ordinate: " + x + ") for matrix of size " 
                    + width );
        while ( matrix.size() <= x )
            matrix.add( new ArrayList<T>() );
    }
    
    protected final void boundsY( int y ) {
        if ( height > 0 && height <= y )
            throw new ArrayIndexOutOfBoundsException( 
                    "Invalid y co-ordinate: " + y + ") for matrix of size " 
                    + height );
    }
    
    @Override
    public synchronized String toString() {
        StringBuilder out = new StringBuilder();
        out.append( "Matrix (" + width + ", " + height + "):\n" );
        for ( List<T> row : matrix)
            out.append( "\t" + row + "\n" );
        return out.toString();
    }
    
    public interface Command<T> {
        public void exec( int x, int y, T item );
    }
    
}
