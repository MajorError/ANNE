package uk.ac.ic.doc.neuralnets.graph.neural;

import org.apache.log4j.Logger;

import uk.ac.ic.doc.neuralnets.events.EventManager;
import uk.ac.ic.doc.neuralnets.graph.neural.NodeFired;
import uk.ac.ic.doc.neuralnets.expressions.BindVariable;
import uk.ac.ic.doc.neuralnets.expressions.ExpressionException;
import uk.ac.ic.doc.neuralnets.expressions.ast.ASTExpression;
import uk.ac.ic.doc.neuralnets.expressions.ast.ASTExpressionFactory;
import uk.ac.ic.doc.neuralnets.graph.Node;
import uk.ac.ic.doc.neuralnets.graph.neural.NodeBase;
import uk.ac.ic.doc.neuralnets.graph.neural.EdgeDecoration;
import uk.ac.ic.doc.neuralnets.graph.neural.Persistable;
import uk.ac.ic.doc.neuralnets.util.plugins.PluginLoadException;
import uk.ac.ic.doc.neuralnets.util.plugins.PluginManager;

/**
 * 
 * @author Peter Coetzee
 */
public class Neurone extends NodeBase<Synapse> {

	private static final long serialVersionUID = 4484879793358393890L;
	private static final Logger log = Logger.getLogger(Neurone.class);

	protected double charge = 0, trigger = 0;

	// Made private to ensure access through getSquashFunction();
	private transient ASTExpression squash, initial;
	protected int id, ticks = 0;
	@Persistable
	protected String decorationName = "Arrow";
	protected EdgeDecoration decoration;

	private String squashString;

	public Neurone() {
		getFreshID();
	}

	public void setID(int id) {
		this.id = id;
	}

	public void getFreshID() {
		id = EventManager.get().getUniqueID();
	}

	public void setCharge(double charge) {
		this.charge = charge;
	}

	@Persistable
	public double getCharge() {
		return charge;
	}

	@Persistable
	public double getTrigger() {
		return trigger;
	}

	public void setTrigger(double d) {
		trigger = d;
	}

	public EdgeDecoration getEdgeDecoration() {
		if (decoration == null) {
			try {
				decoration = PluginManager.get().getPlugin(decorationName,
						EdgeDecoration.class);
			} catch (PluginLoadException ex) {
				log
						.error(
								"Couldn't get requested edge decoration! Attempting to fall back to Arrow",
								ex);
				try {
					decoration = PluginManager.get().getPlugin("Arrow",
							EdgeDecoration.class);
				} catch (PluginLoadException ex2) {
					log.error("Couldn't get any edge decoration!", ex2);
				}
			}
		}
		return decoration;
	}

	public void setEdgeDecoration(EdgeDecoration ed) {
		decoration = ed;
		decorationName = ed.getName();
	}

	public void setSquashFunction(ASTExpression e) {
		squashString = e.getExpression();
		squash = e;
		squash.bind(this);
	}

	public ASTExpression getSquashFunction() {
		if (squash == null && !(squashString == null)) {
			try {
				setSquashFunction(ASTExpressionFactory.get().getExpression(
						squashString));
			} catch (ExpressionException ex) {
				log.error("Unexpected expression parse error", ex);
			}
		}
		return squash;
	}

	public void setTrigger(ASTExpression t) {
		try {
			trigger = t.evaluate(this);
		} catch (ExpressionException ex) {
			throw new RuntimeException(ex);
		}
	}

	public void setInitialCharge(ASTExpression c) {
		try {
			charge = c.evaluate(this);
			initial = c;
		} catch (ExpressionException ex) {
			throw new RuntimeException(ex);
		}
	}

	public void reset() {
		if (initial != null)
			setInitialCharge(initial);
		ticks = 0;
	}

	@BindVariable(value = "charge", rebind = true)
	public Double getCurrentCharge() {
		return charge;
	}

	public Neurone charge(double amt) {
		charge += amt;
		return this;
	}

	/**
	 * Ticks the neurone one step forward. Fires the neurone is appropriate.
	 * 
	 * @return Itself.
	 */
	public Node<Synapse> tick() {
		ticks++;
		try {
			if (charge >= trigger) {
				double output = getSquashFunction().evaluateThis(this);
				for (Synapse e : out)
					e.fire(output);
				EventManager.get().fire(new NodeFired(this, ticks));
			}
		} catch (ExpressionException ex) {
			throw new RuntimeException(ex);
		}
		charge = 0;
		return this;
	}

	@BindVariable("id")
	public int getID() {
		return id;
	}

	@Override
	public String toString() {
		return "Neurone " + id;
	}

}
