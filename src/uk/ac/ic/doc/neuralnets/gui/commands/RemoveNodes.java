package uk.ac.ic.doc.neuralnets.gui.commands;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Display;

import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphItem;
import uk.ac.ic.doc.neuralnets.coreui.ZoomingInterfaceManager;
import uk.ac.ic.doc.neuralnets.graph.Edge;
import uk.ac.ic.doc.neuralnets.graph.Node;
import uk.ac.ic.doc.neuralnets.graph.neural.NeuralNetwork;

public class RemoveNodes {

	private static final Logger log = Logger.getLogger(RemoveNodes.class);
	
	Stack<NeuralNetwork> zoomLevels;
	Stack<NeuralNetwork> tempZoomLevels;
	Stack<Integer> zoomIDs;
	Stack<Integer> tempZoomIDs;

	/**
	 * Removes the Network that is passed to it.
	 * 
	 * @param networkToRemove
	 *            Networks to remove
	 * @param gm
	 *            The ZoomingInterfaceManager<Graph,GraphItem> to remove network from
	 * @param networkToRemoveFrom
	 *            Parent of the network to be removed
	 */
	public void remove(NeuralNetwork networkToRemove, ZoomingInterfaceManager<Graph,GraphItem> gm,
			NeuralNetwork networkToRemoveFrom) {
		List<NeuralNetwork> networksToRemove = new LinkedList<NeuralNetwork>();
		networksToRemove.add(networkToRemove);
		remove(networksToRemove, gm, networkToRemoveFrom);
	}

	/**
	 * Removes the Networks that are passed to it.
	 * 
	 * @param networksToRemove
	 *            List of networks to remove
	 * @param gm
	 *            The ZoomingInterfaceManager<Graph,GraphItem> to remove networks from
	 * @param networkToRemoveFrom
	 *            Parent of the network to be removed
	 */
	@SuppressWarnings("unchecked")
	public void remove(final List<NeuralNetwork> networksToRemove,
			final ZoomingInterfaceManager<Graph,GraphItem> gm, final NeuralNetwork networkToRemoveFrom) {

		initialise(gm);

		final LinkedList<Integer> zoomPath = calcZoomPath(tempZoomLevels,
				tempZoomIDs, networksToRemove);

		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				while (gm.getZoomLevels().size() > 1)
					gm.zoomOut();

				removeNetworks(networksToRemove, networkToRemoveFrom, gm);

				zoomAlongPath(zoomPath, gm);
				gm.getGraph().getSelection().clear();
			}
		});
	}
	
	/**
	 * Removes the nodes and edges that are passed to it.
	 * 
	 * @param edges The edges that are to be removed
	 * @param nodes The nodes that are to be removed
	 * @param gm The ZoomingInterfaceManager<Graph,GraphItem> to remove networks from
	 * @param networkToRemoveFrom Parent of the network to be removed
	 */
	@SuppressWarnings("unchecked")
	public void remove(final Collection<Edge> edges,
			final Collection<Node> nodes, final ZoomingInterfaceManager<Graph,GraphItem> gm,
			final NeuralNetwork networkToRemoveFrom) {

		initialise(gm);

		final LinkedList<Integer> zoomPath = new LinkedList<Integer>();

		boolean canZoom = true;
		while (!tempZoomIDs.isEmpty() && canZoom) {
			for (Node n : tempZoomLevels.peek().getNodes()) {
				for (Node node : nodes) {
					if (node.getID() == n.getID()
							&& n.getID() == tempZoomIDs.peek()
							&& node instanceof NeuralNetwork) {
						canZoom = false;
						break;
					}
				}
				if (!canZoom)
					break;
			}
			if (!canZoom)
				break;

			zoomPath.addLast(tempZoomIDs.peek());
			tempZoomLevels.pop();
			tempZoomIDs.pop();
		}

		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				while (gm.getZoomLevels().size() > 1)
					gm.zoomOut();

				networkToRemoveFrom.getEdges().removeAll(edges);
				networkToRemoveFrom.getNodes().removeAll(nodes);
				gm.redrawCurrentView();

				zoomAlongPath(zoomPath, gm);
				gm.getGraph().getSelection().clear();
			}
		});

	}

	/**
	 * Calculates how much of the current zoom path can be zoomed down after the
	 * network is deleted.
	 */
	private LinkedList<Integer> calcZoomPath(
			Stack<NeuralNetwork> tempZoomLevels, Stack<Integer> tempZoomIDs,
			List<NeuralNetwork> networksToRemove) {

		LinkedList<Integer> zoomPath = new LinkedList<Integer>();

		boolean canZoom = true;
		while (!tempZoomIDs.isEmpty() && canZoom) {
			for (Node n : tempZoomLevels.peek().getNodes()) {
				for (NeuralNetwork networkToRemove : networksToRemove) {
					if (networkToRemove.getID() == n.getID()
							&& n.getID() == tempZoomIDs.peek()) {
						canZoom = false;
						break;
					}
				}
				if (!canZoom)
					break;
			}
			if (!canZoom)
				break;

			zoomPath.addLast(tempZoomIDs.peek());
			tempZoomLevels.pop();
			tempZoomIDs.pop();
		}

		return zoomPath;

	}

	/**
	 * Initialises data structures needed to remove nodes.
	 * 
	 * @param gm The ZoomingInterfaceManager<Graph,GraphItem> of the network containing the nodes to be removed
	 */
	private void initialise(ZoomingInterfaceManager<Graph,GraphItem> gm) {
		zoomLevels = (Stack<NeuralNetwork>) gm.getZoomLevels().clone();
		tempZoomLevels = new Stack<NeuralNetwork>();
		zoomIDs = (Stack<Integer>) gm.getZoomIDs().clone();
		tempZoomIDs = new Stack<Integer>();
		while (zoomLevels.size() >= 1) {
			tempZoomLevels.push(zoomLevels.pop());
		}
		while (zoomIDs.size() >= 2) {
			tempZoomIDs.push(zoomIDs.pop());
		}

	}

	/**
	 * Removes the given neural networks from the GUI.
	 */
	private void removeNetworks(List<NeuralNetwork> networksToRemove,
			NeuralNetwork networkToRemoveFrom, final ZoomingInterfaceManager<Graph,GraphItem> gm) {
		for (NeuralNetwork networkToRemove : networksToRemove) {
			networkToRemoveFrom.getNodes().remove(networkToRemove);
		}

		gm.redrawCurrentView();
	}

	/**
	 * Zooms in from the root using the given path of network layer IDs.
	 * 
	 * @param zoomPath
	 *            the path of network layer IDs to zoom into
	 * @param gm
	 *            the network's ZoomingInterfaceManager<Graph,GraphItem>
	 */
	private void zoomAlongPath(LinkedList<Integer> zoomPath, ZoomingInterfaceManager<Graph,GraphItem> gm) {
		boolean canZoom = true;
		while (!zoomPath.isEmpty() && canZoom) {
			canZoom = false;
			for (Node n : gm.getCurrentNetwork().getNodes()) {
				if (n.getID() == zoomPath.get(0) && n instanceof NeuralNetwork) {
					canZoom = true;
					gm.zoomIn((NeuralNetwork) n);
					zoomPath.removeFirst();
					break;
				}
			}
			if (!canZoom)
				break;
		}

	}

}
