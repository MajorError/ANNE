package uk.ac.ic.doc.neuralnets.gui.connector;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import uk.ac.ic.doc.neuralnets.graph.Edge;
import uk.ac.ic.doc.neuralnets.graph.Node;
import uk.ac.ic.doc.neuralnets.graph.neural.NeuralNetwork;

/**
 * 
 * @author Peter Coetzee
 */
public class LayeredNetworkConnector extends NetworkConnector implements
		SelectionListener {

	private Scale prob = null;
	private Label probLabel;
	private double probPercent = 100;

	@Override
	public Collection<Edge<Node<?>, Node<?>>> connect(List<Node<?>> nodes) {
		NeuralNetwork last = null;
		Collection<Edge<Node<?>, Node<?>>> out = new HashSet<Edge<Node<?>, Node<?>>>();
		for (Node<?> n : nodes) {
			if (!(n instanceof NeuralNetwork))
				continue; // ignore Neurones
			if (last != null)
				out.addAll(gm.getUtils().connect(last.getNodes(),
						((NeuralNetwork) n).getNodes(), probPercent / 100));
			last = (NeuralNetwork) n;
		}
		return out;
	}

	@Override
	public Composite getConfigurationPanel(Composite parent) {
		Composite out = new Composite(parent, SWT.NONE);
		out.setLayout(new FillLayout(SWT.VERTICAL));
		Composite lbl = new Composite(out, SWT.NONE);
		lbl.setLayout(new GridLayout(2, false));

		new Label(lbl, SWT.NONE).setText("Edge Creation Probability:");
		probLabel = new Label(lbl, SWT.NONE);
		probLabel.setAlignment(SWT.RIGHT);
		probLabel.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
				| GridData.FILL_HORIZONTAL));

		prob = new Scale(out, SWT.HORIZONTAL);
		prob.setMinimum(0);
		prob.setMaximum(100);
		prob.setIncrement(5);
		prob.setSelection(50);
		prob.addSelectionListener(this);
		widgetSelected(null);
		return out;
	}

	public String getName() {
		return "LayeredNetwork";
	}

	public void widgetSelected(SelectionEvent e) {
		probLabel.setText(String.valueOf(prob.getSelection()) + "%");
		probPercent = prob.getSelection();
	}

	public void widgetDefaultSelected(SelectionEvent e) {
		widgetSelected(e);
	}

}
