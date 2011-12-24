package uk.ac.ic.doc.neuralnets.gui.commands;

import org.eclipse.swt.widgets.Display;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphItem;

import uk.ac.ic.doc.neuralnets.commands.Command;
import uk.ac.ic.doc.neuralnets.coreui.InterfaceManager;
import uk.ac.ic.doc.neuralnets.events.EventManager;
import uk.ac.ic.doc.neuralnets.events.GraphUpdateEvent;
import uk.ac.ic.doc.neuralnets.graph.neural.NeuralNetwork;
import uk.ac.ic.doc.neuralnets.graph.neural.Neurone;

public class AddNeuroneCommand extends Command {
	
	private Neurone n;
	private NeuralNetwork nn;
	private InterfaceManager<Graph, GraphItem> m;
	
	public AddNeuroneCommand(Neurone n, InterfaceManager<Graph, GraphItem> m) {
		super();
		this.n = n;
		this.nn = m.getCurrentNetwork();
		this.m = m;
	}

	@Override
	protected void execute() {
		nn.addNode(n);
		Display.getDefault().asyncExec(new Runnable(){
			public void run() {
				m.redrawCurrentView();
				EventManager.get().fire( new GraphUpdateEvent() );
			}
		});
	}

	@Override
	protected void undo() {
		nn.getNodes().remove(n);
		Display.getDefault().asyncExec(new Runnable(){
			public void run() {
				m.redrawCurrentView();
				EventManager.get().fire( new GraphUpdateEvent() );
			}
		});
	}

}
