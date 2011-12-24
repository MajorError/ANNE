package uk.ac.ic.doc.neuralnets.gui;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphItem;

import uk.ac.ic.doc.neuralnets.coreui.ZoomingInterfaceManager;
import uk.ac.ic.doc.neuralnets.graph.Node;
import uk.ac.ic.doc.neuralnets.graph.neural.Neurone;
import uk.ac.ic.doc.neuralnets.graph.neural.NodeSpecification;
import uk.ac.ic.doc.neuralnets.graph.neural.manipulation.NodeFactory;
import uk.ac.ic.doc.neuralnets.gui.commands.AddNeuroneCommand;
import uk.ac.ic.doc.neuralnets.gui.commands.ConnectNodesCommand;
import uk.ac.ic.doc.neuralnets.gui.connector.OneToManyConnector;
import uk.ac.ic.doc.neuralnets.gui.graph.GUINode;
import uk.ac.ic.doc.neuralnets.gui.graph.listener.MouseItemListener;

public class InsertToolbar extends ToolbarPlugin {
	private static final Logger log = Logger.getLogger(InsertToolbar.class);

	private ToolItem neurone;
	private ToolItem edge;

	private NeuroneCombo types;

	@Override
	public void create(final GUIToolbar toolbar) {

		CoolItem c = toolbar.addGroup("Insert");

		ToolItem text = toolbar.addButton("Insert", "", SWT.SEPARATOR);

		ToolItem sep = toolbar.addButton("Insert", "", SWT.SEPARATOR);

		neurone = toolbar.addButton("Insert", " N ", SWT.CHECK | SWT.BORDER);
		edge = toolbar.addButton("Insert", " E ", SWT.CHECK | SWT.BORDER);

		Label lbl = new Label((ToolBar) c.getControl(), SWT.NONE);
		lbl.setText("Types:");
		lbl.pack();
		text.setWidth(lbl.getSize().x);
		text.setControl(lbl);

		types = new NeuroneCombo((ToolBar) c.getControl(), Neurone.class);

		types.getCombo().pack();
		sep.setWidth(types.getCombo().getSize().x);
		sep.setControl(types.getCombo());

		toolbar.repackGroup("Insert");

		final NeuroneInserter ni = new NeuroneInserter(toolbar.getManager());
		final EdgeInserter ei = new EdgeInserter(toolbar.getManager());

		neurone.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent arg0) {
			}

			public void widgetSelected(SelectionEvent se) {
				if (neurone.getSelection() && edge.getSelection()) {
					edge.setSelection(false);
					disableEdgeListener(toolbar, ei);
				}

				if (neurone.getSelection()) {
					enableNeuroneListener(toolbar, ni);
				} else {
					disableNeuroneListener(toolbar, ni);
				}
			}

		});

		edge.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent arg0) {
			}

			public void widgetSelected(SelectionEvent arg0) {
				if (edge.getSelection() && neurone.getSelection()) {
					neurone.setSelection(false);
					disableNeuroneListener(toolbar, ni);
				}

				if (edge.getSelection()) {
					enableEdgeListener(toolbar, ei);
				} else {
					disableEdgeListener(toolbar, ei);
				}
			}

		});
	}

	@Override
	public int getPriority() {
		return 5;
	}

	public String getName() {
		return "InsertToolbar";
	}

	private void enableEdgeListener(GUIToolbar toolbar, EdgeInserter ei) {
		log.trace("Edge inserter enabled");
		toolbar.getManager().getGraph().addMouseListener(ei);
	}

	private void disableEdgeListener(GUIToolbar toolbar, EdgeInserter ei) {
		log.trace("Edge inserter disabled");
		toolbar.getManager().getGraph().removeMouseListener(ei);
		ei.clear();
	}

	private void enableNeuroneListener(GUIToolbar toolbar, NeuroneInserter ni) {
		log.trace("Neurone inserter enabled");
		toolbar.getManager().getGraph().addMouseListener(ni);
	}

	private void disableNeuroneListener(GUIToolbar toolbar, NeuroneInserter ni) {
		log.trace("Neurone inserter disabled");
		toolbar.getManager().getGraph().removeMouseListener(ni);
	}

	private class NeuroneInserter extends MouseItemListener {

		private ZoomingInterfaceManager<Graph, GraphItem> manager;

		public NeuroneInserter(ZoomingInterfaceManager<Graph, GraphItem> manager) {
			super(manager.getGraph());
			this.manager = manager;
		}

		@Override
		protected void handleClick(MouseEvent e, GraphItem i) {
			super.handleClick(e, i);
			if (i != null)
				return;

			NodeSpecification<Neurone> s = types.getSpecification();
			log.trace("Insert neurone of type " + s.getName());
			Neurone n = NodeFactory.get().create(s);
			n.setPos(e.x, e.y, 0);
			manager.getCommandControl().addCommand(
					new AddNeuroneCommand(n, manager));
		}
	}

	private class EdgeInserter extends MouseItemListener {

		private ZoomingInterfaceManager<Graph, GraphItem> manager;
		private Node<?> src;
		private Node<?> dst;

		public EdgeInserter(ZoomingInterfaceManager<Graph, GraphItem> manager) {
			super(manager.getGraph());
			this.manager = manager;
		}

		@Override
		protected void handleClick(MouseEvent e, GraphItem i) {
			super.handleClick(e, i);
			if (i instanceof GUINode) {
				Node<?> n = ((GUINode) i).getNode();
				if (src == null) {
					log.trace("Setting edge source " + n);
					src = n;
				} else {
					log.trace("Setting edge destination " + n);
					dst = n;
					connect();
				}
			}
		}

		public void clear() {
			src = dst = null;
		}

		private void connect() {
			log.trace("Creating edge between " + src + " and " + dst);
			manager.getCommandControl().addCommand(
					new ConnectNodesCommand(new OneToManyConnector(manager),
							new ArrayList<Node<?>>(Arrays.asList(src, dst)),
							manager));
			clear();
		}
	}
}
