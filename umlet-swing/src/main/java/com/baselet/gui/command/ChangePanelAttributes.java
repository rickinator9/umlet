package com.baselet.gui.command;

import com.baselet.control.Main;
import com.baselet.diagram.DiagramHandler;
import com.baselet.element.interfaces.GridElement;
import com.baselet.gui.CurrentGui;
import com.baselet.gui.pane.OwnSyntaxPane;

public class ChangePanelAttributes extends StateCaretCommand {
	private final GridElement entity;

	public ChangePanelAttributes(GridElement e, String oldState, String newState, int oldCaret, int newCaret) {
		super(oldState, newState, oldCaret, newCaret);
		entity = e;
	}

	public GridElement getEntity() {
		return entity;
	}

	@Override
	public void execute(DiagramHandler handler) {
		super.execute(handler);
		entity.setPanelAttributes(newState);
		entity.repaint();

		GridElement gridElement = Main.getInstance().getEditedGridElement();
		if (gridElement != null && gridElement.equals(entity)) {
			OwnSyntaxPane pane = CurrentGui.getInstance().getGui().getPropertyPane();
			pane.switchToElement(gridElement);

			if (pane.getText().length() >= newCaret) {
				pane.getTextComponent().setCaretPosition(newCaret);
			}
		}
	}

	@Override
	public void undo(DiagramHandler handler) {
		// AB: Do not call super.undo() which would deselect the entity
		entity.setPanelAttributes(oldState);
		entity.repaint();

		GridElement gridElement = Main.getInstance().getEditedGridElement();
		if (gridElement != null && gridElement.equals(entity)) {
			OwnSyntaxPane pane = CurrentGui.getInstance().getGui().getPropertyPane();
			pane.switchToElement(gridElement);

			if (pane.getText().length() >= oldCaret) {
				pane.getTextComponent().setCaretPosition(oldCaret);
			}
		}
	}

	@Override
	public boolean isMergeableTo(Command c) {
		// method is not mergeable (to allow undo of property changes)
		return false;
	}

	@Override
	public Command mergeTo(Command c) {
		ChangePanelAttributes tmp = (ChangePanelAttributes) c;
		return new ChangePanelAttributes(getEntity(), tmp.getOldState(), getNewState(), tmp.getOldCaret(), getNewCaret());
	}

	@Override
	public String toString() {
		return "Changestate from " + getOldState() + " to " + getNewState();
	}
}
