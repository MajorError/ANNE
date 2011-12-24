package uk.ac.ic.doc.neuralnets.gui.connector;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphItem;

import uk.ac.ic.doc.neuralnets.coreui.ZoomingInterfaceManager;
import uk.ac.ic.doc.neuralnets.graph.Edge;
import uk.ac.ic.doc.neuralnets.graph.Node;

public class OneToManyConnector extends NetworkConnector {

	public OneToManyConnector(ZoomingInterfaceManager<Graph, GraphItem> gm) {
		super(gm);
	}

	@Override
	public Collection<Edge<Node<?>, Node<?>>> connect(List<Node<?>> nodes) {
		Collection<Edge<Node<?>, Node<?>>> out = new HashSet<Edge<Node<?>, Node<?>>>();

		Collection<Edge<?, ?>> temps = new HashSet<Edge<?, ?>>();
		Node<?> target = nodes.remove(0);
		for (Node<?> node : nodes) {

			Edge<?, ?> edge = gm.getUtils().connect(target, node);
			temps.add(edge);

		}

		out.addAll((Collection) temps);

		return out;
	}

	@Override
	public Composite getConfigurationPanel(Composite parent) {
		return null;
	}

	public String getName() {
		return "OneToMany";
	}

}
