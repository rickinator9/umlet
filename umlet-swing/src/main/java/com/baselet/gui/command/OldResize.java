package com.baselet.gui.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.baselet.control.HandlerElementMap;
import com.baselet.control.basics.geom.Point;
import com.baselet.control.basics.geom.PointDouble;
import com.baselet.control.basics.geom.Rectangle;
import com.baselet.control.config.SharedConfig;
import com.baselet.diagram.CurrentDiagram;
import com.baselet.diagram.DiagramHandler;
import com.baselet.element.interfaces.GridElement;
import com.baselet.element.old.element.Relation;
import com.baselet.element.sticking.StickingPolygon;

/**
 * resizing has been merged with Move command and only remains for old grid elements which will not be migrated but removed from the code after some time
 * @deprecated
 */
@Deprecated
public class OldResize extends Command {
	private int currentId = 0;

	private int id;

	private final int diffX;
	private final int diffY;
	private final int diffW;
	private final int diffH;

	private List<OldRelationLinePoint> linePoints;
	private final List<OldMoveLinePoint> moveCommands;
	private final GridElement entity;

	public OldResize(GridElement entity, int diffx, int diffy, int diffw, int diffh) {
		this(entity, new Point(diffx, diffy), diffw, diffh, null);
	}

	// resize for merge
	private OldResize(GridElement entity, int id, Point diffp, int diffw, int diffh,
			List<OldMoveLinePoint> moveCommands, List<OldMoveLinePoint> moveCommands2) {
		this.entity = entity;
		this.id = id;
		this.moveCommands = moveCommands;
		this.moveCommands.addAll(moveCommands2);
		diffX = diffp.x / HandlerElementMap.getHandlerForElement(entity).getGridSize();
		diffY = diffp.y / HandlerElementMap.getHandlerForElement(entity).getGridSize();
		diffW = diffw / HandlerElementMap.getHandlerForElement(entity).getGridSize();
		diffH = diffh / HandlerElementMap.getHandlerForElement(entity).getGridSize();
	}

	public OldResize(GridElement entity, Point diffp, int diffw, int diffh, OldResize first) {
		this.entity = entity;
		moveCommands = new ArrayList<>();
		diffX = diffp.x / HandlerElementMap.getHandlerForElement(entity).getGridSize();
		diffY = diffp.y / HandlerElementMap.getHandlerForElement(entity).getGridSize();
		diffW = (diffw - diffp.x) / HandlerElementMap.getHandlerForElement(entity).getGridSize();
		diffH = (diffh - diffp.y) / HandlerElementMap.getHandlerForElement(entity).getGridSize();

		Rectangle entityRect = this.entity.getRectangle();
		StickingPolygon from = this.entity.generateStickingBorder(entityRect);

		// AB: FIXED: Use this.diffw/this.diffh instead of diffw/diffh as calculation base
		Rectangle newRect = new Rectangle(entityRect.x + diffp.x, entityRect.y + diffp.y, entityRect.width + getDiffw(), entityRect.height + getDiffh());
		StickingPolygon to = this.entity.generateStickingBorder(newRect);

		if (first != null) {
			id = first.id;
			linePoints = first.linePoints;
		}
		else {
			id = currentId;
			currentId++;
			linePoints = getStickingRelationLinePoints(HandlerElementMap.getHandlerForElement(this.entity), from);
		}

		PointDouble diff;
		Point p;
		Relation r;
		for (OldRelationLinePoint lp : linePoints) {
			r = lp.getRelation();
			p = r.getLinePoints().get(lp.getLinePointId());

			diff = from.getLine(lp.getStickingLineId()).diffToLine(to.getLine(lp.getStickingLineId()), p.x + r.getRectangle().x, p.y + r.getRectangle().y);

			DiagramHandler handler = HandlerElementMap.getHandlerForElement(entity);
			moveCommands.add(new OldMoveLinePoint(lp.getRelation(), lp.getLinePointId(), handler.realignToGrid(diff.x), handler.realignToGrid(diff.y)));
		}

	}

	private int getDiffx() {
		return diffX * HandlerElementMap.getHandlerForElement(entity).getGridSize();
	}

	private int getDiffy() {
		return diffY * HandlerElementMap.getHandlerForElement(entity).getGridSize();
	}

	private int getDiffw() {
		return diffW * HandlerElementMap.getHandlerForElement(entity).getGridSize();
	}

	private int getDiffh() {
		return diffH * HandlerElementMap.getHandlerForElement(entity).getGridSize();
	}

	@Override
	public void execute(DiagramHandler handler) {
		super.execute(handler);

		entity.setLocationDifference(getDiffx(), getDiffy());
		entity.setSize(entity.getRectangle().width + getDiffw(), entity.getRectangle().height + getDiffh());
		if (SharedConfig.getInstance().isStickingEnabled()) {
			for (OldMoveLinePoint c : moveCommands) {
				c.execute(handler);
			}
		}
	}

	@Override
	public void undo(DiagramHandler handler) {
		super.undo(handler);
		entity.setLocationDifference(-getDiffx(), -getDiffy());
		entity.setSize(entity.getRectangle().width + -getDiffw(), entity.getRectangle().height + -getDiffh());
		for (OldMoveLinePoint c : moveCommands) {
			c.undo(handler);
		}
		CurrentDiagram.getInstance().getDiagramHandler().getDrawPanel().updatePanelAndScrollbars();
	}

	@Override
	public boolean isMergeableTo(Command c) {
		if (!(c instanceof OldResize)) {
			return false;
		}
		OldResize r = (OldResize) c;
		if (id == r.id) {
			return true;
		}
		return false;
	}

	@Override
	public Command mergeTo(Command c) {
		OldResize tmp = (OldResize) c;
		return new OldResize(entity, Math.max(id, tmp.id), new Point(getDiffx() + tmp.getDiffx(), getDiffy() + tmp.getDiffy()),
				getDiffw() + tmp.getDiffw(), getDiffh() + tmp.getDiffh(), moveCommands, tmp.moveCommands);
	}

	public static List<OldRelationLinePoint> getStickingRelationLinePoints(DiagramHandler handler, StickingPolygon stickingPolygon) {
		List<OldRelationLinePoint> lpts = new ArrayList<>();
		Collection<Relation> rels = handler.getDrawPanel().getOldRelations();
		for (Relation r : rels) {
			PointDouble l1 = r.getAbsoluteCoorStart();
			PointDouble l2 = r.getAbsoluteCoorEnd();
			int c1 = stickingPolygon.isConnected(l1, handler.getGridSize());
			int c2 = stickingPolygon.isConnected(l2, handler.getGridSize());
			if (c1 >= 0) {
				lpts.add(new OldRelationLinePoint(r, 0, c1));
			}
			if (c2 >= 0) {
				lpts.add(new OldRelationLinePoint(r, r.getLinePoints().size() - 1, c2));
			}
		}
		return lpts;
	}
}
