package wumpusworld.solutions.ws1112.JTSBMMSSNR;

import java.util.LinkedList;

import model.wumpusworld.CaveGround;
import model.wumpusworld.CaveGroundType;
import model.wumpusworld.environment.CavePosition;
import model.wumpusworld.environment.NeighbourhoodPerception;

public class Gefangen extends SituationsStatus {
	@Override
	public void updateStatus(LinkedList<CavePosition> Positionen,
			CaveGround[] Nachbarschaft, NeighbourhoodPerception Wahrnehmung) {
		Anzutreffen = false;
		if( ((Wahrnehmung.getNorth() == null) || (Wahrnehmung.getNorth().getType() == CaveGroundType.PIT)) &&
			((Wahrnehmung.getEast() == null) || (Wahrnehmung.getEast().getType() == CaveGroundType.PIT)) &&
			((Wahrnehmung.getSouth() == null) || (Wahrnehmung.getSouth().getType() == CaveGroundType.PIT)) &&
			((Wahrnehmung.getWest() == null) || (Wahrnehmung.getWest().getType() == CaveGroundType.PIT)) )
			Anzutreffen = true;
	}
	
	public Gefangen(boolean Anzutreffen) {
		super(SituationsStatusID.GEFANGEN, Anzutreffen);
	}
	public Gefangen() {
		super(SituationsStatusID.GEFANGEN, false);
	}
}
