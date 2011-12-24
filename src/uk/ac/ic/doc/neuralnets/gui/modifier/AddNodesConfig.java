package uk.ac.ic.doc.neuralnets.gui.modifier;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Spinner;

import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphItem;
import uk.ac.ic.doc.neuralnets.coreui.ZoomingInterfaceManager;
import uk.ac.ic.doc.neuralnets.graph.neural.manipulation.EdgeCreatedEvent;
import uk.ac.ic.doc.neuralnets.events.Event;
import uk.ac.ic.doc.neuralnets.events.EventHandler;
import uk.ac.ic.doc.neuralnets.events.EventManager;
import uk.ac.ic.doc.neuralnets.graph.neural.manipulation.NodeCreatedEvent;
import uk.ac.ic.doc.neuralnets.graph.neural.Neurone;
import uk.ac.ic.doc.neuralnets.gui.NeuroneCombo;
import uk.ac.ic.doc.neuralnets.gui.ImageHandler;
import uk.ac.ic.doc.neuralnets.gui.commands.AddNodesCommand;

/**
 * Creates the UI for adding nodes to the network. A number of neurone type, 
 * neurone count pairs are added to a holding pen, an edge probability is set,
 * then the insert button can be clicked.
 * 
 * @author Fred van den Driessche
 */
public class AddNodesConfig extends Composite implements EventHandler {

	private static final Logger log = Logger.getLogger(AddNodesConfig.class);

	private ExpandItem ei;
	private Map<String, Integer> buffer; //type/count holding pen
	private int probability = 50; //edge probability

	private ProgressBar buildProgress;
	private Scale edgeProb;
	private List collector;
	private Button insertGroup;
	private Button addCluster;
	private Button delCluster;

	public AddNodesConfig(Composite parent, final ZoomingInterfaceManager<Graph,GraphItem> gm, ExpandItem ei) {
		super(parent, SWT.NONE);
		this.ei = ei;

		GridLayout layout = new GridLayout(3, false);
		layout.horizontalSpacing = 0;
		layout.marginWidth = 0;
		setLayout(layout);

		Combo neuroneType = createNeuroneCombo();

		createTimesLabel();

		Spinner neuroneCount = createNeuroneCountSpinner();

		createNeuroneList();
		
		createProbLabel();

		Label edgeProbVal = createProbValue();

		createProbSlider();

		createBuildProgress();

		createInsertGroupButton();

		addCluster.addSelectionListener(new AddCluster(neuroneType, neuroneCount));
		delCluster.addSelectionListener(new DelCluster());

		edgeProb.addSelectionListener(new ValueUpdater(edgeProbVal));

		insertGroup.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent arg0) {
			}

			public void widgetSelected(SelectionEvent se) {
				runInsertThread(gm);
			}

		});
	}

	private void createInsertGroupButton() {
		insertGroup = new Button(this, SWT.PUSH | SWT.BORDER);
		insertGroup.setText("Insert Group");
		insertGroup.setEnabled(false);

		GridData gdGroup = new GridData();
		gdGroup.horizontalAlignment = SWT.RIGHT;
		insertGroup.setLayoutData(gdGroup);
	}

	private void createBuildProgress() {
		buildProgress = new ProgressBar(this, SWT.HORIZONTAL | SWT.SMOOTH);
		buildProgress.setVisible(false);

		GridData gdProgress = new GridData(GridData.HORIZONTAL_ALIGN_FILL
				| GridData.GRAB_HORIZONTAL);
		gdProgress.horizontalSpan = 2;
		buildProgress.setLayoutData(gdProgress);
	}

	private void createProbSlider() {
		edgeProb = new Scale(this, SWT.HORIZONTAL);
		edgeProb.setMinimum(0);
		edgeProb.setMaximum(100);
		edgeProb.setIncrement(1);
		edgeProb.setPageIncrement(5);
		edgeProb.setSelection(probability);

		GridData gdProb = new GridData(GridData.HORIZONTAL_ALIGN_FILL
				| GridData.GRAB_HORIZONTAL);
		gdProb.horizontalSpan = 3;
		edgeProb.setLayoutData(gdProb);
	}

	private Label createProbValue() {
		Label edgeProbVal = new Label(this, SWT.NONE);
		edgeProbVal.setText("" + probability);

		GridData gdProbVal = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		gdProbVal.horizontalIndent = 5;
		edgeProbVal.setLayoutData(gdProbVal);

		return edgeProbVal;
	}

	private Label createProbLabel() {
		Label edgeProbLabel = createProbValue();
		edgeProbLabel.setText("Edge Probability:");

		GridData gdProbLbl = new GridData();
		gdProbLbl.horizontalSpan = 2;
		edgeProbLabel.setLayoutData(gdProbLbl);

		return edgeProbLabel;
	}

	private void createNeuroneList() {
		Composite c = new Composite(this, SWT.NONE);
		GridData cData = new GridData(GridData.FILL_BOTH);
		cData.horizontalSpan = 3;
		c.setLayoutData(cData);
		c.setLayout(new GridLayout(2, false));
		
		collector = new List(c, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);

		addCluster = new Button(c, SWT.PUSH | SWT.BORDER);
		addCluster.setImage(ImageHandler.get().getIcon("add"));

		GridData gdAdd = new GridData();
		addCluster.setLayoutData(gdAdd);
		
		delCluster = new Button(c, SWT.PUSH | SWT.BORDER);
		delCluster.setImage(ImageHandler.get().getIcon("delete"));
		delCluster.setEnabled(false);

		GridData gdDel = new GridData();
		delCluster.setLayoutData(gdDel);
		
		GridData gdCollector = new GridData(160, 57);
		gdCollector.horizontalAlignment = GridData.FILL;
		gdCollector.grabExcessHorizontalSpace = true;
		gdCollector.horizontalSpan = 1;
		gdCollector.verticalSpan = 2;
		collector.setLayoutData(gdCollector);
		

	}

	private Spinner createNeuroneCountSpinner() {
		Spinner neuroneCount = new Spinner(this, SWT.BORDER);
		// defualt, min, max, decimal digits, increment, page increment.
		neuroneCount.setValues(100, 2, 1000, 0, 1, 50);

		GridData gdCount = new GridData(GridData.FILL_HORIZONTAL);
		gdCount.horizontalSpan = 2;
		neuroneCount.setLayoutData(gdCount);

		return neuroneCount;
	}

	private Label createTimesLabel() {
		Label timesLabel = new Label(this, SWT.CENTER);
		timesLabel.setText("x");

		GridData gdTimes = new GridData(50, 20);
		// gdTimes.horizontalIndent = 50;
		timesLabel.setLayoutData(gdTimes);

		return timesLabel;
	}

//	private Button createInsertButton() {
//		Button insertNeurone = new Button(this, SWT.PUSH);
//		insertNeurone.setText("Add Neuron");
//
//		GridData gdNeurone = new GridData();
//		gdNeurone.verticalIndent = 3;
//		insertNeurone.setLayoutData(gdNeurone);
//
//		return insertNeurone;
//	}

	private Combo createNeuroneCombo() {
		NeuroneCombo neuroneType = new NeuroneCombo(this, Neurone.class);

		GridData gdType = new GridData(GridData.HORIZONTAL_ALIGN_FILL
				| GridData.GRAB_HORIZONTAL);
		gdType.horizontalSpan = 3;
		neuroneType.setLayoutData(gdType);

		return neuroneType.getCombo();
	}
	
	public Map<String, Integer> getBuffer() {
		if (buffer == null) {
			buffer = new HashMap<String, Integer>();
		}
		return buffer;
	}

	private void logBuffer() {
		String strLog = "Buffer state:\n";
		for (Map.Entry<String, Integer> entry : getBuffer().entrySet()) {
			strLog += "\t'" + entry.getKey() + "' : " + entry.getValue() + "\n";
		}
		log.trace(strLog);
	}

	/**
	 * Updates the value of the probability "text" while using the slider
	 * 
	 */
	private class ValueUpdater implements SelectionListener {

		private Label label;

		public ValueUpdater(Label label) {
			this.label = label;
		}

		public void widgetDefaultSelected(SelectionEvent se) {
		}

		public void widgetSelected(SelectionEvent se) {
			Scale s = (Scale) se.widget;
			probability = s.getSelection();
			label.setText("" + probability);
			label.redraw();
		}

	}

	/**
	 * Allows for selecting the number of nodes to using a spinner and to select
	 * neurone type via a combo box, to add to list of node groups to add.
	 * 
	 */
	public class AddCluster implements SelectionListener {

		private Combo typeSelector;
		private Spinner count;

		public AddCluster(Combo type, Spinner count) {
			this.typeSelector = type;
			this.count = count;
		}

		public void widgetDefaultSelected(SelectionEvent se) {
		}

		public void widgetSelected(SelectionEvent se) {
			String nType = typeSelector.getItem(
					typeSelector.getSelectionIndex());
			int nCount = count.getSelection();

			log.trace("Adding cluster type '" + nType + "' x " + nCount);
			
			//Accumulate counts for similar neurone types in the same entry
			if (getBuffer().containsKey(nType)) {
				getBuffer().put(nType, getBuffer().get(nType) + nCount);
			} else {
				getBuffer().put(nType, nCount);
			}
			logBuffer();

			//Add to the list
			collector.add(nCount + " x " + nType);

			//Enable the insert and delete buttons, if not already
			if (!insertGroup.isEnabled())
				insertGroup.setEnabled(true);
			if (!delCluster.isEnabled())
				delCluster.setEnabled(true);

			log.trace("Added group");
		}

	}

	/**
	 * Delete clusters from the list. 
	 *
	 */
	public class DelCluster implements SelectionListener {

		public void widgetDefaultSelected(SelectionEvent se) {
		}

		public void widgetSelected(SelectionEvent se) {
			if (collector.getSelectionCount() < 1)
				return;

			//items to remove from the buffer.
			String[] items = collector.getSelection();

			for (String item : items) {
				String[] itemParts = item.split(" x ");
				String nType = itemParts[1];
				int nCount = Integer.parseInt(itemParts[0]);

				log.trace("Removing cluster type " + nType + " x " + nCount);

				Integer curVal = getBuffer().get(nType);

				if ((curVal - nCount) > 0) {
					getBuffer().put(nType, curVal - nCount);
				} else { //remove buffer entry if none left
					getBuffer().remove(nType);
				}

			}
			
			//remove the items from the list
			collector.remove(collector.getSelectionIndices());
			
			//disable buttons if necessary
			if (collector.getItemCount() == 0) {
				insertGroup.setEnabled(false);
				delCluster.setEnabled(false);
			}
			logBuffer();
		}

	}

	public int getEdgeProbability() {
		return probability;
	}

	private void runInsertThread(ZoomingInterfaceManager<Graph,GraphItem> gm) {
        startInsert();
		gm.getCommandControl().addCommand(new AddNodesCommand( gm, getBuffer(),
                getEdgeProbability() / 100.0 ) );
	}

	public void startInsert() {
		log.trace("Starting insert");
		this.getDisplay().syncExec(new Runnable() {
			public void run() {
				buildProgress.setMaximum(100);
				buildProgress.setVisible(true);
			}
		});
        EventManager.get().registerAsync( NodeCreatedEvent.class, this );
        EventManager.get().registerAsync( EdgeCreatedEvent.class, this );
	}

	public void endInsert() {
		log.trace("Finishing insert");
        final EventHandler h = this;
        new Thread( new Runnable() {
            public void run() {
                EventManager.get().deregisterAsync( NodeCreatedEvent.class, h );
                EventManager.get().deregisterAsync( EdgeCreatedEvent.class, h );
            }
        } ).start();
		this.getDisplay().asyncExec(new Runnable() {
			public void run() {
				buildProgress.setVisible(false);
				collector.removeAll();
				insertGroup.setEnabled(false);
                log.trace( "Reset GUI" );
			}
		});
	}

	public void setProgress(final int value) {
		log.trace("Setting progress to " + value);
		this.getDisplay().asyncExec(new Runnable() {
			public void run() {
				buildProgress.setSelection(value);
			}
		});
	}


    public void flush() {
    }

    public void handle( Event e ) {
        log.trace( e );
        if ( e instanceof NodeCreatedEvent ) handle( (NodeCreatedEvent) e );
        else if ( e instanceof EdgeCreatedEvent )
            handle( (EdgeCreatedEvent) e );
    }

    public void handle( NodeCreatedEvent e ) {
        setProgress( Math.round( 50 * e.getNodeNumber() / e.getNodeCount() ) );
    }

    public void handle( EdgeCreatedEvent e ) {
        int v = 50 + Math.round( 50 * e.getEdgeNumber() / e.getEdgeCount() );
        setProgress( v );
        if ( v > 99 )
            endInsert();
    }

    public boolean isValid() {
        return true;
    }
    
    public String getName() {
        return "AddNodesConfigProgressUpdater";
    }
    
	// private void incProgress(final int value){
	// log.trace("Incrementing progress " + value);
	// this.getDisplay().asyncExec(new Runnable(){
	// public void run() {
	// buildProgress.setSelection(buildProgress.getSelection() + value);
	// }
	// });
	// }

	protected void resize() {
		ei.setHeight(computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
	}

}
