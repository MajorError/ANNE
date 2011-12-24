package uk.ac.ic.doc.neuralnets.gui.graph.ionodes;

import uk.ac.ic.doc.neuralnets.graph.neural.io.*;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

import uk.ac.ic.doc.neuralnets.events.EventManager;
import uk.ac.ic.doc.neuralnets.events.GraphUpdateEvent;
import uk.ac.ic.doc.neuralnets.matrix.PartitionableMatrix;

public class PunchingInputNode extends InputNode implements Runnable {

	private static final Logger log = Logger.getLogger(PunchingInputNode.class);
	private static final long serialVersionUID = 8553787535315636444L;
	private int inputs;
	private Shell shell;
	private boolean destroyed = false;

	@Override
	public void configure() {
		inputs = -1;
		Display.getDefault().syncExec(this);
		synchronized (this) {
			try {
				while (inputs == -1)
					wait();
				if (inputs == -2) {
					log.trace("Add Punching nodes cancelled");
					data = new PartitionableMatrix<Double>(0, 0);
					targets = new PartitionableMatrix<Double>(0, 0);
					return;
				}
				data = new PartitionableMatrix<Double>(inputs, 1);
				for (int i = 0; i < data.getWidth(); i++) {
					data.set(0d, i, 0);
				}
				targets = new PartitionableMatrix<Double>(1, 1);
			} catch (InterruptedException e) {
				log.error("Interrupted", e);
				return;
			}
		}
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				createSliders(data);
			}
		});
	}

	private void createSliders(PartitionableMatrix<Double> data) {
		shell = new Shell(Display.getDefault(), SWT.BORDER | SWT.TITLE
				| SWT.MODELESS);
		shell.setText("Configure Neurones");
		shell.setLayout(new RowLayout(SWT.HORIZONTAL));
		shell.setLocation(600, 300);

		log.debug(data);

		for (int i = 0; i < data.getWidth(); i++) {
			new PunchControl(shell, i, 0);
		}

		shell.pack();
		shell.open();
	}

	public void awake(int count) {
		synchronized (this) {
			inputs = count;
			notifyAll();
		}
	}

	private void interrupt() {
		synchronized (this) {
			inputs = -2;
			notifyAll();
		}
	}

	public String getName() {
		return "PunchInput";
	}

	public void run() {
		final Shell s = new Shell(Display.getDefault(), SWT.BORDER | SWT.TITLE
				| SWT.CLOSE | SWT.MODELESS);
		s.setText("Neurone Count");
		s.setLocation(300, 300);

		FillLayout layout = new FillLayout(SWT.HORIZONTAL);
		layout.marginHeight = layout.marginWidth = 20;
		layout.spacing = 10;
		s.setLayout(layout);

		final Spinner count = new Spinner(s, SWT.BORDER);
		// default, min, max, decimal digits, increment, page increment.
		count.setValues(2, 1, 10, 0, 1, 2);

		Button insert = new Button(s, SWT.PUSH);
		insert.setText("Insert");

		insert.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent arg0) {
			}

			public void widgetSelected(SelectionEvent arg0) {
				awake(count.getSelection());
				s.dispose();
			}

		});

		s.addShellListener(new ShellListener() {

			public void shellActivated(ShellEvent arg0) {
			}

			public void shellClosed(ShellEvent arg0) {
				interrupt();
			}

			public void shellDeactivated(ShellEvent arg0) {
			}

			public void shellDeiconified(ShellEvent arg0) {
			}

			public void shellIconified(ShellEvent arg0) {
			}

		});

		s.pack();
		s.open();
	}

	private class PunchControl {

		private Button toggle;
		private Scale charge;
		private Text value;

		public PunchControl(Shell parent, final int x, final int y) {
			Composite container = new Composite(parent, SWT.NONE);
			container.setLayout(new GridLayout(1, true));

			toggle = new Button(container, SWT.TOGGLE);
			toggle.setText("ON");
			toggle.setSelection(true);

			toggle.addSelectionListener(new SelectionListener() {

				public void widgetDefaultSelected(SelectionEvent arg0) {
				}

				public void widgetSelected(SelectionEvent arg0) {
					if (toggle.getSelection()) {
						toggle.setText("ON");
						charge.setEnabled(true);
						value.setEnabled(true);
						setCharge(new Double(getRealCharge()), x, y);
					} else {
						toggle.setText("OFF");
						charge.setEnabled(false);
						value.setEnabled(false);
						setCharge(0d, x, y);
					}
				}

			});

			GridData toggleData = new GridData(40, 30);
			toggleData.horizontalAlignment = GridData.CENTER;
			toggle.setLayoutData(toggleData);

			Label chargeLabel = new Label(container, SWT.NONE);
			chargeLabel.setText("Charge:");

			charge = new Scale(container, SWT.VERTICAL);
			charge.setMinimum(0);
			charge.setMaximum(2000);
			charge.setSelection(1000);

			charge.addSelectionListener(new SelectionListener() {

				public void widgetDefaultSelected(SelectionEvent arg0) {
				}

				public void widgetSelected(SelectionEvent arg0) {
					int c = getRealCharge();
					value.setText("" + c);
					setCharge(new Double(c), x, y);
				}

			});

			GridData chargeData = new GridData(GridData.FILL_VERTICAL);
			chargeData.horizontalAlignment = GridData.CENTER;
			charge.setLayoutData(chargeData);

			value = new Text(container, SWT.BORDER);
			value.setText("" + getRealCharge());

			GridData valueData = new GridData(40, 20);
			valueData.horizontalAlignment = SWT.CENTER;
			value.setLayoutData(valueData);

			value.addModifyListener(new ModifyListener() {

				public void modifyText(ModifyEvent arg0) {
					String v = value.getText();
					if (v.length() > 0) {
						if (v.equals("-")) {
							charge.setSelection(1000);
							return;
						}
						try {
							int c = Integer.parseInt(v);
							if (c > 1000 || c < -1000)
								throw new NumberFormatException();
							charge.setSelection(1000 - c);
						} catch (NumberFormatException nfe) {
							log.trace(nfe);
							value.setText("" + getRealCharge());
						}
					} else {
						charge.setSelection(1000);
					}
				}
			});
		}

		private int getRealCharge() {
			return 1000 - charge.getSelection();
		}

		private void setCharge(Double d, int x, int y) {
			log.trace("Setting charge to " + d);
			data.set(d, x, y);
			EventManager.get().fire(new GraphUpdateEvent());
		}

	}

	public void destroy() {
		destroyed = true;
		shell.dispose();
		shell = null;
	}

	public void recreate() {
		destroyed = false;
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				createSliders(data);
			}
		});
	}

}
