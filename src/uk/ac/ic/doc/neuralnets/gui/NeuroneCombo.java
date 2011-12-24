package uk.ac.ic.doc.neuralnets.gui;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import uk.ac.ic.doc.neuralnets.events.Event;
import uk.ac.ic.doc.neuralnets.events.EventHandler;
import uk.ac.ic.doc.neuralnets.events.EventManager;
import uk.ac.ic.doc.neuralnets.graph.neural.Neurone;
import uk.ac.ic.doc.neuralnets.graph.neural.NeuroneTypes;
import uk.ac.ic.doc.neuralnets.graph.neural.NewNeuroneTypeEvent;
import uk.ac.ic.doc.neuralnets.graph.neural.NodeSpecification;

public class NeuroneCombo implements EventHandler {
	private static final Logger log = Logger.getLogger(NeuroneCombo.class);

	private Combo types;
	private Class<? extends Neurone> filter;
	private NodeSpecification<Neurone> spec;

	public NeuroneCombo(Composite parent, Class<? extends Neurone> filter) {
		this.filter = filter;
		types = new Combo(parent, SWT.READ_ONLY);

		loadTypes();

		if (types.getItems().length > 0) {
			types.select(0);
			updateSpecification();
		} else {
			types.add("No valid types");
			types.select(0);
			types.setEnabled(false);
		}

		types.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent se) {
			}

			public void widgetSelected(SelectionEvent se) {
				updateSpecification();
			}

		});

		EventManager.get().registerAsync(NewNeuroneTypeEvent.class, this);
	}

	private void loadTypes() {
		Map<String, Class<? extends Neurone>> ts = NeuroneTypes.nodeTypes;
		for (Entry<String, Class<? extends Neurone>> e : ts.entrySet())
			if (filter.isAssignableFrom(e.getValue()))
				addType(e.getKey());
	}

	private void addType(String type) {
		int pos = 0;
		String[] items = types.getItems();
		while (pos < items.length && type.compareTo(items[pos]) > 1)
			pos++;
		types.add(type, pos);
	}

	public Combo getCombo() {
		return types;
	}

	public void setLayoutData(Object layout) {
		types.setLayoutData(layout);
	}

	public void updateSpecification() {
		String name = types.getItem(types.getSelectionIndex());
		log.trace("Updating specification to " + name);
		setSpecification(NeuroneTypes.specFor(name));
	}

	public NodeSpecification<Neurone> getSpecification() {
		return spec;
	}

	public void setSpecification(NodeSpecification<Neurone> spec) {
		this.spec = spec;
	}

	public void handle(Event e) {
		if (!(e instanceof NewNeuroneTypeEvent))
			return;

		final String name = ((NewNeuroneTypeEvent) e).getName();

		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				for (String n : types.getItems())
					if (n.equals(name)) {
						if (spec.getName().equals(name)) {
							updateSpecification();
						}
						return; // No duplicates
					}

				Class<?> type = NeuroneTypes.nodeTypes.get(name);
				if (filter.isAssignableFrom(type)) {
					addType(name);
				}
			}
		});
	}

	public void flush() {
		// no-op
	}

	public boolean isValid() {
		return true;
	}

	public String getName() {
		return "NeuroneComboUpdater";
	}
}
