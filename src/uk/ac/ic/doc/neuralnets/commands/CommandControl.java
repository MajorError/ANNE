package uk.ac.ic.doc.neuralnets.commands;

import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import uk.ac.ic.doc.neuralnets.events.EventManager;

/**
 * Implements undo and redo functionality. The addCommand() method adds a new
 * stack and runs it, and the undo() and redo() methods can be called from the
 * GUI.
 * 
 * @author ig106 and ctm06
 * 
 */
public class CommandControl {

	private final int MAX_COMMANDS = 10;
	private int numCommands;

	private LinkedList<Command> undoStack;
	private LinkedList<Command> redoStack;

	private BlockingQueue<Command> todo = new LinkedBlockingQueue<Command>();
	private boolean runDispatcher = true;

	private static final Logger log = Logger.getLogger(CommandControl.class);

	public CommandControl() {
		numCommands = 0;
		undoStack = new LinkedList<Command>();
		redoStack = new LinkedList<Command>();
		createDispatcherThread();
	}

	/**
	 * Executes a command and adds it to the stack so it can be undone and
	 * redone.
	 * 
	 * @param command
	 */
	public void addCommand(Command command) {
		command.setUndo(false);
		execute(command);
		addCommand(command, undoStack);
		redoStack.clear();
		numCommands = undoStack.size();

	}

	/**
	 * Undoes the most recent command.
	 */
	public void undo() {
		if (!canUndo())
			return;

		Command command = undoStack.removeFirst();
		numCommands--;
		undoCommand(command);
		addCommand(command, redoStack);

		EventManager.get().fire(new CommandEvent());
	}

	/**
	 * Redoes the last command that was undone.
	 */
	public void redo() {
		if (!canRedo())
			return;

		Command command = redoStack.removeFirst();
		numCommands--;
		redoCommand(command);
		addCommand(command, undoStack);

		EventManager.get().fire(new CommandEvent());
	}

	/**
	 * Returns boolean value of ability to undo.
	 * 
	 * @return Boolean of ability to undo.
	 */
	public boolean canUndo() {
		return !undoStack.isEmpty();
	}

	/**
	 * Returns boolean value of ability to redo.
	 * 
	 * @return Boolean of ability to redo.
	 */
	public boolean canRedo() {
		return !redoStack.isEmpty();
	}

	private void addCommand(Command command, LinkedList<Command> stack) {
		stack.addFirst(command);
		numCommands++;
		while (numCommands > MAX_COMMANDS) {
			stack.removeLast();
			numCommands--;
		}
	}

	private void undoCommand(Command command) {
		log.debug("Undoing " + command);
		command.setUndo(true);
		execute(command);
	}

	private void redoCommand(Command command) {
		command.setUndo(false);
		execute(command);
	}

	public void reset() {
		undoStack.clear();
		redoStack.clear();
	}

	private void execute(Command command) {
		todo.add(command);
	}

	public void stopDispatcher() {
		runDispatcher = false;
	}

	private void createDispatcherThread() {
		new Thread(new Runnable() {

			public void run() {
				Thread.currentThread().setName("Command Dispatcher");
				while (runDispatcher) {
					try {
						Command next = todo.take();
						try {
							next.run();
						} catch (Exception e) {
							log.error("Unable to run command: "
									+ e.getMessage(), e);
						}
					} catch (InterruptedException ex) {
						// no-op; just wait for the next command
					}
				}
			}

		}).start();
	}
}
