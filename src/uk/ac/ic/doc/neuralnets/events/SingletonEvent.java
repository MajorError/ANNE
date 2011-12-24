package uk.ac.ic.doc.neuralnets.events;

/**
 *
 * @author Peter Coetzee
 */
public abstract class SingletonEvent extends Event {
    
    @Override
    public abstract boolean equals( Object o );

}
