package com.baselet.gui.command;

import com.baselet.control.HandlerElementMap;
import com.baselet.control.Main;
import com.baselet.diagram.CustomPreviewHandler;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.SelectorOld;
import com.baselet.element.interfaces.GridElement;
import com.baselet.gui.CurrentGui;
import com.baselet.gui.pane.OwnSyntaxPane;

public class CustomCodePropertyChanged extends StateCaretCommand {
	public CustomCodePropertyChanged(String oldState, String newState, int oldCaret, int newCaret) {
		super(oldState, newState, oldCaret, newCaret);
	}

	@Override
	public void execute(DiagramHandler handler) {
		super.execute(handler);

		GridElement gridElement = Main.getInstance().getEditedGridElement();

		// select grid element if nothing is selected
		if (gridElement == null) {
			SelectorOld selector = CurrentGui.getInstance().getGui().getCurrentCustomHandler().getPreviewHandler().getDrawPanel().getSelector();
			selector.selectAll();
			if (selector.getSelectedElements().size() >= 1) {
				gridElement = selector.getSelectedElements().get(0);
			}
		}

		if (gridElement != null && HandlerElementMap.getHandlerForElement(gridElement) instanceof CustomPreviewHandler) {
			gridElement.setPanelAttributes(newState);

			OwnSyntaxPane pane = CurrentGui.getInstance().getGui().getPropertyPane();
			pane.switchToElement(gridElement);

			if (pane.getText().length() >= newCaret) {
				pane.getTextComponent().setCaretPosition(newCaret);
			}

			gridElement.repaint();
		}
	}

	@Override
	public void undo(DiagramHandler handler) {
		// AB: Do not call super.undo() which would deselect the entity

		GridElement gridElement = Main.getInstance().getEditedGridElement();

		// select grid element
		if (gridElement == null) {
			SelectorOld selector = CurrentGui.getInstance().getGui().getCurrentCustomHandler().getPreviewHandler().getDrawPanel().getSelector();
			selector.selectAll();
			if (selector.getSelectedElements().size() >= 1) {
				gridElement = selector.getSelectedElements().get(0);
			}
		}

		if (gridElement != null && HandlerElementMap.getHandlerForElement(gridElement) instanceof CustomPreviewHandler) {
			gridElement.setPanelAttributes(oldState);

			OwnSyntaxPane pane = CurrentGui.getInstance().getGui().getPropertyPane();
			pane.switchToElement(gridElement);

			if (pane.getText().length() >= oldCaret) {
				pane.getTextComponent().setCaretPosition(oldCaret);
			}

			gridElement.repaint();
		}
	}

	@Override
	public String toString() {
		return "Changestate from " + getOldState() + " to " + getNewState();
	}
}
