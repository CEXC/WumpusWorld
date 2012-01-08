package wumpusworld.solutions.ws1112.JTSBMMSSNR;

import java.util.LinkedList;

import model.wumpusworld.CaveGround;
import model.wumpusworld.Orientation;
import model.wumpusworld.environment.CavePosition;
import model.wumpusworld.environment.NeighbourhoodPerception;

public class RandGesehen extends SituationsStatus {
	@Override
	public void updateStatus(LinkedList<CavePosition> Positionen,
			CaveGround[] Nachbarschaft, NeighbourhoodPerception Wahrnehmung, Orientation Blickrichtung) {
		Anzutreffen = false;
		for(CaveGround Feld : Nachbarschaft) {
			if(Feld == null)
				Anzutreffen = true;
		}
	}
	
	public RandGesehen(boolean Anzutreffen) {
		super(SituationsStatusID.RANDGESEHEN, Anzutreffen);
	}
	public RandGesehen() {
		super(SituationsStatusID.RANDGESEHEN, false);
	}
}
