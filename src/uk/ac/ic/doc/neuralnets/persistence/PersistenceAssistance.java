package uk.ac.ic.doc.neuralnets.persistence;

import uk.ac.ic.doc.neuralnets.graph.Saveable;

/**
 *
 * @author Peter Coetzee
 */
public abstract class PersistenceAssistance {
    
    public abstract void setSaveTarget( Saveable o );
    
    public abstract void setObjectsToLoad( int objects );
    
    public abstract void done( int num );
    
    public abstract void fixIDs( Saveable o );
    
    public abstract String inferLoadPlugin( String filename );
    
    public abstract String inferSavePlugin( String filename );
    
    public static PersistenceAssistance newInstance() {
        return new NeuralNetworkAssistance();
    }

}
