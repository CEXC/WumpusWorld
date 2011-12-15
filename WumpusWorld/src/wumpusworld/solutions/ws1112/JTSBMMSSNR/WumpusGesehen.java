package wumpusworld.solutions.ws1112.JTSBMMSSNR;

import java.util.LinkedList;

import model.wumpusworld.CaveGround;
import model.wumpusworld.environment.CavePosition;
import model.wumpusworld.environment.NeighbourhoodPerception;

public class WumpusGesehen extends SituationsStatus {
	@Override
	public void updateStatus(LinkedList<CavePosition> Positionen,
			CaveGround[] Nachbarschaft, NeighbourhoodPerception Wahrnehmung) {
		Anzutreffen = false;
		if(Wahrnehmung.getCurrentCaveGround().isStench())
			Anzutreffen = true;

	}
	
	public WumpusGesehen(boolean Anzutreffen) {
		super(SituationsStatusID.WUMPUSGESEHEN, Anzutreffen);
	}
	public WumpusGesehen() {
		super(SituationsStatusID.WUMPUSGESEHEN, false);
	}
}
