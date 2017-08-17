package com.baselet.gui.command;

public abstract class StateCaretCommand extends Command {
	protected final String newState;
	protected final String oldState;
	protected final int oldCaret;
	protected final int newCaret;

	protected StateCaretCommand(String oldState, String newState, int oldCaret, int newCaret) {
		this.newState = newState;
		this.oldState = oldState;
		this.newCaret = newCaret;
		this.oldCaret = oldCaret;
	}

	public String getNewState() {
		return newState;
	}

	public String getOldState() {
		return oldState;
	}

	public int getOldCaret() {
		return oldCaret;
	}

	public int getNewCaret() {
		return newCaret;
	}
}
