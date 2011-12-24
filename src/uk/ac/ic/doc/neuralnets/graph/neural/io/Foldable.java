
package uk.ac.ic.doc.neuralnets.graph.neural.io;

/**
 * Denotes that an InputNode can be used for N-Fold training.
 *
 * @author Peter Coetzee
 */
public interface Foldable {
    
    /**
     * Instruct this foldable to prepare for the next fold
     * @param foldNumber The number of the current fold to prepare
     * @param folds The number of folds total
     */
    public void fold( int foldNumber, int folds );

}
