package uk.ac.ic.doc.neuralnets.gui.modifier;

import uk.ac.ic.doc.neuralnets.gui.NetworkModifier;
import uk.ac.ic.doc.neuralnets.gui.commands.LayeredNetworkBuildingCommand;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;

import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphItem;
import uk.ac.ic.doc.neuralnets.coreui.ZoomingInterfaceManager;
import uk.ac.ic.doc.neuralnets.graph.neural.NodeSpecification;
import uk.ac.ic.doc.neuralnets.graph.neural.Perceptron;
import uk.ac.ic.doc.neuralnets.gui.NeuroneCombo;
import uk.ac.ic.doc.neuralnets.gui.ImageHandler;
import uk.ac.ic.doc.neuralnets.graph.neural.NeuroneTypes;

/**
 * Network modifier for creating classic feed-forward networks
 * 
 * @author Fred van den Driessche
 *
 */
public class LayeredNetworkModifier extends NetworkModifier {
	private static final Logger log = Logger
			.getLogger(LayeredNetworkModifier.class);

	private List<Layer> layers;

	private Composite container;
	private Composite controls;

	private ZoomingInterfaceManager<Graph,GraphItem> gm;
	private ExpandItem ei;

	public Composite getConfigurationGUI(Composite parent, ZoomingInterfaceManager<Graph,GraphItem> gm,
			ExpandItem ei) {
		this.gm = gm;
		this.ei = ei;
		layers = new LinkedList<Layer>();
		container = new Composite(parent, SWT.NONE);

		GridLayout layout = new GridLayout(1, true);
		container.setLayout(layout);

		addLayer();
		addControlButtons();

		return container;
	}

	private void addControlButtons() {
		controls = new Composite(container, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		controls.setLayout(layout);

		Button addLayer = new Button(controls, SWT.PUSH);
		addLayer.setText("Add Layer");
		addLayer.setImage(ImageHandler.get().getIcon("add"));
		
		GridData addData = new GridData(150, 27);
		addData.horizontalAlignment = SWT.RIGHT;
		addLayer.setLayoutData(addData);
		
		addLayer.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent arg0) {
			}

			public void widgetSelected(SelectionEvent arg0) {
				addLayer();
			}

		});

		Button insert = new Button(controls, SWT.PUSH);
		insert.setText("Insert");
		
		GridData insertData = new GridData();
		insertData.horizontalAlignment = SWT.RIGHT;
		insert.setLayoutData(insertData);
		
		insert.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent arg0) {
			}

			public void widgetSelected(SelectionEvent arg0) {
				runInsertThread();
			}

		});
	}

	protected void runInsertThread() {
        List<NodeSpecification> specs = new ArrayList<NodeSpecification>();
        List<Integer> quant = new ArrayList<Integer>();
        for ( Layer l : layers ) {
            specs.add( l.getSpec() );
            quant.add( l.getCount() );
        }
		gm.getCommandControl().addCommand( 
                new LayeredNetworkBuildingCommand( specs, quant, gm ) );
	}

	private void addLayer() {

		log.trace("Adding new layer");
		Layer layer = new Layer(container, SWT.NONE);
		layer.moveAbove(controls);

		layers.add(layer);
		log.trace(layers.size());
		ei.setHeight(container.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);

		// can't delete the first row.
		if (layers.size() < 2) {
			enableFirstDeleteButton(false);
		} else {
			enableFirstDeleteButton(true);
		}
	}

	private void enableFirstDeleteButton(boolean enabled) {
		layers.get(0).getDeleteButton().setEnabled(enabled);
	}

	public String getName() {
		return "LayeredNetwork";
	}

	public String toString() {
		return "Create Layered Network";
	}

	@Override
	public int getPriority() {
		return 5;
	}

	public class Layer extends Composite {

		private Label lbl;
		private NeuroneCombo type;
		private Spinner count;
		private Button del;
		
		private int ns;

		public Layer(Composite parent, int style) {
			super(parent, style);
			
			this.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

			GridLayout layout = new GridLayout(4, false);
			layout.marginWidth = layout.marginHeight = 0;
			layout.horizontalSpacing = layout.verticalSpacing = 2;

			this.setLayout(layout);

			lbl = new Label(this, SWT.NONE);
			lbl.setText(layers.size() + 1 + " ");
			lbl.setAlignment(SWT.RIGHT);
			
			lbl.setLayoutData(new GridData());

			type = makeNeuroneSelector(this);

			count = new Spinner(this, SWT.BORDER);
			// default, min, max, decimal digits, increment, page increment.
			count.setValues(5, 1, 100, 0, 1, 5);
			count.setLayoutData(new GridData(32, 18));

			count.addSelectionListener(new SelectionListener(){

				public void widgetDefaultSelected(SelectionEvent arg0) {}

				public void widgetSelected(SelectionEvent arg0) {
					ns = count.getSelection();
				}
				
			});
			ns = count.getSelection();
			
			del = new Button(this, SWT.PUSH);
			del.setImage(ImageHandler.get().getIcon("delete"));
			del.setToolTipText("Remove layer");
			del.setLayoutData(new GridData(24, 23));
			del.addSelectionListener(new SelectionListener() {

				public void widgetDefaultSelected(SelectionEvent arg0) {
				}

				public void widgetSelected(SelectionEvent se) {
					log.trace("Deleting layer");
					Composite layer = ((Button) se.widget).getParent();
					for (int i = layers.indexOf(layer); i < layers.size(); i++) {
						Label l = layers.get(i).getLabel();
						int newNumber = Integer.parseInt(l.getText().trim()) - 1;
						l.setText(newNumber + " ");
					}
					layers.remove(layer);
					log.trace(layers.size());
					layer.setVisible(false);
					layer.dispose();
					ei.setHeight(container
							.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
					if (layers.size() == 1) {
						enableFirstDeleteButton(false);
					}
				}

			});
		}

		public NodeSpecification<?> getSpec() {
			return type.getSpecification();
		}

		public int getCount() {
			return ns;
		}

		protected Label getLabel() {
			return lbl;
		}

		public Button getDeleteButton() {
			return del;
		}

		private NeuroneCombo makeNeuroneSelector(Composite layer) {
			NeuroneCombo neuroneSelector = new NeuroneCombo(layer, Perceptron.class);

			GridData nsData = new GridData(101, 26);
			nsData.horizontalAlignment = GridData.FILL;
			nsData.grabExcessHorizontalSpace = true;
			neuroneSelector.setLayoutData(nsData);

			return neuroneSelector;
		}
	}
}
