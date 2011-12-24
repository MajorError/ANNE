package uk.ac.ic.doc.neuralnets.gui.connector;

import java.util.Collection;
import java.util.Collections;
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
public class SimpleRandomConnector extends NetworkConnector implements
		SelectionListener {

	private Scale prob;
	private Label probLabel;
	private double probPercent = 100;

	@Override
	@SuppressWarnings("unchecked")
	public Collection<Edge<Node<?>, Node<?>>> connect(List<Node<?>> nodes) {
		Collection<Edge<Node<?>, Node<?>>> out = new HashSet<Edge<Node<?>, Node<?>>>();
		for (Node<?> n1 : nodes) {
			for (Node<?> n2 : nodes) {
				Collection<Node> c1, c2;
				if (n1 instanceof NeuralNetwork)
					c1 = (Collection) ((NeuralNetwork) n1).getNodes();
				else
					c1 = Collections.singleton((Node) n1);
				if (n2 instanceof NeuralNetwork)
					c2 = (Collection) ((NeuralNetwork) n2).getNodes();
				else
					c2 = Collections.singleton((Node) n2);
				out.addAll((Collection) gm.getUtils().connect(c1, c2,
						probPercent / 100));
			}
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
		return "SimpleRandom";
	}

	public void widgetSelected(SelectionEvent e) {
		probLabel.setText(String.valueOf(prob.getSelection()) + "%");
		probPercent = prob.getSelection();
	}

	public void widgetDefaultSelected(SelectionEvent e) {
		widgetSelected(e);
	}

}
