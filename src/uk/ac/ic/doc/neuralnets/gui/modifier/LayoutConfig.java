package uk.ac.ic.doc.neuralnets.gui.modifier;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphItem;
import org.eclipse.zest.layouts.LayoutAlgorithm;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.GridLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.HorizontalShift;
import org.eclipse.zest.layouts.algorithms.RadialLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.VerticalLayoutAlgorithm;

import uk.ac.ic.doc.neuralnets.coreui.ZoomingInterfaceManager;
import uk.ac.ic.doc.neuralnets.events.EventManager;
import uk.ac.ic.doc.neuralnets.events.GraphUpdateEvent;
import uk.ac.ic.doc.neuralnets.gui.graph.CachingLayout;

/**
 * Create ui for changing the network layout algorithm
 * 
 * @author Peter Coetzee
 */
public class LayoutConfig extends Composite {
	
	private Map<String, LayoutAlgorithm> algorithms;

	private static Logger log = Logger.getLogger(LayoutGraphModifier.class);

	public LayoutConfig(Composite parent, final ZoomingInterfaceManager<Graph,GraphItem> gm) {
		super(parent, SWT.NONE);
		
		GridLayout layout = new GridLayout(2,false);
		setLayout(layout);
		
		Label layoutLabel = new Label(this, SWT.NONE);
		layoutLabel.setText("Layout:");
		layoutLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));
		
		Combo algorithmSelector = new Combo(this, SWT.READ_ONLY);
		algorithmSelector.setItems(
				getAlgorithms().keySet().toArray(
						new String[getAlgorithms().keySet().size()]));
		
		algorithmSelector.select(0);
		
		algorithmSelector.addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}

			public void widgetSelected(SelectionEvent se) {
				Combo selector= (Combo) se.widget;
				final String algorithm=selector.getItem(selector.getSelectionIndex());
				log.trace("Changing layout to: " + algorithm);
				selector.getDisplay().asyncExec(new Runnable(){
					public void run() {
						gm.getGraph().setLayoutAlgorithm(new CachingLayout(getAlgorithms().get(algorithm), false), true);
                        EventManager.get().fire( new GraphUpdateEvent() );
					}
				});
			}
		});
	}

	private Map<String, LayoutAlgorithm> getAlgorithms() {
		if(algorithms == null){
		  algorithms = new HashMap<String, LayoutAlgorithm>();
		  algorithms.put("Spring",
			new SpringLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING));
		  algorithms.put("Grid", 
			new GridLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING));
		  algorithms.put("Horizontal Shift", 
			new HorizontalShift(LayoutStyles.NO_LAYOUT_NODE_RESIZING));
		  algorithms.put("Radial", 
			new RadialLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING));
		  algorithms.put("Vertical" , 
			new VerticalLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING));
		}
		return algorithms;
	}
}
