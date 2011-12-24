package uk.ac.ic.doc.neuralnets.gui;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;

import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphItem;
import uk.ac.ic.doc.neuralnets.coreui.ZoomingInterfaceManager;
import uk.ac.ic.doc.neuralnets.events.Event;
import uk.ac.ic.doc.neuralnets.events.EventHandler;
import uk.ac.ic.doc.neuralnets.events.EventManager;
import uk.ac.ic.doc.neuralnets.graph.neural.NeuralNetworkSimulationEvent;
import uk.ac.ic.doc.neuralnets.graph.neural.NeuralNetworkTickEvent;
import uk.ac.ic.doc.neuralnets.gui.listeners.ContinueQuestion;
import uk.ac.ic.doc.neuralnets.gui.ImageHandler;

/**
 * Creates the user interface for the Run tab. The Run tab listens syncronously
 * for NeuralNetworkSimulationEvents and NeuralNetworkTickEvents.
 * 
 * @see NeuralNetworkTickEvent
 * @see NeuralNetworkSimulationEvent
 * 
 * @author Fred van den Driessche
 * 
 */
public class RunPanel implements EventHandler {
	private static final Logger log = Logger.getLogger(RunPanel.class);

	private ZoomingInterfaceManager<Graph, GraphItem> gm;
	private Composite parent;

	private Button masterRun; // run indefinitely
	private Button masterPause;// pause

	private Button stepper; // step once

	private Spinner stepCount;
	private Button stepRun;
	private Button stepPause;
	private Button stepReset;
	private Label count;

	private Button reset;

	private Label tcount;

	/**
	 * Create the Run tab.
	 * 
	 * @param parent
	 *            - the tab container
	 * @param gm
	 *            - the graph manager
	 */
	public RunPanel(Composite parent,
			ZoomingInterfaceManager<Graph, GraphItem> gm) {
		this.parent = parent;
		this.gm = gm;
		EventManager.get().registerSynchro(NeuralNetworkSimulationEvent.class,
				this);
		EventManager.get().registerSynchro(NeuralNetworkTickEvent.class, this);
		build();
	}

	/*
	 * Contsruct the panel.
	 */
	private void build() {

		parent.setLayout(new GridLayout(4, true));

		buildMasterControls();
		buildStepper();
		buildCountStepper();
		buildCounter();
		buildReset();

	}

	/*
	 * Tick indefinitely.
	 */
	private void buildMasterControls() {

		masterRun = new Button(parent, SWT.PUSH);
		masterRun.setImage(ImageHandler.get().getIcon("control_play"));
		masterRun.setText("Run");
		masterRun.setAlignment(SWT.LEFT);

		masterRun.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				log.info("Running Network");
				tcount.setText("0");
				gm.getUtils().runNetwork();

				disable(stepper);
				disable(masterRun);
				enable(masterPause);
				disable(stepCount);
				disable(stepRun);
				disable(stepReset);
				disable(stepPause);
				disable(reset);
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});

		GridData runData = new GridData(10, 50);
		runData.horizontalSpan = 2;
		runData.horizontalAlignment = GridData.FILL;
		masterRun.setLayoutData(runData);

		masterPause = new Button(parent, SWT.PUSH);
		masterPause.setImage(ImageHandler.get().getIcon("control_pause"));
		masterPause.setText("Pause");
		masterPause.setAlignment(SWT.RIGHT);
		masterPause.setEnabled(false);

		masterPause.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent arg0) {
			}

			public void widgetSelected(SelectionEvent arg0) {
				log.info("Pause Network Run");
				gm.getUtils().pauseNetwork();
			}

		});

		GridData pauseData = new GridData(10, 50);
		pauseData.horizontalSpan = 2;
		pauseData.horizontalAlignment = GridData.FILL;
		masterPause.setLayoutData(pauseData);

		buildSeparator(parent);
	}

	/*
	 * Tick once.
	 */
	private void buildStepper() {

		stepper = new Button(parent, SWT.PUSH);
		stepper.setText("Step Network");
		stepper.setImage(ImageHandler.get().getIcon("arrow_rotate_clockwise"));
		GridData stepData = new GridData(10, 50);
		stepData.horizontalAlignment = SWT.FILL;
		stepData.horizontalSpan = 4;
		stepper.setLayoutData(stepData);

		stepper.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent arg0) {
			}

			public void widgetSelected(SelectionEvent arg0) {
				tcount.setText("0");
				log.info("Running Network");
				gm.getUtils().runNetwork(1);

				disable(stepper);
				disable(masterRun);
				disable(masterPause);
				disable(stepCount);
				disable(stepRun);
				disable(stepReset);
				disable(stepPause);
				disable(reset);
			}

		});

		buildSeparator(parent);

	}

	/*
	 * run for a number of ticks
	 */
	private void buildCountStepper() {

		Label runLabel = new Label(parent, SWT.NONE);
		runLabel.setText("Step ");

		GridData runLblData = new GridData();
		runLblData.horizontalSpan = 2;
		runLabel.setLayoutData(runLblData);

		stepRun = new Button(parent, SWT.PUSH);
		stepRun.setImage(ImageHandler.get().getIcon("control_play"));
		stepRun.setText("Run Steps");

		stepRun.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent arg0) {
			}

			public void widgetSelected(SelectionEvent arg0) {
				tcount.setText("0");
				int count = stepCount.getSelection();

				log.info("Running network");
				gm.getUtils().runNetwork(count);

				disable(masterRun);
				disable(masterPause);
				disable(stepper);
				disable(stepCount);
				disable(stepRun);
				disable(stepReset);
				enable(stepPause);
				disable(reset);
			}
		});

		GridData runData = new GridData(GridData.FILL_HORIZONTAL);
		runData.horizontalSpan = 2;
		stepRun.setLayoutData(runData);

		stepCount = new Spinner(parent, SWT.BORDER);
		// defualt, min, max, decimal digits, increment, page increment.
		stepCount.setValues(1000, 2, 99999, 0, 1, 50);

		GridData countData = new GridData(GridData.FILL_HORIZONTAL);
		countData.horizontalSpan = 2;
		stepCount.setLayoutData(countData);

		stepPause = new Button(parent, SWT.PUSH);
		stepPause.setImage(ImageHandler.get().getIcon("control_pause"));
		stepPause.setText("Pause Steps");
		stepPause.setEnabled(false);
		stepPause.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent arg0) {

			}

			public void widgetSelected(SelectionEvent arg0) {
				log.info("Pausing network");
				gm.getUtils().pauseNetwork();
			}

		});

		GridData pauseData = new GridData(GridData.FILL_HORIZONTAL);
		pauseData.horizontalSpan = 2;
		stepPause.setLayoutData(pauseData);

		Label stepLabel = new Label(parent, SWT.NONE);
		stepLabel.setText("times");

		GridData stepLblData = new GridData();
		stepLblData.horizontalSpan = 2;
		stepLabel.setLayoutData(stepLblData);

		stepReset = new Button(parent, SWT.PUSH);
		stepReset.setImage(ImageHandler.get().getIcon("arrow_refresh"));
		stepReset.setText("Reset Steps");
		stepReset.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent arg0) {
			}

			public void widgetSelected(SelectionEvent arg0) {
				log.info("Reseting step count");
				gm.getUtils().runNetwork(0);
				tcount.setText("0");
			}

		});

		GridData resetData = new GridData(GridData.FILL_HORIZONTAL);
		resetData.horizontalSpan = 2;
		stepReset.setLayoutData(resetData);

		buildSeparator(parent);
	}

	/*
	 * Count the number of ticks so far.
	 */
	private void buildCounter() {

		Label tstep = new Label(parent, SWT.NONE);
		tstep.setText("Steps: ");
		tstep.setAlignment(SWT.RIGHT);

		GridData tstepData = new GridData(GridData.FILL_HORIZONTAL);
		tstep.setLayoutData(tstepData);

		tcount = new Label(parent, SWT.NONE);
		tcount.setText("0");

		GridData tcountData = new GridData(GridData.FILL_HORIZONTAL);
		tcount.setLayoutData(tcountData);

		Label step = new Label(parent, SWT.NONE);
		step.setText("Total: ");
		step.setAlignment(SWT.RIGHT);

		GridData stepData = new GridData(GridData.FILL_HORIZONTAL);
		step.setLayoutData(stepData);

		count = new Label(parent, SWT.NONE);
		count.setText("0");

		GridData countData = new GridData(GridData.FILL_HORIZONTAL);
		count.setLayoutData(countData);

		buildSeparator(parent);

	}

	/*
	 * The reset button regenerates the synaptic weights, sets neurones back to
	 * their initial charge and tick count to 0, and total tick counter to 0.
	 */
	private void buildReset() {

		reset = new Button(parent, SWT.PUSH);
		reset.setText("Reset Network");
		reset.setImage(ImageHandler.get().getIcon("lightning"));

		reset.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent arg0) {
			}

			public void widgetSelected(SelectionEvent arg0) {
				log.info("Resetting network");
				String desc = "This will reset all neurone charges and synapse weights";
				if (ContinueQuestion.ask(reset.getShell(), desc)) {
					count.setText("0");
					tcount.setText("0");
					gm.getUtils().resetNetwork();
					log.info("Network reset");
				} else {
					log.info("Reset cancelled");
				}
			}

		});

		GridData resetData = new GridData(10, 50);
		resetData.horizontalSpan = 4;
		resetData.horizontalAlignment = GridData.FILL;

		reset.setLayoutData(resetData);

		buildSeparator(parent);

	}

	private void buildSeparator(Composite parent) {
		Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData sepData = new GridData(GridData.FILL_HORIZONTAL);
		sepData.horizontalSpan = 4;
		separator.setLayoutData(sepData);
	}

	/*
	 * This is here because i'm too lazy to write out button.setEnabled(true)
	 */
	private void enable(Control c) {
		c.setEnabled(true);
	}

	/*
	 * This is here because i'm too lazy to write out button.setEnabled(false)
	 */
	private void disable(Control c) {
		c.setEnabled(false);
	}

	/*
	 * Event handling stuff here...
	 */
	public void flush() {
		// no-op;
	}

	public void handle(Event e) {

		/*
		 * Handle when the network stops running.
		 */
		if (e instanceof NeuralNetworkSimulationEvent) {
			final boolean started = ((NeuralNetworkSimulationEvent) e)
					.started();
			Display.getDefault().syncExec(new Runnable() {

				public void run() {
					if (!started) {
						enable(masterRun);
						enable(stepRun);
						enable(stepper);
						disable(stepPause);
						disable(masterPause);
						enable(stepCount);
						enable(reset);
						enable(stepReset);
					}
				}
			});
		}

		/*
		 * Update the step counters when network ticks.
		 */
		if (e instanceof NeuralNetworkTickEvent) {
			log.trace("Tick event received");
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					count.setText("" + (Integer.parseInt(count.getText()) + 1));
					tcount.setText(""
							+ (Integer.parseInt(tcount.getText()) + 1));
				}
			});
		}
	}

	public boolean isValid() {
		return true;
	}

	public String getName() {
		return "RunPanel";
	}
}
