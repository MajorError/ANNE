package uk.ac.ic.doc.neuralnets.graph.neural;

import uk.ac.ic.doc.neuralnets.events.*;

public class NeuralNetworkTickEvent extends Event {

	private int ticks;

	public NeuralNetworkTickEvent(int ticks) {
		this.ticks = ticks;
	}

	@Override
	public String toString() {
		return "NeuralNetworkTickEvent";
	}

}
