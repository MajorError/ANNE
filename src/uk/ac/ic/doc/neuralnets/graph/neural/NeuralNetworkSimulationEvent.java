package uk.ac.ic.doc.neuralnets.graph.neural;

import uk.ac.ic.doc.neuralnets.events.*;

/**
 *
 * @author Peter Coetzee
 */
public class NeuralNetworkSimulationEvent extends RevalidateStatisticiansEvent {
    
    private boolean started = false;
    
    public NeuralNetworkSimulationEvent() {
        // no-op
    }

    public NeuralNetworkSimulationEvent( boolean b ) {
        started = b;
    }
    
    public boolean started() {
        return started;
    }

    @Override
    public String toString() {
        return "Neural Network Simulation Event";
    }

}
