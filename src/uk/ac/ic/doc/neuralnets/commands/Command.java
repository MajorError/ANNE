package uk.ac.ic.doc.neuralnets.commands;

import uk.ac.ic.doc.neuralnets.events.EventManager;

/**
 * Action that can be undone or redone.
 * 
 * @author Ismail
 */

public abstract class Command implements Runnable {

	private boolean undo = false;

	/**
	 * Runs the command, undone is undo state is true, else command executed.
	 */
	public void run() {
		if (undo) {
			undo();
		} else {
			execute();
		}
		EventManager.get().fire(new CommandEvent());
	}

	/**
	 * Returns the value of whether the command is set to undo.
	 * 
	 * @return Boolean commands undo state.
	 */
	public boolean isUndo() {
		return undo;
	}

	/**
	 * Sets the commands state of undo.
	 * 
	 * @param undo
	 *            Boolean for undo state.
	 */
	public void setUndo(boolean undo) {
		this.undo = undo;
	}

	protected abstract void undo();

	protected abstract void execute();

}
