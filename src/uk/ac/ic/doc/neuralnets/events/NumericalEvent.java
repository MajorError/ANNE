package uk.ac.ic.doc.neuralnets.events;

/**
 *
 * @author Peter Coetzee
 */
public abstract class NumericalEvent extends Event{
    
    public abstract void push( NumericalStatistician s );
    
    public abstract double get( int idx );
    
    public abstract double numPoints();

}
