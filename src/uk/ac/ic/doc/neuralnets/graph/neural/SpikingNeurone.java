package uk.ac.ic.doc.neuralnets.graph.neural;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

import uk.ac.ic.doc.neuralnets.events.EventManager;
import uk.ac.ic.doc.neuralnets.expressions.BindVariable;
import uk.ac.ic.doc.neuralnets.expressions.ExpressionException;
import uk.ac.ic.doc.neuralnets.expressions.ast.ASTExpression;
import uk.ac.ic.doc.neuralnets.expressions.ast.ASTExpressionFactory;
import uk.ac.ic.doc.neuralnets.graph.Node;

/**
 * 
 * @author Peter Coetzee
 */
public class SpikingNeurone extends Neurone {

	private static final Logger log = Logger.getLogger(SpikingNeurone.class);
	private static final long serialVersionUID = -2404653964175715587L;

	@Persistable
	private double recoveryScale, recoverySensitivity, psr, u, psrRecovery,
			chargeUp = 0;
	private transient ASTExpression thalamicInput, synapticDelay;
	private String thalamicString, synapticDelayString;
	private int fired = 0;
	private List<Double[]> delays = new ArrayList<Double[]>();
	private Synapse[] outbound = new Synapse[] {};

	public void setRecoveryScale(ASTExpression e) {
		try {
			recoveryScale = e.evaluate(this);
		} catch (ExpressionException ex) {
			throw new RuntimeException(ex);
		}
	}

	public void setRecoverySensitivity(ASTExpression e) {
		try {
			recoverySensitivity = e.evaluate(this);
		} catch (ExpressionException ex) {
			throw new RuntimeException(ex);
		}
	}

	public void setPostSpikeReset(ASTExpression e) {
		try {
			psr = e.evaluate(this);
		} catch (ExpressionException ex) {
			throw new RuntimeException(ex);
		}
	}

	public void setPSRRecovery(ASTExpression e) {
		try {
			psrRecovery = e.evaluate(this);
		} catch (ExpressionException ex) {
			throw new RuntimeException(ex);
		}
	}

	public void setThalamicInput(ASTExpression e) {
		thalamicString = e.getExpression();
		thalamicInput = e;
		thalamicInput.bind(this);
	}

	public void setSynapticDelay(ASTExpression e) {
		synapticDelayString = e.getExpression();
		synapticDelay = e;
		synapticDelay.bind(this);
	}

	@Persistable
	private ASTExpression getThalamicInput() {
		if (thalamicInput == null) {
			try {
				setThalamicInput(ASTExpressionFactory.get().getExpression(
						thalamicString));
			} catch (ExpressionException ex) {
				log.error("Unexpected expression parse error", ex);
			}
		}
		return thalamicInput;
	}

	@Persistable
	private ASTExpression getSynapticDelay() {
		if (synapticDelay == null) {
			try {
				setThalamicInput(ASTExpressionFactory.get().getExpression(
						synapticDelayString));
			} catch (ExpressionException ex) {
				log.error("Unexpected expression parse error", ex);
			}
		}
		return synapticDelay;
	}

	@Persistable
	@BindVariable("recoveryScale")
	public Double getRecoveryScale() {
		return recoveryScale;
	}

	@Persistable
	@BindVariable("recoverySensitivity")
	public Double getRecoverySensitivity() {
		return recoverySensitivity;
	}

	@Persistable
	@BindVariable("psr")
	public Double getPostSpikeReset() {
		return psr;
	}

	@Persistable
	@BindVariable("psrRecovery")
	public Double getPSRRecovery() {
		return psrRecovery;
	}

	@Override
	public Neurone charge(double amt) {
		chargeUp += amt;
		return this;
	}

	@Override
	public Node<Synapse> tick() {
		if (ticks == 0)
			u = charge * recoverySensitivity;
		ticks++;
		makeDelayStructure();
		try {
			if (charge >= trigger) {
				fired++;
				charge = psr;
				u += psrRecovery;
				fire(getSquashFunction().evaluateThis(this));
				EventManager.get().fire(new NodeFired(this, ticks));
			} else {
				fire(0);
			}
		} catch (ExpressionException ex) {
			throw new RuntimeException(ex);
		}
		tickDelay();
		double i = chargeUp;
		try {
			i += getThalamicInput().evaluateThis(this);
		} catch (ExpressionException ex) {
			throw new RuntimeException(ex);
		}
		charge += 0.5 * (0.04 * Math.pow(charge, 2) + 5 * charge + 140 - u + i);
		charge += 0.5 * (0.04 * Math.pow(charge, 2) + 5 * charge + 140 - u + i);
		u += recoveryScale * (recoverySensitivity * charge - u);
		chargeUp = 0;

		EventManager.get().fire(new NodeChargeUpdateEvent(this));

		return this;
	}

	private void fire(double v) {
        if ( v != 0 )
            for ( Synapse s : out )
                s.fire( v );
//		for (Double[] ds : delays)
//			ds[(ticks - 1) % ds.length] = v;
	}


	private void tickDelay() {
		int i = 0;
		for (Double[] ds : delays) {
			double v = ds[ticks % ds.length];
			if (v != 0d)
				outbound[i].fire(v);
			i++;
		}
	}

	private void makeDelayStructure() {
		if (outbound.length == getOutgoing().size())
			return;
		outbound = getOutgoing().toArray(new Synapse[] {});
		try {
			while (outbound.length > delays.size()) {
				Double[] ds = new Double[Math.max(1, (int) Math
						.round(getSynapticDelay().evaluateThis(this)))];
				for (int i = 0; i < ds.length; i++)
					ds[i] = 0d;
				delays.add(ds);
			}
		} catch (ExpressionException ex) {
			log.error("Couldn't make synaptic delay!", ex);
		}
		while (outbound.length < delays.size())
			delays.remove(0);
		int i = 0;
		for (Double[] ds : delays)
			outbound[i++].setDelay(ds.length);
	}

	@Override
	public String toString() {
		return "Spiking Neurone " + id;
	}

}
