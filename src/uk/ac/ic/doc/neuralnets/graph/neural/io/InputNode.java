package uk.ac.ic.doc.neuralnets.graph.neural.io;

import org.apache.log4j.Logger;
import uk.ac.ic.doc.neuralnets.expressions.ast.ASTExpression;
import uk.ac.ic.doc.neuralnets.graph.neural.manipulation.NodeFactory;
import uk.ac.ic.doc.neuralnets.graph.neural.NodeSpecification;
import uk.ac.ic.doc.neuralnets.graph.neural.NeuralNetwork;
import uk.ac.ic.doc.neuralnets.matrix.PartitionableMatrix;
import uk.ac.ic.doc.neuralnets.util.plugins.Plugin;

/**
 * InputNodes are the default method for passing data, from the user or 
 * from external sources, through the network.
 * 
 * InputNodes contain a matrix of Doubles which are fired into the network row 
 * by row whenever a network ticks.
 * 
 * They can optionally contain a corresponding matrix of target values which can
 *  be used for training.
 * 
 * @author Peter Coetzee
 */
public abstract class InputNode extends NeuralNetwork implements Plugin,
		Foldable {

	protected PartitionableMatrix<Double> data;
	protected PartitionableMatrix<Double> targets;
	protected int row = 0, start = 0;
	private static final Logger log = Logger.getLogger(InputNode.class);

	/**
	 * Configures and adds the input node to the network.
	 */
	public InputNode() {
		configure();
		toNetwork();
	}

	/**
	 * Called before nodes are added to the network. Can be used to prompt for 
	 * the location of input data for instance.
	 */
	public abstract void configure();

	/**
	 * Matrix of data to be passed through the network.
	 * @return matrix of data values
	 */
	public PartitionableMatrix<Double> getData() {
		return data;
	}

	/**
	 * Matrix of target test data
	 * @return matrix of target values
	 */
	public PartitionableMatrix<Double> getTargets() {
		return targets;
	}

	/**
	 * Sends data to the network.
	 * 
	 * @return Itself.
	 */
	public NeuralNetwork toNetwork() {
		checkData();
		if (getNodes().size() > 0)
			return this;
		for (int i = 0; i < data.getWidth(); i++) {
			final int curr = i;
			NodeSpecification<IONeurone> ns = new NodeSpecification<IONeurone>(
					IONeurone.class);
			ns.set("Squash Function", new ASTExpression(0d) {
				@Override
				public Double evaluate() {
					return getData().getPartitioned(curr, row);
				}

				public Double evaluateThis(Object o) {
					return evaluate();
				}
			});
			ns.set("Trigger", new ASTExpression(0d));
			addNode(NodeFactory.get().create(ns));
		}
		return this;
	}

	/**
	 * Set the current row of data to use for input. Is fold-sensitive (row N is
	 * different per fold).
	 * 
	 * @param row
	 *            The number of the row to seek to
	 */
	public void setRow(int row) {
		this.row = start + row;
	}

	public void fold(int foldNumber, int folds) {
		checkData();
		int numPerFold = data.getHeight() / folds;
		start = numPerFold * foldNumber;
		int end = start + numPerFold;
		log.debug("Partitioning along fold " + foldNumber + "/" + folds
				+ " with params [" + start + "," + data.getWidth() + "] - ["
				+ end + "," + data.getWidth() + "]");
		data.partition(start, data.getWidth() - 1, end, data.getWidth() - 1);
		targets.partition(start, targets.getWidth() - 1, end, targets
				.getWidth() - 1);
	}

	private void checkData() {
		if (data == null) {
			log.warn("No data yet, requesting configuration!");
			configure();
		}
	}

	@Override
	public String toString() {
		return "Input Node (" + getID() + ")";
	}

	/**
	 * Tear-down housekeeping for when the node is removed from the graph.
	 */
	public abstract void destroy();

	/**
	 * Called when configuration data is already in memory and the user need not
	 * be promted for it again.
	 */
	public abstract void recreate();

}
